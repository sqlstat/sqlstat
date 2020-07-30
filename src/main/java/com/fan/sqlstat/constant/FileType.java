package com.fan.sqlstat.constant;

import java.util.HashMap;
import java.util.Map;

public enum FileType {
    JAVA,C, XML, SHELL, SQL, CTL, OTHERS, TASKEND;

    private static final Map<String, FileType> extentionMap = new HashMap<String, FileType>(){
        {
            put(".java", JAVA);
            put(".c", C);
            put(".xml", XML);
            put(".sh", SHELL);
            put(".sql", SQL);
            put(".qry", SQL);
            put(".ctl", CTL);
        }
    };

    public static FileType getFileTypeByExt(String ext){
        FileType fileType =  extentionMap.get(ext);
        if(fileType == null){
            return FileType.OTHERS;
        }else{
            return fileType;
        }
    }

}
