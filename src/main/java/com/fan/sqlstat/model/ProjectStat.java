package com.fan.sqlstat.model;

public class ProjectStat {

    public String projectName;
    public int shell;
    public int shellWithSql;
    public int sql;
    public int ctl;
    public int others;
    public int sqlItem;

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
