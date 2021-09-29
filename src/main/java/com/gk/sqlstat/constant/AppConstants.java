package com.gk.sqlstat.constant;

import org.apache.commons.compress.utils.Sets;
import org.springframework.stereotype.Component;

import java.util.Set;

public class AppConstants {
    public static Set<String> XML_SQL_TAGS = Sets.newHashSet("insert", "select", "delete", "update", "sql");
}
