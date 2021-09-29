package com.gk.sqlstat.worker;

import com.gk.sqlstat.constant.FileType;
import com.gk.sqlstat.model.FileTarget;
import com.gk.sqlstat.util.ScanFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class FileScanWorker implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(FileScanWorker.class);

    private CountDownLatch countDownLatch;
    private BlockingQueue<FileTarget> blockingQueue;

    private File baseDirFile;
    private ScanFileFilter scanFileFilter;
    private boolean baseDirIsProject;
    private List<String> excludeDirList;
    private List<String> excludeDirNameList;
    private int workers;

    public FileScanWorker(CountDownLatch countDownLatch, BlockingQueue<FileTarget> blockingQueue){
        this.countDownLatch = countDownLatch;
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        initExcludeDirs();
        if(baseDirIsProject == true){
            String projectName = baseDirFile.getName();
            logger.info("projectName {}", projectName);
            try {
                dirRecursion(baseDirFile, projectName);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }else{
            File[] files = baseDirFile.listFiles();
            for(File projectDir : files){
                if(projectDir.isDirectory()){
                    String projectName = projectDir.getName();
                    logger.info("projectName {}", projectName);
                    try {
                        dirRecursion(projectDir, projectName);
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }
        try {
            addPoison();
        } catch(InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        countDownLatch.countDown();
    }

    private void dirRecursion(File baseDir, String projectName) throws InterruptedException {
        if(!baseDir.isDirectory()){
            logger.error("input dir is not dir, {}", baseDir.getAbsolutePath());
            return;
        }
        if(isExcludeFile(baseDir)){
            logger.info("dir is excluded:{}", baseDir);
            return;
        }
        File[] files = baseDir.listFiles(scanFileFilter);
        for(File file : files){
            if(file.isDirectory()){
                logger.trace("dir found {}", file.getAbsolutePath());
                dirRecursion(file, projectName);
            }else{
                String filename = file.getName();
                for(String ext: scanFileFilter.getFileExtentions()){
                    if(filename.endsWith(ext)||filename.endsWith(ext.toUpperCase())){
                        FileType fileType = FileType.getFileTypeByExt(ext);
                        FileTarget fileTarget = new FileTarget(projectName, file, fileType);
                        blockingQueue.put(fileTarget);
                        logger.trace("put into queue, {}, queueSize {}", file.getAbsolutePath(),blockingQueue.size());
                        break;
                    }
                }
            }
        }
    }

    private void initExcludeDirs(){
        if(excludeDirList!=null){
            excludeDirNameList = new ArrayList<>();
            try {
                String baseDirStr = baseDirFile.getCanonicalPath();
                for(String excludeDirStr : excludeDirList){
                    excludeDirNameList.add(baseDirStr+File.separator+excludeDirStr);
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
            logger.debug("excludeFileList:{}", excludeDirNameList);
        }
    }

    private void addPoison() throws InterruptedException {
        for(int i=0; i<workers; i++){
            blockingQueue.put(new FileTarget(null, null, FileType.TASKEND));
        }
        logger.info("poison added");
    }

    private boolean isExcludeFile(File file) {
        if(excludeDirNameList == null){
            return false;
        }
        try {
            if(excludeDirNameList.contains(file.getCanonicalPath())){
                return true;
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    public int getWorkers() {
        return workers;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }

    public void setScanFileFilter(ScanFileFilter scanFileFilter){
        this.scanFileFilter = scanFileFilter;
    }

    public void setBaseDirFile(File baseDirFile) {
        this.baseDirFile = baseDirFile;
    }

    public void setBaseDirIsProject(boolean baseDirIsProject) {
        this.baseDirIsProject = baseDirIsProject;
    }

    public void setExcludeDirList(List<String> excludeDirList) {
        this.excludeDirList = excludeDirList;
    }
}
