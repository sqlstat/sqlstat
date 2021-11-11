package com.gk.sqlstat.controller;

import com.gk.sqlstat.common.IResult;
import com.gk.sqlstat.common.Result;
import com.gk.sqlstat.service.RuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sqlstat")
public class SqlstatController {

    private final Logger logger = LoggerFactory.getLogger(SqlstatController.class);

    @Resource
    private RuleService ruleService;

    @Resource
    private Driver driver;



    @PostMapping("/getRuleByType")
    public IResult getRuleByType(@RequestParam Integer type) {
        return Result.dataSuccess(ruleService.getRuleByType(type));
    }

    @PostMapping("/scanByCommonRules")
    public IResult scanByCommonRules() {
        driver.launch();
        return Result.success();
    }

}
