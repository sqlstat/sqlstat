package com.fan.sqlstat.service.check;

import com.fan.sqlstat.constant.FileType;
import com.fan.sqlstat.model.FileTarget;
import com.fan.sqlstat.model.Rule;
import com.fan.sqlstat.model.SqlHit;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class XmlCheckService implements ChechService {
    private static final Logger logger = LoggerFactory.getLogger(XmlCheckService.class);

    @Resource
    private CommonFileCheckService commonFileCheckService;

    @Resource(name="targetSqlRuleMap")
    private Map<Integer, Rule> targetSqlRuleMap;

    @Override
    public FileTarget check(FileTarget fileTarget) {

        SAXReader reader = new SAXReader();
        if(!fileTarget.getFileType().equals(FileType.XML)){
            return fileTarget;
        }

        FileType fileType = fileTarget.getFileType();
        File file = fileTarget.getFile();
        String projectName = fileTarget.getProject();
        try {
            Document document = reader.read(fileTarget.getFile());
            //ibatis
            if(document.getDocType().getElementName().equals("sqlMap") &&
                    document.getDocType().getSystemID().equals("http://www.ibatis.com/dtd/sql-map-2.dtd")){
                logger.trace("ibatis mapping file found:"+document.getName());
                Element xmlroot = document.getRootElement();
                Iterator it = xmlroot.elementIterator();
                while (it.hasNext()) {
                    Element element = (Element) it.next();
                    if(element.getName().equals("select") || element.getName().equals("insert")
                            || element.getName().equals("update") || element.getName().equals("delete")){

                        StringBuilder stringBuilder = new StringBuilder();
                        for(Node node : element.content()){
                            stringBuilder.append(node.asXML());
                        }
//                        String sql = element.getStringValue().trim();
                        String sql = stringBuilder.toString();
                        logger.trace("ibatis sql found:" + sql);
//                        List<SqlHit> sqlHitList = commonFileCheckService.checkText(sql, fileType, true);
                        List<SqlHit> sqlHitList = checkSql(sql);
                        if(!sqlHitList.isEmpty()){
                            fileTarget.setTarget(true);
                            List<SqlHit> addList = fileTarget.getSqlHitList();
                            if(addList == null){
                                addList = sqlHitList;
                                fileTarget.setSqlHitList(addList);
                            }else{
                                addList.addAll(sqlHitList);
                            }
                            //no sql parser
                            fileTarget.addSqlItemNum();
                            logger.info("{} is found, project:{}, file:{},  sql:{}",
                                    fileType, projectName, file.getAbsolutePath(), fileTarget.isTarget(), sql);
                        }
                    }
                }
            }

            //todo mybatis
            /*
            if(document.getDocType().getElementName().equals("sqlMap") &&
                    document.getDocType().getSystemID().equals("http://www.ibatis.com/dtd/sql-map-2.dtd")){
                System.out.println("ibatis mapping file found");
                Element xmlroot = document.getRootElement();
                Iterator it = xmlroot.elementIterator();
                while (it.hasNext()) {
                    Element element = (Element) it.next();
                    if(element.getName().equals("select") || element.getName().equals("insert")
                            || element.getName().equals("update") || element.getName().equals("delete")){
                        System.out.println("sql found:"+element.getStringValue());
                    }
                }
            } */
            // treat as common file;
//            else{
//                fileTarget = commonFileCheckService.check(fileTarget);
//            }
        } catch (Exception e) {
//            logger.error(e.getMessage());
//            fileTarget = commonFileCheckService.check(fileTarget);
        }
        return fileTarget;
    }

    public List<SqlHit> checkSql(String sql){
        List resultList = new ArrayList<>();
        targetSqlRuleMap.forEach((index, rule) ->{
            try{
                String regex = rule.getRegex();
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(sql);
                if(matcher.find()){
                    resultList.add(new SqlHit("targetSqlRuleMap", index, sql));
                }
            }catch(Throwable e){
                logger.error(e.getMessage(), e);
            }
        });
        return resultList;
    }
}
