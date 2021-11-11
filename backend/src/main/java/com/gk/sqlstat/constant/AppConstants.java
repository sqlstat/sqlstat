package com.gk.sqlstat.constant;

import org.apache.commons.compress.utils.Sets;

import java.util.Set;

public class AppConstants {
    public static Set<String> XML_SQL_TAGS = Sets.newHashSet("insert", "select", "delete", "update", "sql", "procedure", "resultMap", "parameterMap");

    // rule type
    public final static Integer RULE_TYPE_COMMON = 1;

}
