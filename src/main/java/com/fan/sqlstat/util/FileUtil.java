package com.fan.sqlstat.util;

import com.fan.sqlstat.constant.FileType;
import com.fan.sqlstat.model.FileTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static File checkBaseDir(String baseDir){
        if(baseDir == null){
            logger.error("baseDir is not set");
            throw new RuntimeException("baseDir is not set");
        }else{
            File file = new File(baseDir);
            if(!file.exists()){
                throw new RuntimeException("baseDir does not exist");
            }else if(!file.isDirectory()){
                logger.error("baseDir is not dir");
                throw new RuntimeException("baseDir is not dir");
            }else{
                logger.info("baseDir file init successfully {}", file.getAbsolutePath());
                return file;
            }
        }
    }

    public static String read(File file) {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader in = new BufferedReader(new FileReader(file))){
            String s;
            while ((s = in.readLine()) != null) {
                sb.append(s +"\n");
            }
        }catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return sb.toString();
    }

//    public static Map<FileType, Integer> statFile(FileTarget fileTarget){
//        Map<FileType, Integer> resultMap = new HashMap<FileType, Integer>();
//        FileType fileType = fileTarget.getFileType();
//        File file = fileTarget.getFile();
//        String projectName = fileTarget.getProject();
//        String text = read(file);
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
//        return resultMap;
//    }


}
