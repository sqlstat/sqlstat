package com.gk.sqlstat.worker;

import com.gk.sqlstat.constant.FileType;
import com.gk.sqlstat.model.FileTarget;
import com.gk.sqlstat.model.ProjectStat;
import com.gk.sqlstat.model.ResultSet;
import com.gk.sqlstat.service.check.CheckService;
import com.gk.sqlstat.service.check.SqlStatCheckService;
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
//        CheckService checkService = SpringContext.getBean(SqlStatCheckService.class);

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

//                fileTarget = checkService.check(fileTarget);
                ProjectStat projectStat = threadMap.get(projectName);
                if(projectStat == null){
                    projectStat = new ProjectStat();
                    projectStat.projectName = projectName;
                    threadMap.put(projectName, projectStat);
                }
                switch(fileTarget.getFileType()){
                    case JAVA:
                        if (fileTarget.isTarget()) {
                            projectStat.java += 1;
                            projectStat.javaSqlHitNum += fileTarget.getSqlItemNum();
                        }
                        projectStat.javaFileTotalCnt += 1;
                        break;
                    case C:
                        if (fileTarget.isTarget()) {
                            projectStat.c += 1;
                            projectStat.cSqlNum += fileTarget.getSqlItemNum();
                        }
                        break;
                    case XML:
                        if (fileTarget.isTarget()) {
                            projectStat.xml += 1;
                            projectStat.xmlSqlHitNum += fileTarget.getSqlItemNum();
                            projectStat.sqlMapSqlHitNum += fileTarget.getSqlMapSqlHitCnt();
                            projectStat.mapperSqlHitNum += fileTarget.getMapperSqlHitCnt();
                        }
                        projectStat.xmlSqlSum += fileTarget.getXmlSqlCnt();
                        projectStat.xmlFileTotalCnt += fileTarget.isSqlMapOrMapper()?1:0;
                        break;
                    case SHELL:
                        if (fileTarget.isTarget()) {
                            projectStat.shell += 1;
                            projectStat.shellSqlHitNum += fileTarget.getSqlItemNum();
                        }
                        projectStat.shellFileTotalCnt += 1;
                        break;
                    case SQL:
                        if (fileTarget.isTarget()) {
                            projectStat.sql += 1;
                            projectStat.sqlSqlNum += fileTarget.getSqlItemNum();
                        }
                        projectStat.sqlFileTotalCnt += 1;
                        break;
                    case CTL:
                        if (fileTarget.isTarget()) {
                            projectStat.ctl += 1;
                        }
                        break;
                    case OTHERS:
                        if (fileTarget.isTarget()){
                            projectStat.others += 1;
                            projectStat.othersSqlNum += fileTarget.getSqlItemNum();
                        }
                        break;
                }

                if (fileTarget.isTarget()) {
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
