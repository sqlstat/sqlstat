package com.fan.sqlstat.service;

import com.fan.sqlstat.model.ProjectStat;
import com.fan.sqlstat.model.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TextOutService implements OutService {
    private static final Logger logger = LoggerFactory.getLogger(TextOutService.class);

    @Override
    public void printResult(ResultSet resultSet) {
        Map<String, ProjectStat> resultMap = resultSet.getResultMap();
        logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++");
        resultMap.forEach((key, projectStat) -> logger.info("{}", projectStat.toString()));
        logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++");
    }
}
