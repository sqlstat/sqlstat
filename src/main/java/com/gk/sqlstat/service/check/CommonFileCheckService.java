package com.gk.sqlstat.service.check;

import com.gk.sqlstat.constant.FileType;
import com.gk.sqlstat.model.FileTarget;
import com.gk.sqlstat.model.Rule;
import com.gk.sqlstat.model.SqlHit;
import com.gk.sqlstat.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CommonFileCheckService implements ChechService {
    private static final Logger logger = LoggerFactory.getLogger(CommonFileCheckService.class);

    @Resource(name="commonSqlRuleMap")
    private Map<Integer, Rule> commonSqlRuleMap;

    @Resource(name="ctlRuleMap")
    private Map<Integer, Rule> ctlRuleMap;

    private Map<FileType, Map<Integer, Rule>> fileTypeRuleMap;
    private Map<FileType, String> fileTypeRuleNameMap;

    @PostConstruct
    public void init(){
        fileTypeRuleMap = new HashMap<>();
        fileTypeRuleMap.put(FileType.CTL, ctlRuleMap);
        fileTypeRuleMap.put(FileType.JAVA, commonSqlRuleMap);
        fileTypeRuleMap.put(FileType.C, commonSqlRuleMap);
        fileTypeRuleMap.put(FileType.XML, commonSqlRuleMap);
        fileTypeRuleMap.put(FileType.SHELL, commonSqlRuleMap);
        fileTypeRuleMap.put(FileType.SQL, commonSqlRuleMap);
        fileTypeRuleMap.put(FileType.OTHERS, commonSqlRuleMap);

        fileTypeRuleNameMap = new HashMap<>();
        fileTypeRuleNameMap.put(FileType.CTL, "ctlRuleMap");
        fileTypeRuleNameMap.put(FileType.JAVA, "commonSqlRuleMap");
        fileTypeRuleNameMap.put(FileType.C, "commonSqlRuleMap");
        fileTypeRuleNameMap.put(FileType.XML, "commonSqlRuleMap");
        fileTypeRuleNameMap.put(FileType.SHELL, "commonSqlRuleMap");
        fileTypeRuleNameMap.put(FileType.SQL, "commonSqlRuleMap");
        fileTypeRuleNameMap.put(FileType.OTHERS, "commonSqlRuleMap");
    }

    @Override
    public FileTarget check(FileTarget fileTarget) {
        FileType fileType = fileTarget.getFileType();
        File file = fileTarget.getFile();
        String projectName = fileTarget.getProject();
        String text = FileUtil.read(file);
        logger.trace("filename:{}, text:\n{}", file.getAbsolutePath(), text);
        if(fileType.equals(FileType.JAVA) || fileType.equals(FileType.C) || fileType.equals(FileType.XML) ||
                fileType.equals(FileType.SHELL) || fileType.equals(FileType.SQL) || fileType.equals(FileType.OTHERS)){
            List<SqlHit> sqlHitList = checkText(text, fileType, false);
            if(!sqlHitList.isEmpty()){
                fileTarget.setTarget(true);
                fileTarget.setSqlHitList(sqlHitList);
                //no sql parser
                fileTarget.setSqlItemNum(0);
                logger.info("{} is found, project:{}, file:{}, contain sql {}",
                        fileType, projectName, file.getAbsolutePath(), fileTarget.isTarget());
            }

        }else if(fileType.equals(FileType.CTL)){
            List<SqlHit> sqlHitList = checkText(text,fileType, false);
            if(!sqlHitList.isEmpty()){
                fileTarget.setTarget(true);
                fileTarget.setSqlHitList(sqlHitList);
                logger.info("ctl file is found, project:{}, file:{}", projectName, file.getAbsolutePath());
            }
        }

        return fileTarget;
    }

    public List<SqlHit> checkText(String text, FileType fileType, boolean isSql){
        List resultList = new ArrayList<>();
        Map<Integer, Rule> ruleMap = fileTypeRuleMap.get(fileType);
        String ruleName = fileTypeRuleNameMap.get(fileType);
        ruleMap.forEach((index, rule) ->{
            try{
                String regex = rule.getRegex();
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(text);
                if(matcher.find()){
                    String originalSql;
                    if(isSql){
                        originalSql = text;
                    }else{
                        originalSql = null;
                    }
                    resultList.add(new SqlHit(ruleName, index, originalSql));
                }
            }catch(Throwable e){
                logger.error(e.getMessage(), e);
            }
        });
        return resultList;
    }


}
