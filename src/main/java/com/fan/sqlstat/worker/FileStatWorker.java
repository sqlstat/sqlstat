package com.fan.sqlstat.worker;

import com.fan.sqlstat.AppStart;
import com.fan.sqlstat.constant.FileType;
import com.fan.sqlstat.model.FileTarget;
import com.fan.sqlstat.model.ProjectStat;
import com.fan.sqlstat.model.ResultSet;
import com.fan.sqlstat.util.FileUtil;
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

                Map<FileType, Integer> statMap = FileUtil.statFile(fileTarget);
                int sql = statMap.get(FileType.SQL) == null ? 0 : statMap.get(FileType.SQL);
                int shell = statMap.get(FileType.SHELL) == null ? 0 : statMap.get(FileType.SHELL);
                int shellWithSql = statMap.get(FileType.SHELLWITHSQL) == null ? 0 : statMap.get(FileType.SHELLWITHSQL);
                int ctl = statMap.get(FileType.CTL) == null ? 0 : statMap.get(FileType.CTL);
                int others = statMap.get(FileType.OTHERS) == null ? 0 : statMap.get(FileType.OTHERS);

                ProjectStat projectStat = threadMap.get(projectName);
                if(projectStat == null){
                    projectStat = new ProjectStat();
                    projectStat.projectName = projectName;
                    projectStat.sql = sql;
                    projectStat.shell = shell;
                    projectStat.shellWithSql = shellWithSql;
                    projectStat.ctl = ctl;
                    projectStat.others = others;
                    threadMap.put(projectName, projectStat);
                }else{
                    projectStat.sql += sql;
                    projectStat.shell += shell;
                    projectStat.shellWithSql += shellWithSql;
                    projectStat.ctl += ctl;
                    projectStat.others += others;
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
