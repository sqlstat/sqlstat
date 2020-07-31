package com.fan.sqlstat.service.check;

import com.fan.sqlstat.constant.FileType;
import com.fan.sqlstat.model.FileTarget;
import com.fan.sqlstat.service.check.ChechService;
import com.fan.sqlstat.service.check.CommonFileCheckService;
import com.fan.sqlstat.service.check.JavaFileCheckService;
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

    @PostConstruct
    public void init(){
        chechServiceMap = new HashMap<>();
        chechServiceMap.put(FileType.JAVA, javaFileCheckService);
        chechServiceMap.put(FileType.C, commonFileCheckService);
        chechServiceMap.put(FileType.XML, commonFileCheckService);
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
