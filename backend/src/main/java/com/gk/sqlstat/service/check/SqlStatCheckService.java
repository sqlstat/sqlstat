package com.gk.sqlstat.service.check;

import com.gk.sqlstat.constant.FileType;
import com.gk.sqlstat.model.FileTarget;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class SqlStatCheckService implements CheckService {

    private Map<FileType, CheckService> chechServiceMap;

    @Resource
    private CommonFileCheckService commonFileCheckService;

    @Resource
    private JavaFileCheckService javaFileCheckService;

    @Resource
    private XmlCheckService xmlCheckService;

    @Resource
    private JarFileCheckService jarFileCheckService;

    @PostConstruct
    public void init(){
        chechServiceMap = new HashMap<>();
        chechServiceMap.put(FileType.JAVA, commonFileCheckService);
        chechServiceMap.put(FileType.C, commonFileCheckService);
        chechServiceMap.put(FileType.XML, xmlCheckService);
        chechServiceMap.put(FileType.SHELL, commonFileCheckService);
        chechServiceMap.put(FileType.SQL, commonFileCheckService);
        chechServiceMap.put(FileType.CTL, commonFileCheckService);
        chechServiceMap.put(FileType.OTHERS, commonFileCheckService);
        chechServiceMap.put(FileType.JAR, jarFileCheckService);
    }

    @Override
    public FileTarget check(FileTarget fileTarget) {
        try {
            CheckService checkService = chechServiceMap.get(fileTarget.getFileType());
            fileTarget = checkService.check(fileTarget);
            return fileTarget;
        } catch (Exception e) {
            System.out.println(e);
        }
        return fileTarget;
    }
}
