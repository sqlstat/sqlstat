package com.fan.sqlstat;

import com.fan.sqlstat.util.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
@Component
public class AppStart {
    private static final Logger logger = LoggerFactory.getLogger(AppStart.class);

    @Value("${app.name}")
    private String name;

    @Resource
    Environment env;

    public static void main(String[] args) throws InterruptedException {
        AppStart appStart = SpringContext.getBean(AppStart.class);
        Driver driver = SpringContext.getBean(Driver.class);
        logger.info("{} initialized... ", appStart.name);
        driver.launch();
        logger.info("{} finished...", appStart.name);
    }


    @Bean(name="commonSqlRuleList")
    public List<String> commonSqlRuleList() {
        List<String> list = new ArrayList<>();
        int num = env.getProperty("app.rule.sql.common.num", Integer.class);
        for(int i = 0; i < num; i++){
            String str =  env.getProperty("app.rule.sql.common["+ i +"]");
            list.add(str);
        }
        return list;
    }

    @Bean(name="ctlRuleList")
    public List<String> ctlRuleList() {
        List<String> list = new ArrayList<>();
        int num = env.getProperty("app.rule.ctl.num", Integer.class);
        for(int i = 0; i < num; i++){
            String str =  env.getProperty("app.rule.ctl["+ i +"]");
            list.add(str);
        }
        return list;
    }
}
