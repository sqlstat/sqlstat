package com.gk.sqlstat.service;

import com.gk.sqlstat.model.Rule;
import com.gk.sqlstat.model.SqlstatRule;

import java.util.List;

public interface RuleService {

    List<SqlstatRule> getRuleByType(Integer type);

}
