package com.gk.sqlstat.model;

import com.gk.sqlstat.constant.FileType;

import java.io.File;
import java.util.List;

public class FileTarget {
    private String project;
    private File file;
    private FileType fileType;

    //hit by rules
    private boolean target;

    //sqlHits ；
    private List<SqlHit> sqlHitList;

    //sql hit num;
    private int sqlItemNum;

    // xml类型文件相关统计
    private int sqlMapSqlHitCnt; // sqlMap(ibatis) sql hit cnt
    private int mapperSqlHitCnt; // mapper(mybatis) sql hit cnt
    private int xmlSqlCnt; // xml 中sql 总数

    public FileTarget(String project, File file, FileType fileType){
        this.project = project;
        this.file = file;
        this.fileType = fileType;
    }

    public int getSqlMapSqlHitCnt() {
        return sqlMapSqlHitCnt;
    }

    public void setSqlMapSqlHitCnt(int sqlMapSqlHitCnt) {
        this.sqlMapSqlHitCnt = sqlMapSqlHitCnt;
    }

    public int getMapperSqlHitCnt() {
        return mapperSqlHitCnt;
    }

    public void setMapperSqlHitCnt(int mapperSqlHitCnt) {
        this.mapperSqlHitCnt = mapperSqlHitCnt;
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

    public boolean isTarget() {
        return target;
    }

    public void setTarget(boolean target) {
        this.target = target;
    }

    public List<SqlHit> getSqlHitList() {
        return sqlHitList;
    }

    public void setSqlHitList(List<SqlHit> sqlHitList) {
        this.sqlHitList = sqlHitList;
    }

    public int getSqlItemNum() {
        return sqlItemNum;
    }

    public void setSqlItemNum(int sqlItemNum) {
        this.sqlItemNum = sqlItemNum;
    }

    public void addSqlItemNum(){
        this.sqlItemNum++;
    }

    public int getXmlSqlCnt() {
        return xmlSqlCnt;
    }

    public void setXmlSqlCnt(int xmlSqlCnt) {
        this.xmlSqlCnt = xmlSqlCnt;
    }
}
