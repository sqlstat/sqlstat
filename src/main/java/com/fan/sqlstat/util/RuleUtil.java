package com.fan.sqlstat.util;

import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RuleUtil {
    public static Map<Integer, String> getRuleMap(StandardEnvironment env, String keyPrefix){
        Map<Integer, String> ruleMap = new HashMap<>();
        MutablePropertySources mutablePropertySources = ((StandardEnvironment)env).getPropertySources();
        for(org.springframework.core.env.PropertySource propertySource : mutablePropertySources){
            if(propertySource instanceof MapPropertySource){
                Map<String, Object> map = ((MapPropertySource)propertySource).getSource();
                Set<String> keySet = map.keySet();
                for(String key : keySet){
                    if(key.trim().startsWith(keyPrefix+"[")){
                        int index = Integer.valueOf(key.split("\\[")[1].split("\\]")[0]);
                        ruleMap.put(index, (String)map.get(key));
                    }
                }
            }
        }
        return ruleMap;
    }
}
