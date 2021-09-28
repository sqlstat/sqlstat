package com.gk.sqlstat.model;

import java.util.LinkedList;
import java.util.List;

public class ProjectStat {
    public String projectName;
    public int java;
    public int javaSqlNum;
    public int c;
    public int cSqlNum;
    public int xml;
    public int xmlSqlNum;
    public int shell;
    public int shellSqlNum;
    public int sql;
    public int sqlSqlNum;
    public int ctl;
    public int others;
    public int othersSqlNum;
    public int sqlMapSqlCnt; // sqlMap(ibatis) sql统计
    public int mapperSqlCnt; // mapper(mybatis) sql 统计

    public List<FileTarget> fileTargetList = new LinkedList<>();

    @Override
    public String toString() {
        return "projectName='" + projectName + '\'' +
                ", java=" + java +
//                ", javaSqlNum=" + javaSqlNum +
                ", c=" + c +
//                ", cSqlNum=" + cSqlNum +
                ", xml=" + xml +
//                ", xmlSqlNum=" + xmlSqlNum +
                ", shell=" + shell +
//                ", shellSqlNum=" + shellSqlNum +
                ", sql=" + sql +
//                ", sqlSqlNum=" + sqlSqlNum +
                ", ctl=" + ctl +
                ", others=" + others +
//                ", othersSqlNum=" + othersSqlNum +
//                ", fileTargetList=" + fileTargetList +
                ", totalFiles=" + (java + c + xml + shell + sql + ctl + others) +
                ", totalSqls=" + (javaSqlNum + cSqlNum + xmlSqlNum + shellSqlNum + sqlSqlNum + others)
                ;
    }
}


