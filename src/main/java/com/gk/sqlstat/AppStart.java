package com.gk.sqlstat;

import com.gk.sqlstat.model.Rule;
import com.gk.sqlstat.service.TextOutService;
import com.gk.sqlstat.util.RuleUtil;
import com.gk.sqlstat.util.SpringContext;
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
@PropertySource(value="${applicationPath:classpath:application.properties}", encoding="UTF-8")
@Component
public class AppStart {
    private static final Logger logger = LoggerFactory.getLogger(AppStart.class);

    @Value("${app.name}")
    private String name;

    @Resource
    StandardEnvironment env;

    public static void main(String[] args) throws InterruptedException {
        // 从参数读取配置
        Driver driver = SpringContext.getBean(Driver.class);
        if (args != null && args.length > 0) {
            logger.info("自定义配置：" + args);
            if (args.length == 1) {
                logger.info("指定配置文件：" + args[0]);
                System.setProperty("applicationPath", args[0]);
            } else if (args.length == 2) {
                logger.info(String.format("指定扫描路径及输出路径：%s，%s",args[0], args[1]));
                driver.setBaseDir(args[0]);
                TextOutService outService = (TextOutService)driver.getOutService();
                outService.setResultDir(args[1]);
            } else if (args.length == 3) {
                logger.info(String.format("指定配置文件路径、扫描路径及输出路径：%s，%s，%s",args[0], args[1], args[2]));
                System.setProperty("applicationPath", args[0]);
                driver.setBaseDir(args[1]);
                TextOutService outService = (TextOutService)driver.getOutService();
                outService.setResultDir(args[2]);
            } else {
                logger.error("参数数量不支持， " + args);
                return;
            }
        }

        AppStart appStart = SpringContext.getBean(AppStart.class);
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

    @Bean(name="targetSqlRuleMap")
    public Map<Integer, Rule> targetSqlRuleMap() {
        Map<Integer, Rule> ruleMap = RuleUtil.getRuleMap(env, "app.rule.sql.target");
        logger.debug("targetSqlRuleMap:{}", ruleMap);
        return ruleMap;
    }

    @Bean(name="combinedRuleMap")
    public Map<String, Map<Integer, Rule>> combinedRuleMap(){
        Map<String, Map<Integer, Rule>> combinedRuleMap = new HashMap<>();
        combinedRuleMap.put("commonSqlRuleMap", commonSqlRuleMap());
        combinedRuleMap.put("ctlRuleMap", ctlRuleMap());
        combinedRuleMap.put("targetSqlRuleMap", targetSqlRuleMap());
        return combinedRuleMap;
    }
}
