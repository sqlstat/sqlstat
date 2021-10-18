package com.gk.sqlstat.model;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class SqlHit {
    private String ruleMapName;
    private int ruleId;
    private String originalSql;
    private String sqlId;
    private int matchCnt;

    public SqlHit(String ruleMapName, int ruleId, String originalSql) {
        this.ruleMapName = ruleMapName;
        this.ruleId = ruleId;
        this.originalSql = originalSql;
    }

    public SqlHit(String ruleMapName, int ruleId, String originalSql, int matchCnt) {
        this.ruleMapName = ruleMapName;
        this.ruleId = ruleId;
        this.originalSql = originalSql;
        this.matchCnt = matchCnt;
    }
}
