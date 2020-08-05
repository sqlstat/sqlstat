package com.fan.sqlstat.service.check;

import com.fan.sqlstat.constant.FileType;
import com.fan.sqlstat.model.FileTarget;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class SqlStatChechService implements ChechService {

    private Map<FileType, ChechService> chechServiceMap;

    @Resource
    private CommonFileCheckService commonFileCheckService;

    @Resource
    private JavaFileCheckService javaFileCheckService;

    @Resource
    private XmlCheckService xmlCheckService;

    @PostConstruct
    public void init(){
        chechServiceMap = new HashMap<>();
        chechServiceMap.put(FileType.JAVA, javaFileCheckService);
        chechServiceMap.put(FileType.C, commonFileCheckService);
        chechServiceMap.put(FileType.XML, xmlCheckService);
        chechServiceMap.put(FileType.SHELL, commonFileCheckService);
        chechServiceMap.put(FileType.SQL, commonFileCheckService);
        chechServiceMap.put(FileType.CTL, commonFileCheckService);
        chechServiceMap.put(FileType.OTHERS, commonFileCheckService);
    }

    @Override
    public FileTarget check(FileTarget fileTarget) {
        ChechService chechService = chechServiceMap.get(fileTarget.getFileType());
        fileTarget = chechService.check(fileTarget);
        return fileTarget;
    }
}
