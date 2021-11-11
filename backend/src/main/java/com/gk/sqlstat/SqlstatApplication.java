package com.gk.sqlstat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value="${applicationPath:classpath:application.properties}", encoding="UTF-8")
public class SqlstatApplication {

    public static void main(String[] args) {
        SpringApplication.run(SqlstatApplication.class, args);
    }

}
