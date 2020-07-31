package com.fan.sqlstat;

import com.fan.sqlstat.util.RuleUtil;
import com.fan.sqlstat.util.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
@Component
public class AppStart {
    private static final Logger logger = LoggerFactory.getLogger(AppStart.class);

    @Value("${app.name}")
    private String name;

    @Resource
    StandardEnvironment env;

    public static void main(String[] args) throws InterruptedException {
        AppStart appStart = SpringContext.getBean(AppStart.class);
        Driver driver = SpringContext.getBean(Driver.class);
        logger.info("{} initialized... ", appStart.name);
        driver.launch();
        logger.info("{} finished...", appStart.name);
    }


    @Bean(name="commonSqlRuleMap")
    public Map<Integer, String> commonSqlRuleList() {
        Map<Integer, String> ruleMap = RuleUtil.getRuleMap(env, "app.rule.sql.common");
        logger.debug("commonSqlRuleMap:{}", ruleMap);
        return ruleMap;
    }

    @Bean(name="ctlRuleMap")
    public Map<Integer, String> ctlRuleList() {
        Map<Integer, String> ruleMap = RuleUtil.getRuleMap(env, "app.rule.ctl");
        logger.debug("ctlRuleMap:{}", ruleMap);
        return ruleMap;
    }
}
