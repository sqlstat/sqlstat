package com.gk.sqlstat.constant;

public enum XmlType {
    XML_SQLMAP("sqlMap"), XML_MAPPER("mapper"), XML_NORMAL("normal");

    XmlType(String type) {
        this.type = type;
    }
    public String type;



}
