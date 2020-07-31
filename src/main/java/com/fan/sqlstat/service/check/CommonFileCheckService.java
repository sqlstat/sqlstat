package com.fan.sqlstat.service.check;

import com.fan.sqlstat.constant.FileType;
import com.fan.sqlstat.model.FileTarget;
import com.fan.sqlstat.model.SqlHit;
import com.fan.sqlstat.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CommonFileCheckService implements ChechService {

    private static final Logger logger = LoggerFactory.getLogger(CommonFileCheckService.class);

    @Resource(name="ctlRuleMap")
    private Map<Integer, String> ctlRuleMap;

    @Resource(name="commonSqlRuleMap")
    private Map<Integer, String> commonSqlRuleMap;

    @Override
    public FileTarget check(FileTarget fileTarget) {
        FileType fileType = fileTarget.getFileType();
        File file = fileTarget.getFile();
        String projectName = fileTarget.getProject();
        String text = FileUtil.read(file);
        logger.trace("filename:{}, text:\n{}", file.getAbsolutePath(), text);
        if(fileType.equals(FileType.JAVA) || fileType.equals(FileType.C) || fileType.equals(FileType.XML) ||
                fileType.equals(FileType.SHELL) || fileType.equals(FileType.SQL) || fileType.equals(FileType.OTHERS)){
            List<SqlHit> sqlHitList = hasSql(text);
            if(!sqlHitList.isEmpty()){
                fileTarget.setTarget(true);
                fileTarget.setSqlHitList(sqlHitList);
                //no sql parser
                fileTarget.setSqlItemNum(0);
                logger.info("{} is found, project:{}, file:{}, contain sql {}",
                        fileTarget.getFileType(), projectName, file.getAbsolutePath(), fileTarget.isTarget());
            }

        }else if(fileType.equals(FileType.CTL)){
            List<SqlHit> sqlHitList = isCtl(text);
            if(!sqlHitList.isEmpty()){
                fileTarget.setTarget(true);
                fileTarget.setSqlHitList(sqlHitList);
                logger.info("ctl file is found, project:{}, file:{}", projectName, file.getAbsolutePath());
            }
        }

        return fileTarget;
    }

    private List<SqlHit> hasSql(String text){
        List resultList = new ArrayList<>();
        commonSqlRuleMap.forEach((index, regex) ->{
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);
            if(matcher.find()){
                resultList.add(new SqlHit(index, null));
            }
        });
        return resultList;
    }

    private List<SqlHit> isCtl(String text){
        List resultList = new ArrayList<>();
        ctlRuleMap.forEach((index, regex) ->{
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);
            if(matcher.find()){
                resultList.add(new SqlHit(index, null));
            }
        });
        return resultList;
    }


}
