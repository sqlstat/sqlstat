package com.fan.sqlstat.model;

import com.fan.sqlstat.constant.FileType;

import java.io.File;

public class FileTarget {
    private String project;
    private File file;
    private FileType fileType;

    public FileTarget(String project, File file, FileType fileType){
        this.project = project;
        this.file = file;
        this.fileType = fileType;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
