package com.fan.sqlstat;

import com.fan.sqlstat.constant.FileType;
import com.fan.sqlstat.model.Rule;
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
import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan
@PropertySource(value="classpath:application.properties", encoding="UTF-8")
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
    public Map<Integer, Rule> commonSqlRuleMap() {
        Map<Integer, Rule> ruleMap = RuleUtil.getRuleMap(env, "app.rule.sql.common");
        logger.debug("commonSqlRuleMap:{}", ruleMap);
        return ruleMap;
    }

    @Bean(name="ctlRuleMap")
    public Map<Integer, Rule> ctlRuleMap() {
        Map<Integer, Rule> ruleMap = RuleUtil.getRuleMap(env, "app.rule.ctl");
        logger.debug("ctlRuleMap:{}", ruleMap);
        return ruleMap;
    }

    @Bean(name="fileTypeRuleMap")
    public Map<FileType, Map<Integer, Rule>> fileTypeRuleMap(){
        Map<FileType, Map<Integer, Rule>> ruleMap = new HashMap<>();
        ruleMap.put(FileType.CTL, ctlRuleMap());
        ruleMap.put(FileType.JAVA, commonSqlRuleMap());
        ruleMap.put(FileType.C, commonSqlRuleMap());
        ruleMap.put(FileType.XML, commonSqlRuleMap());
        ruleMap.put(FileType.SHELL, commonSqlRuleMap());
        ruleMap.put(FileType.SQL, commonSqlRuleMap());
        ruleMap.put(FileType.OTHERS, commonSqlRuleMap());
        return ruleMap;
    }
}
