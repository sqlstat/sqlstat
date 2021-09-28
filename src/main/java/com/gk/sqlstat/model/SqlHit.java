package com.gk.sqlstat.model;

public class SqlHit {
    private String ruleMapName;
    private int ruleId;
    private String originalSql;

    public SqlHit(String ruleMapName, int ruleId, String originalSql) {
        this.ruleMapName = ruleMapName;
        this.ruleId = ruleId;
        this.originalSql = originalSql;
    }

    public String getRuleMapName() {
        return ruleMapName;
    }

    public void setRuleMapName(String ruleMapName) {
        this.ruleMapName = ruleMapName;
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
