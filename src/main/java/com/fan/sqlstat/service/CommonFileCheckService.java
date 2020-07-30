package com.fan.sqlstat.service;

import com.fan.sqlstat.constant.FileType;
import com.fan.sqlstat.model.FileTarget;
import com.fan.sqlstat.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class CommonFileCheckService implements ChechService {

    private static final Logger logger = LoggerFactory.getLogger(CommonFileCheckService.class);

    @Override
    public FileTarget check(FileTarget fileTarget) {
//        FileType fileType = fileTarget.getFileType();
//        File file = fileTarget.getFile();
//        String projectName = fileTarget.getProject();
//        String text = FileUtil.read(file);
//        logger.trace("filename:{}, text:\n{}", file.getAbsolutePath(), text);
//        if(fileType.equals(FileType.SHELL)){
//            resultMap.put(FileType.SHELL, 1);
//            int result = hasSql(text);
//            resultMap.put(FileType.SHELLWITHSQL, result);
//            logger.info("shell is found, project:{}, file:{}, contain sql {}", projectName, file.getAbsolutePath(), result==1? true: false);
//        }else if(fileType.equals(FileType.SQL)){
//            int result = hasSql(text);
//            resultMap.put(FileType.SQL, result);
//            if(result == 1){
//                logger.info("SQL file is found, project:{}, file:{}", projectName, file.getAbsolutePath());
//            }
//        }else if(fileType.equals(FileType.CTL)){
//            int result = isCtl(text);
//            resultMap.put(FileType.CTL, result);
//            if(result == 1){
//                logger.info("ctl file is found, project:{}, file:{}", projectName, file.getAbsolutePath());
//            }
//        }else if(fileType.equals(FileType.OTHERS)){
//            int result = hasSql(text);
//            resultMap.put(FileType.OTHERS, result);
//            if(result == 1){
//                logger.info("other file is found, project:{}, file:{}", projectName, file.getAbsolutePath());
//            }
//        }
//
        return fileTarget;
    }



}
