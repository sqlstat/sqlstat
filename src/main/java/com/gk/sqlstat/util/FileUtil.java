package com.gk.sqlstat.util;

import com.gk.sqlstat.constant.FileType;
import com.gk.sqlstat.model.FileTarget;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;
import java.nio.channels.FileChannel;

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

    public static Document read(FileTarget fileTarget, SAXReader reader) throws Exception{
        if (fileTarget.isJarFile()) {
            return reader.read(fileTarget.getJarFile().getInputStream(fileTarget.getJarEntry()));
        } else {
            return reader.read(fileTarget.getFile());
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

    public static String read(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))){
            String s;
            while ((s = in.readLine()) != null) {
                sb.append(s +"\n");
            }
        }catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return sb.toString();
    }

    public static String read(FileTarget fileTarget) throws Exception{
        if (fileTarget.isJarFile()) {
            return read(fileTarget.getJarFile().getInputStream(fileTarget.getJarEntry()));
        } else {
            return read(fileTarget.getFile());
        }
    }

    public static void copy(File source, File target)  {
        File parent = target.getParentFile();
        if(!parent.exists()){
            parent.mkdirs();
        }
        try(FileChannel in = new FileInputStream(source).getChannel();
            FileChannel out = new FileOutputStream(target).getChannel();) {
            out.transferFrom(in, 0, in.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
