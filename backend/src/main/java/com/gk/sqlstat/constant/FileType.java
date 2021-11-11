package com.gk.sqlstat.constant;

import java.util.HashMap;
import java.util.Map;

public enum FileType {
    JAVA("java"),C("c"), XML("xml"), SHELL("shell"), SQL("sql"), CTL("ctl"), OTHERS("other"), TASKEND("taskend"), JAR("jar");



    public String type;

    FileType(String type){
        this.type = type;
    }

    private static final Map<String, FileType> extentionMap = new HashMap<String, FileType>(){
        {
            put(".java", JAVA);
            put(".c", C);
            put(".xml", XML);
            put(".sh", SHELL);
            put(".sql", SQL);
            put(".qry", SQL);
            put(".ctl", CTL);
            put(".jar", JAR);
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
