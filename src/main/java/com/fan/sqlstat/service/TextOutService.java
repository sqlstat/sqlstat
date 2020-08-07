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
public class TextOutService extends AbstractOutService implements OutService {
    private static final Logger logger = LoggerFactory.getLogger(TextOutService.class);

    @Resource(name="combinedRuleMap")
    Map<String, Map<Integer, Rule>> combinedRuleMap;

    @Value("${app.projectStat.showDetails:false}")
    private boolean showDetails;

    @Override
    public void outputAction(ResultSet resultSet) {
        Map<String, ProjectStat> resultMap = resultSet.getResultMap();
        logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++");
        resultMap.forEach((key, projectStat) -> {
            logger.info("{}", projectStat.toString());
            if(showDetails){
                projectStat.fileTargetList.forEach(fileTarget -> {
                    logger.info("{}", "file:"+fileTarget.getFile().getAbsolutePath());
                    fileTarget.getSqlHitList().forEach(sqlHit -> {
                        StringBuilder stringBuilder = new StringBuilder();
                        String ruleMapName = sqlHit.getRuleMapName();
                        int ruleId = sqlHit.getRuleId();
                        Map<Integer,Rule> ruleMap = combinedRuleMap.get(ruleMapName);
                        Rule rule = ruleMap.get(ruleId);
                        stringBuilder.append(" [ruleId:").append(ruleId)
                                .append(" Regex:").append("\"").append(rule.getRegex()).append("\"")
                                .append(" Suggestion:").append(rule.getSuggrestion())
                                .append(" originalSql:").append(sqlHit.getOriginalSql() == null ? null : sqlHit.getOriginalSql().trim())
                                .append("]");
                        logger.info("{}", stringBuilder.toString());
                    });
                });
            }
        });
        logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++");
    }
}
