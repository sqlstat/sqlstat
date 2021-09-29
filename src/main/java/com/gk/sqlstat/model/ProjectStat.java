package com.gk.sqlstat.model;

import java.util.LinkedList;
import java.util.List;

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
    public int sqlMapSqlHitNum; // sqlMap(ibatis) sql统计
    public int mapperSqlHitNum; // mapper(mybatis) sql 统计
    public int xmlSqlSum; // xml中sql总数

    public List<FileTarget> fileTargetList = new LinkedList<>();

    @Override
    public String toString() {
        return "ProjectStat{" +
                "projectName='" + projectName + '\'' +
                ", java=" + java +
                ", javaSqlNum=" + javaSqlHitNum +
                ", c=" + c +
                ", cSqlNum=" + cSqlNum +
                ", xml=" + xml +
                ", xmlSqlNum=" + xmlSqlHitNum +
                ", shell=" + shell +
                ", shellSqlNum=" + shellSqlHitNum +
                ", sql=" + sql +
                ", sqlSqlNum=" + sqlSqlNum +
                ", ctl=" + ctl +
                ", others=" + others +
                ", othersSqlNum=" + othersSqlNum +
                ", sqlMapSqlCnt=" + sqlMapSqlHitNum +
                ", mapperSqlCnt=" + mapperSqlHitNum +
                ", xmlSqlSumCnt=" + xmlSqlSum +
                '}';
    }


}


