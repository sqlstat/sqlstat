package com.fan.sqlstat.model;

import java.util.List;

public class ProjectStat {

    public String projectName;
    public int shell;
    public int shellWithSql;
    public int sql;
    public int ctl;
    public int others;
    public int sqlItems;

    public List<FileTarget> fileTargetList;

    @Override
    public String toString() {
        return  "projectName='" + projectName + '\'' +
                ", shell=" + shell +
                ", shellWithSql=" + shellWithSql +
                ", sql=" + sql +
                ", ctl=" + ctl +
                ", others=" + others +
                ", totalShell=" + (shell + sql + ctl + others) +
                ", totalShellWithSql=" + (shellWithSql + sql + ctl + others)
                ;
    }
}
