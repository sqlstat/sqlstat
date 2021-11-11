package com.gk.sqlstat.model;

import lombok.Data;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

@Data
@ToString(exclude = {"fileTargetList"})
public class ProjectStat {
    public String projectName;
    public int java;
    public int javaSqlHitNum;
    public int c;
    public int cSqlNum;
    public int xml;
    public int xmlSqlHitNum;
    public int shell;
    public int shellSqlHitNum;
    public int sql;
    public int sqlSqlNum;
    public int ctl;
    public int others;
    public int othersSqlNum;
    public int sqlMapSqlHitNum; // sqlMap(ibatis) sql 命中数统计
    public int mapperSqlHitNum; // mapper(mybatis) sql 命中数统计
    public int xmlSqlSum; // xml中sql总数
    public int javaFileTotalCnt; // java文件数统计
    public int sqlFileTotalCnt; // sql文件数统计
    public int xmlFileTotalCnt; //xml文件数统计
    public int shellFileTotalCnt; // shell文件数统计


    public List<FileTarget> fileTargetList = new LinkedList<>();


}


