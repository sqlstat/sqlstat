package com.fan.sqlstat.worker;

import com.fan.sqlstat.constant.FileType;
import com.fan.sqlstat.model.FileTarget;
import com.fan.sqlstat.model.ProjectStat;
import com.fan.sqlstat.model.ResultSet;
import com.fan.sqlstat.service.check.ChechService;
import com.fan.sqlstat.service.check.SqlStatChechService;
import com.fan.sqlstat.util.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class FileStatWorker implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(FileStatWorker.class);

    private CountDownLatch countDownLatch;
    private BlockingQueue<FileTarget> blockingQueue;

    private ResultSet resultSet;

    private Map<String, ProjectStat> threadMap = new HashMap<>();

    public FileStatWorker(CountDownLatch countDownLatch, BlockingQueue<FileTarget> blockingQueue){
        this.countDownLatch = countDownLatch;
        this.blockingQueue = blockingQueue;
    }
    @Override
    public void run() {
        logger.info("FileStatWorker Started");
        ChechService chechService = SpringContext.getBean(SqlStatChechService.class);

        while(true){
            try {
                FileTarget fileTarget = blockingQueue.take();
                if(fileTarget.getFileType().equals(FileType.TASKEND)){
                    resultSet.getThreadResultList().add(threadMap);
                    break;
                }

                String projectName = fileTarget.getProject();
                File file = fileTarget.getFile();
                FileType fileType = fileTarget.getFileType();
                logger.trace("project {}, filename {}, type {}", projectName, file.getName(), fileType);

                fileTarget = chechService.check(fileTarget);
                if(fileTarget.isTarget()){
                    ProjectStat projectStat = threadMap.get(projectName);
                    if(projectStat == null){
                        projectStat = new ProjectStat();
                        projectStat.projectName = projectName;
                        threadMap.put(projectName, projectStat);
                    }
                    switch(fileTarget.getFileType()){
                        case JAVA:
                            projectStat.java += 1;
                            projectStat.javaSqlNum += fileTarget.getSqlItemNum();
                            break;
                        case C:
                            projectStat.c += 1;
                            projectStat.cSqlNum += fileTarget.getSqlItemNum();
                            break;
                        case XML:
                            projectStat.xml += 1;
                            projectStat.xmlSqlNum += fileTarget.getSqlItemNum();break;
                        case SHELL:
                            projectStat.shell += 1;
                            projectStat.shellSqlNum += fileTarget.getSqlItemNum();
                            break;
                        case SQL:
                            projectStat.sql += 1;
                            projectStat.sqlSqlNum += fileTarget.getSqlItemNum();
                            break;
                        case CTL:
                            projectStat.ctl += 1;
                            break;
                        case OTHERS:
                            projectStat.others += 1;
                            projectStat.othersSqlNum += fileTarget.getSqlItemNum();
                            break;
                    }
                    projectStat.fileTargetList.add(fileTarget);
                }


            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
        logger.info("FileStatWorker leaving");
        countDownLatch.countDown();
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
}
