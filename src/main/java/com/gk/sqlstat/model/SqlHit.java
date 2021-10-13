package com.gk.sqlstat.model;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SqlHit {
    private String ruleMapName;
    private int ruleId;
    private String originalSql;
    private String sqlId;

    public SqlHit(String ruleMapName, int ruleId, String originalSql) {
        this.ruleMapName = ruleMapName;
        this.ruleId = ruleId;
        this.originalSql = originalSql;
    }
}
