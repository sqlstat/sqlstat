package com.gk.sqlstat.service.check;

import com.gk.sqlstat.constant.AppConstants;
import com.gk.sqlstat.constant.FileType;
import com.gk.sqlstat.constant.XmlType;
import com.gk.sqlstat.model.FileTarget;
import com.gk.sqlstat.model.Rule;
import com.gk.sqlstat.model.SqlHit;
import com.gk.sqlstat.util.FileUtil;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class XmlCheckService implements CheckService {
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
        String projectName = fileTarget.getProject();
        try {
            Document document = FileUtil.read(fileTarget, reader);
            logger.trace("xml file found:"+document.getName());
            fileTarget.setSqlMapOrMapper(true);
            Element xmlroot = document.getRootElement();
            Iterator it = xmlroot.elementIterator();
            while (it.hasNext()) {
                Element element = (Element) it.next();
                logger.info("element name: {}}", element.getName());
                String sql = "";
                String sqlId = "";
                StringBuilder stringBuilder = new StringBuilder();
                if (document.getDocType() != null
                        && (document.getDocType().getElementName().equals("sqlMap") || document.getDocType().getElementName().equals("mapper"))
                        && AppConstants.XML_SQL_TAGS.contains(element.getName())) {
                    fileTarget.setXmlSqlCnt(fileTarget.getXmlSqlCnt() + 1); // mapper\sqlMap中的一个标签认为是一个sql
                    sqlId = element.attributeValue("id");
                    logger.trace("ibatis/mybatis sql found:" + sql);

                } else {
                    // 除了sqlMap、mapper之外的普通xml处理，将每个标签的属性和内容取出进行匹配
                    List<Attribute> attributes = element.attributes();
                    boolean hasAttribute = attributes != null && attributes.size() > 0;
                    sqlId = String.format("tagName: %s, attributeName: %s, attributeValue: %s", element.getName(), (hasAttribute? element.attribute(0).getName():""), hasAttribute?element.attribute(0).getValue():"");
                    for (Attribute attribute: element.attributes()) {
                        stringBuilder.append(attribute.getName() + "="+ attribute.getValue());
                        stringBuilder.append('\n');
                    }
                }
                for (Node node : element.content()) {
                    stringBuilder.append(node.asXML());
                }
                sql = stringBuilder.toString();
                List<SqlHit> sqlHitList = checkSql(sql, sqlId);
                if (!sqlHitList.isEmpty()) {
                    fileTarget.setTarget(true);
                    List<SqlHit> addList = fileTarget.getSqlHitList();
                    if (addList == null) {
                        addList = sqlHitList;
                        fileTarget.setSqlHitList(addList);
                    } else {
                        addList.addAll(sqlHitList);
                    }
                    fileTarget.setXmlType(XmlType.XML_NORMAL);
                    if (document.getDocType() != null && document.getDocType().getElementName().equals("sqlMap")) {
                        fileTarget.setSqlMapSqlHitCnt(fileTarget.getSqlMapSqlHitCnt() + 1);
                        fileTarget.setXmlType(XmlType.XML_SQLMAP);
                    } else if (document.getDocType() != null && document.getDocType().getElementName().equals("mapper")) {
                        fileTarget.setMapperSqlHitCnt(fileTarget.getMapperSqlHitCnt() + 1);
                        fileTarget.setXmlType(XmlType.XML_MAPPER);
                    }
                    //no sql parser
                    fileTarget.addSqlItemNum();
                    logger.info("{} is found, project:{}, file:{},  sql:{}",
                            fileType, projectName, fileTarget.getFilePath(), fileTarget.isTarget(), sql);
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
            logger.error(e.toString());
//            fileTarget = commonFileCheckService.check(fileTarget);
        }
        return fileTarget;
    }

    public List<SqlHit> checkSql(String sql, String sqlId){
        List resultList = new ArrayList<>();
        targetSqlRuleMap.forEach((index, rule) ->{
            try{
                String regex = rule.getRegex();
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(sql);
                int matchCnt = 0;
                while(matcher.find()){
                    matchCnt ++;
                }
                if (matchCnt > 0) {
                    resultList.add(new SqlHit(  "targetSqlRuleMap", index, sql, sqlId, matchCnt));
                }
            }catch(Throwable e){
                logger.error(e.getMessage(), e);
            }
        });
        return resultList;
    }
}
