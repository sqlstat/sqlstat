package com.gk.sqlstat.util;

import com.gk.sqlstat.AppStart;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringContext {
    private static final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppStart.class);

    public static <T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }

}
