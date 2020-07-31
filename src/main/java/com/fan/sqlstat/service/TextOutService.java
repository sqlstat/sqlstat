package com.fan.sqlstat.service;

import com.fan.sqlstat.constant.FileType;
import com.fan.sqlstat.model.ProjectStat;
import com.fan.sqlstat.model.ResultSet;
import com.fan.sqlstat.model.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class TextOutService implements OutService {
    private static final Logger logger = LoggerFactory.getLogger(TextOutService.class);

    @Resource(name="fileTypeRuleMap")
    private Map<FileType, Map<Integer, Rule>> fileTypeRuleMap;

    @Value("${app.projectStat.showDetails:false}")
    private boolean showDetails;

    @Override
    public void printResult(ResultSet resultSet) {
        Map<String, ProjectStat> resultMap = resultSet.getResultMap();
        logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++");
        resultMap.forEach((key, projectStat) -> {
            logger.info("{}", projectStat.toString());
            if(showDetails){
                projectStat.fileTargetList.forEach(fileTarget -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("file:")
                            .append(fileTarget.getFile().getAbsolutePath());
                    fileTarget.getSqlHitList().forEach(sqlHit -> {
                        int ruleId = sqlHit.getRuleId();
                        Rule rule = fileTypeRuleMap.get(fileTarget.getFileType()).get(ruleId);
                        stringBuilder.append(" [ruleId:").append(ruleId)
                                .append(" Regex:").append("\"").append(rule.getRegex()).append("\"")
                                .append(" Suggestion:").append(rule.getSuggrestion())
                                .append(" originalSql:").append(sqlHit.getOriginalSql())
                                .append("]");
                    });


                    logger.info("\t{}", stringBuilder.toString());
                });
            }
        });
        logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++");
    }
}
