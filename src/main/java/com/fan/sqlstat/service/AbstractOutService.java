package com.fan.sqlstat.service;

import com.fan.sqlstat.model.ProjectStat;
import com.fan.sqlstat.model.ResultSet;
import com.fan.sqlstat.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public abstract class AbstractOutService implements OutService{
    private static final Logger logger = LoggerFactory.getLogger(AbstractOutService.class);

    @Value("${app.result.dir}")
    private String resultDir;

    @Override
    public void output(ResultSet resultSet) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = dateFormat.format(new Date());
        String dateDir;
        if(!resultDir.endsWith(File.separator)){
            dateDir = resultDir+File.separator+dateStr+File.separator;
        }else{
            dateDir = resultDir+dateStr+File.separator;
        }
        logger.info("result output base dir:{}", dateDir);
        File dateDirFile = new File(dateDir);
        dateDirFile.mkdirs();

        Map<String, ProjectStat> resultMap = resultSet.getResultMap();
        boolean isWindows = isWindows();
        resultMap.forEach((key, projectStat) -> {
            projectStat.fileTargetList.forEach(fileTarget -> {
                try {
                    File source = fileTarget.getFile();
                    String filePath;
                    //windows cut C：/ for example
                    if(isWindows){
                        filePath = source.getCanonicalPath().split(":", 1)[1];
                    }else{
                        filePath = source.getCanonicalPath();
                    }
                    File target = new File(dateDir+filePath);
                    FileUtil.copy(source, target);
                } catch (Exception e) {
                   logger.error(e.getMessage(), e);
                }
            });
        });
        outputAction(resultSet);
    }

    private boolean isWindows() {
        return System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
    }

}
