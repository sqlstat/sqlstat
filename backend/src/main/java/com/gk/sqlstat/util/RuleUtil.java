package com.gk.sqlstat.util;

import com.gk.sqlstat.model.Rule;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RuleUtil {
    public static Map<Integer, Rule> getRuleMap(StandardEnvironment env, String keyPrefix){
        Map<Integer, Rule> ruleMap = new HashMap<>();
//        MutablePropertySources mutablePropertySources = ((StandardEnvironment)env).getPropertySources();
//        for(org.springframework.core.env.PropertySource propertySource : mutablePropertySources){
//            if(propertySource instanceof MapPropertySource){
//                Map<String, Object> map = ((MapPropertySource)propertySource).getSource();
//                Set<String> keySet = map.keySet();
//                for(String key : keySet){
//                    if(key.trim().startsWith(keyPrefix+"[")){
//                        int index = Integer.valueOf(key.split("\\[")[1].split("\\]")[0]);
//                        Rule rule = ruleMap.get(index);
//                        if(rule == null){
//                            rule = new Rule();
//                            rule.setId(index);
//                            ruleMap.put(index, rule);
//                        }
//                        if(key.endsWith("regex")){
//                            rule.setRegex(((String)map.get(key)).trim());
//                        }else{
//                            rule.setSuggrestion((String)map.get(key));
//                        }
//                    }
//                }
//            }
//        }
        return ruleMap;
    }
}
