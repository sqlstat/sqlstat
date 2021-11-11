package com.gk.sqlstat.service.impl;

import com.gk.sqlstat.dao.SqlstatRuleMapper;
import com.gk.sqlstat.model.Rule;
import com.gk.sqlstat.model.SqlstatRule;
import com.gk.sqlstat.service.RuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service()
public class RuleServiceImpl implements RuleService {

    @Resource
    private SqlstatRuleMapper sqlstatRuleMapper;

    @Override
    public List<SqlstatRule> getRuleByType(Integer type) {
        List<SqlstatRule> ruleByType = sqlstatRuleMapper.getRuleByType(type);
        return ruleByType;
    }
}
