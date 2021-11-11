package com.gk.sqlstat.common;

import com.gk.sqlstat.constant.AppConstants;
import com.gk.sqlstat.model.SqlstatRule;
import com.gk.sqlstat.service.RuleService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Component
public class RuleMap {

    @Resource
    private RuleService ruleService;

    public List<SqlstatRule> COMMON_RULES;

    @PostConstruct
    public void init() {
        COMMON_RULES = ruleService.getRuleByType(AppConstants.RULE_TYPE_COMMON);
    }

}
