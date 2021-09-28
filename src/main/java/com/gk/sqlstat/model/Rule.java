package com.gk.sqlstat.model;

public class Rule {
    private int id;
    private String regex;
    private String suggrestion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getSuggrestion() {
        return suggrestion;
    }

    public void setSuggrestion(String suggrestion) {
        this.suggrestion = suggrestion;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "id=" + id +
                ", regex='" + regex + '\'' +
                ", suggrestion='" + suggrestion + '\'' +
                '}';
    }
}
