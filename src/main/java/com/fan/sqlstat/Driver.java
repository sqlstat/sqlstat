package com.fan.sqlstat;

import com.fan.sqlstat.constant.FileType;
import com.fan.sqlstat.model.FileTarget;
import com.fan.sqlstat.model.ProjectStat;
import com.fan.sqlstat.model.ResultSet;
import com.fan.sqlstat.util.FileUtil;
import com.fan.sqlstat.util.ScanFileFilter;
import com.fan.sqlstat.worker.FileScanWorker;
import com.fan.sqlstat.worker.FileStatWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class Driver {
    private static final Logger logger = LoggerFactory.getLogger(Driver.class);

    @Value("${app.workers:5}")
    private int workers;

    @Value("${app.baseDir}")
    private String baseDir;

    @Value("${app.queue.depth:300}")
    private int queueDepth;

    @Resource
    private ScanFileFilter scanFileFilter;

    @Resource
    private ResultSet resultSet;

    private BlockingQueue<FileTarget> blockingQueue;

    @PostConstruct
    private void init(){
        blockingQueue = new LinkedBlockingQueue<>(queueDepth);
    }

    public void launch() throws InterruptedException {
        logger.info("workers:{}",workers);
        logger.info("baseDir:{}",baseDir);
        logger.info("queueDepth:{}",queueDepth);
        File baseDirFile = FileUtil.checkBaseDir(baseDir);

        CountDownLatch countDownLatch = new CountDownLatch(workers);
        ExecutorService executorService = Executors.newFixedThreadPool(workers);
        for(int i=0; i<workers; i++){
            FileStatWorker fileStatWorker = new FileStatWorker(countDownLatch, blockingQueue);
            fileStatWorker.setResultSet(resultSet);
            executorService.execute(fileStatWorker);
        }

        FileScanWorker fileScanWorker = new FileScanWorker(countDownLatch, blockingQueue);
        fileScanWorker.setScanFileFilter(scanFileFilter);
        fileScanWorker.setBaseDirFile(baseDirFile);
        fileScanWorker.run();

        addPoison();
        countDownLatch.await();
        agggregateResult(resultSet);
        printResult(resultSet);
        executorService.shutdown();
    }

    private void addPoison() throws InterruptedException {
        for(int i=0; i<workers; i++){
            blockingQueue.put(new FileTarget(null, null,FileType.TASKEND));
        }
    }

    private void agggregateResult(ResultSet resultSet){
        List<Map<String, ProjectStat>> list = resultSet.getThreadResultList();
        Map<String, ProjectStat> resultMap = new HashMap<>();
        list.forEach(projectMap -> {
            projectMap.forEach((key, threadStat) -> {
                ProjectStat resultStat = resultMap.get(key);
                if(resultStat == null){
                    resultMap.put(key, threadStat);
                }else{
                    resultStat.shell += threadStat.shell;
                    resultStat.shellWithSql += threadStat.shellWithSql;
                    resultStat.sql += threadStat.sql;
                    resultStat.ctl += threadStat.ctl;
                    resultStat.others += threadStat.others;
                }
            });
        });
        resultSet.setResultMap(resultMap);
    }

    private void printResult(ResultSet resultSet){
        Map<String, ProjectStat> resultMap = resultSet.getResultMap();
        logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++");
        resultMap.forEach((key, projectStat) -> logger.info("{}", projectStat.toString()));
        logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++");
    }


}
