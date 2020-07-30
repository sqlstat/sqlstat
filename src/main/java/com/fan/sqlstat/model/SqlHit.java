package com.fan.sqlstat.model;

public class SqlHit {

    private int ruleId;
    private String originalSql;

    public SqlHit(int ruleId, String originalSql) {
        this.ruleId = ruleId;
        this.originalSql = originalSql;
    }

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public String getOriginalSql() {
        return originalSql;
    }

    public void setOriginalSql(String originalSql) {
        this.originalSql = originalSql;
    }
}
