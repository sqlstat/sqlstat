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
}
