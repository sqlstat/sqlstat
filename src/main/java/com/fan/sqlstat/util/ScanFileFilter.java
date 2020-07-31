package com.fan.sqlstat.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

@Component
public class ScanFileFilter implements FileFilter {

    @Value("${app.source.dirs}")
    private String sourceDirs;

    @Value("${app.source.file.extentions}")
    private String sourceFileExtentions;

    public List<String> getFileExtentions(){
        String[] strArr = sourceFileExtentions.trim().split(";");
        return Arrays.asList(strArr);
    }

    public List<String> getSrcDir(){
        String[] strArr = sourceDirs.trim().split(";");
        return Arrays.asList(strArr);
    }

    @Override
    public boolean accept(File pathname) {

        if(pathname.isDirectory()){
            return true;
        }
        String filename = pathname.getName();
        for(String ext : getFileExtentions()){
            if(filename.endsWith(ext) || filename.endsWith(ext.toUpperCase())){
                return true;
            }
        }
        return false;
    }
}
