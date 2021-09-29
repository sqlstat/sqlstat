package com.gk.sqlstat;

import com.gk.sqlstat.model.FileTarget;
import com.gk.sqlstat.model.ProjectStat;
import com.gk.sqlstat.model.ResultSet;
import com.gk.sqlstat.service.OutService;
import com.gk.sqlstat.util.FileUtil;
import com.gk.sqlstat.util.ScanFileFilter;
import com.gk.sqlstat.worker.FileScanWorker;
import com.gk.sqlstat.worker.FileStatWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
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

    @Value("${app.basedir.excludeDir:null}")
    private String excludeDir;

    @Value("${app.queue.depth:300}")
    private int queueDepth;

    @Value("${app.baseDir.isProject:false}")
    private boolean baseDirIsProject;

    @Resource
    private ScanFileFilter scanFileFilter;

    @Resource
    private ResultSet resultSet;

    @Resource
    private OutService outService;

    private BlockingQueue<FileTarget> blockingQueue;
    private List<String> excludeDirList;

    @PostConstruct
    private void init(){
        blockingQueue = new LinkedBlockingQueue<>(queueDepth);
        if(excludeDir!=null && !excludeDir.equals("null")){
            excludeDirList = Arrays.asList(excludeDir.split(";"));
            logger.info("excludeDirList:{}", excludeDirList);
        }
    }

    public void launch() throws InterruptedException {
        logger.info("workers:{}",workers);
        logger.info("baseDir:{}",baseDir);
        logger.info("queueDepth:{}",queueDepth);
        File baseDirFile = FileUtil.checkBaseDir(baseDir);

        CountDownLatch countDownLatch = new CountDownLatch(workers+1);
        ExecutorService executorService = Executors.newFixedThreadPool(workers + 1);
        for(int i=0; i<workers; i++){
            FileStatWorker fileStatWorker = new FileStatWorker(countDownLatch, blockingQueue);
            fileStatWorker.setResultSet(resultSet);
            executorService.execute(fileStatWorker);
        }

        FileScanWorker fileScanWorker = new FileScanWorker(countDownLatch, blockingQueue);
        fileScanWorker.setWorkers(workers);
        fileScanWorker.setScanFileFilter(scanFileFilter);
        fileScanWorker.setBaseDirFile(baseDirFile);
        fileScanWorker.setBaseDirIsProject(baseDirIsProject);
        fileScanWorker.setExcludeDirList(excludeDirList);
        executorService.execute(fileScanWorker);

        countDownLatch.await();
        agggregateResult(resultSet);
        outService.output(resultSet);
        logger.info("result set" + resultSet.getResultMap());
        executorService.shutdown();
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
                    resultStat.java += threadStat.java;
                    resultStat.javaSqlHitNum += threadStat.javaSqlHitNum;
                    resultStat.c += threadStat.c;
                    resultStat.cSqlNum += threadStat.cSqlNum;
                    resultStat.xml += threadStat.xml;
                    resultStat.xmlSqlHitNum += threadStat.xmlSqlHitNum;
                    resultStat.shell += threadStat.shell;
                    resultStat.shellSqlHitNum += threadStat.shellSqlHitNum;
                    resultStat.sql += threadStat.sql;
                    resultStat.sqlSqlNum += threadStat.sqlSqlNum;
                    resultStat.ctl += threadStat.ctl;
                    resultStat.others += threadStat.others;
                    resultStat.othersSqlNum += threadStat.othersSqlNum;
                    resultStat.xmlSqlSum += threadStat.xmlSqlSum;
                    resultStat.mapperSqlHitNum += threadStat.mapperSqlHitNum;
                    resultStat.sqlMapSqlHitNum += threadStat.sqlMapSqlHitNum;
                    resultStat.fileTargetList.addAll(threadStat.fileTargetList);
                }
            });
        });
        resultSet.setResultMap(resultMap);
    }

}
