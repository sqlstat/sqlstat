package com.gk.sqlstat.model;

import java.io.Serializable;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class SqlstatRule implements Serializable {
    /**
     * 自增id
     */
    private Integer id;

    /**
     * 规则类型 1：通用
     */
    private Byte ruleType;

    /**
     * 规则对应的正则表达
     */
    private String ruleRegex;

    /**
     * 修改建议
     */
    private String suggestion;

    private static final long serialVersionUID = 1L;
}