package com.gk.sqlstat.worker;

import com.gk.sqlstat.constant.FileType;
import com.gk.sqlstat.model.FileTarget;
import com.gk.sqlstat.util.ScanFileFilter;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
            } catch (Exception e) {
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
                    } catch (Exception e) {
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

    private void dirRecursion(File baseDir, String projectName) throws Exception {
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
                        if (fileType.equals(FileType.JAR)) {
                            // jar类型的文件 读取其中文件进行处理
                            jarFileScan(blockingQueue, projectName, file);
                        } else {
                            FileTarget fileTarget = new FileTarget(projectName, file, fileType, file.getCanonicalPath());
                            blockingQueue.put(fileTarget);
                        }
                        logger.trace("put into queue, {}, queueSize {}", file.getAbsolutePath(),blockingQueue.size());
                        break;
                    }
                }
            }
        }
    }

    private void jarFileScan(BlockingQueue blockingQueue, String projectName, File jarFile) throws Exception {
        JarFile jar = new JarFile(jarFile);
        Enumeration<JarEntry> entries = jar.entries();
        String jarPath = jarFile.getCanonicalPath();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            if (!jarEntry.isDirectory()) {
                // 如果是jar文件，暂不处理
                if (jarEntry.getName().endsWith(".jar") || jarEntry.getName().endsWith(".jar".toUpperCase())) {
                    logger.info("****** jar file has a jar in it！ path: " + jarFile.getCanonicalPath());
                    continue;
                }
                String filePath = jarPath + "-" + jarEntry.getName();
                FileTarget fileTarget = new FileTarget(projectName, jarFile, FileType.JAR, filePath, jarEntry, jar);
                blockingQueue.put(fileTarget);
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
            blockingQueue.put(new FileTarget(null, null, FileType.TASKEND, null));
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
