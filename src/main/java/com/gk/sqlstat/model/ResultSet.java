package com.gk.sqlstat.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data
public class ResultSet {
    private Map<String, ProjectStat> resultMap;
    private List<Map<String, ProjectStat>> threadResultList = Collections.synchronizedList(new ArrayList());
    private String baseDirName;

    public Map<String, ProjectStat> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, ProjectStat> resultMap) {
        this.resultMap = resultMap;
    }

    public List<Map<String, ProjectStat>> getThreadResultList() {
        return threadResultList;
    }

    public void setThreadResultList(List<Map<String, ProjectStat>> threadResultList) {
        this.threadResultList = threadResultList;
    }
}
