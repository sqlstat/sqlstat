package com.fan.sqlstat.worker;

import com.fan.sqlstat.constant.FileType;
import com.fan.sqlstat.model.FileTarget;
import com.fan.sqlstat.util.ScanFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class FileScanWorker implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(FileScanWorker.class);

    private CountDownLatch countDownLatch;
    private BlockingQueue<FileTarget> blockingQueue;

    private File baseDirFile;
    private ScanFileFilter scanFileFilter;
    private boolean baseDirIsProject;

    private String sourceDir;

    public FileScanWorker(CountDownLatch countDownLatch, BlockingQueue<FileTarget> blockingQueue){
        this.countDownLatch = countDownLatch;
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
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

        countDownLatch.countDown();
    }

    private void dirRecursion(File baseDir, String projectName) throws InterruptedException {
        if(!baseDir.isDirectory()){
            logger.error("input dir is not dir, {}", baseDir.getAbsolutePath());
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

    public void setScanFileFilter(ScanFileFilter scanFileFilter){
        this.scanFileFilter = scanFileFilter;
    }

    public void setBaseDirFile(File baseDirFile) {
        this.baseDirFile = baseDirFile;
    }

    public void setBaseDirIsProject(boolean baseDirIsProject) {
        this.baseDirIsProject = baseDirIsProject;
    }
}
