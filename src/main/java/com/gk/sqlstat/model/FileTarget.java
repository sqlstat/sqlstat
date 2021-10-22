package com.gk.sqlstat.model;

import com.gk.sqlstat.constant.FileType;
import com.gk.sqlstat.constant.XmlType;
import lombok.Data;

import java.io.File;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Data
public class FileTarget {
    private String project;
    private File file;
    private FileType fileType;
    private String filePath;

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
    private int xmlFileCnt; // xml 文件总数统计
    private boolean isSqlMapOrMapper; // 是否时sqlMap或者mapper类型的xml文件
    private XmlType xmlType;

    // java类型文件统计
    private int javaFileCnt; // java文件总数统计

    // shell类型文件统计
    private int shellFileCnt; // shell文件总数统计

    // sql类型文件统计
    private int sqlFileCnt; // sql文件总数统计

    // jar文件相关
    private JarEntry jarEntry;
    private boolean isJarFile;
    private JarFile jarFile;



    public FileTarget(String project, File file, FileType fileType, String filePath){
        this.project = project;
        this.file = file;
        this.fileType = fileType;
        this.filePath = filePath;
    }

    public FileTarget(String project, File file, FileType fileType, String filePath, JarEntry jarEntry, JarFile jar){
        this.project = project;
        this.file = file;
        this.fileType = fileType;
        this.filePath = filePath;
        this.jarEntry = jarEntry;
        this.isJarFile = true;
        this.jarFile = jar;
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
