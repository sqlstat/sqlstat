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
public class CommonFileCheckService implements CheckService {
    private static final Logger logger = LoggerFactory.getLogger(CommonFileCheckService.class);

    @Resource(name="commonSqlRuleMap")
    private Map<Integer, Rule> commonSqlRuleMap;

    @Resource(name="targetSqlRuleMap")
    private Map<Integer, Rule> targetSqlRuleMap;

    @Resource(name="ctlRuleMap")
    private Map<Integer, Rule> ctlRuleMap;

    private Map<FileType, Map<Integer, Rule>> fileTypeRuleMap;
    private Map<FileType, String> fileTypeRuleNameMap;

    @PostConstruct
    public void init(){
        fileTypeRuleMap = new HashMap<>();
        fileTypeRuleMap.put(FileType.CTL, targetSqlRuleMap);
        fileTypeRuleMap.put(FileType.JAVA, targetSqlRuleMap);
        fileTypeRuleMap.put(FileType.C, targetSqlRuleMap);
        fileTypeRuleMap.put(FileType.XML, targetSqlRuleMap);
        fileTypeRuleMap.put(FileType.SHELL, targetSqlRuleMap);
        fileTypeRuleMap.put(FileType.SQL, targetSqlRuleMap);
        fileTypeRuleMap.put(FileType.OTHERS, targetSqlRuleMap);

        fileTypeRuleNameMap = new HashMap<>();
        fileTypeRuleNameMap.put(FileType.CTL, "targetSqlRuleMap");
        fileTypeRuleNameMap.put(FileType.JAVA, "targetSqlRuleMap");
        fileTypeRuleNameMap.put(FileType.C, "targetSqlRuleMap");
        fileTypeRuleNameMap.put(FileType.XML, "targetSqlRuleMap");
        fileTypeRuleNameMap.put(FileType.SHELL, "targetSqlRuleMap");
        fileTypeRuleNameMap.put(FileType.SQL, "targetSqlRuleMap");
        fileTypeRuleNameMap.put(FileType.OTHERS, "targetSqlRuleMap");
    }

    @Override
    public FileTarget check(FileTarget fileTarget) {
        try {
            FileType fileType = fileTarget.getFileType();
            File file = fileTarget.getFile();
            String projectName = fileTarget.getProject();
            String text = FileUtil.read(fileTarget);
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
        } catch (Exception e) {
            logger.error(e.getMessage());
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
                int matchCnt = 0;
                while(matcher.find()){
                    matchCnt ++;
                }
                if (matchCnt > 0) {
                    String originalSql;
                    if(isSql){
                        originalSql = text;
                    }else{
                        originalSql = null;
                    }
                    resultList.add(new SqlHit(ruleName, index, originalSql, matchCnt));
                }
            }catch(Throwable e){
                logger.error(e.getMessage(), e);
            }
        });
        return resultList;
    }


}
