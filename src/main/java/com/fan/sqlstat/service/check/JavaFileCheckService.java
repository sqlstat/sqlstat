package com.fan.sqlstat.service.check;

import com.fan.sqlstat.model.FileTarget;
import org.springframework.stereotype.Component;

@Component
public class JavaFileCheckService implements ChechService {
    @Override
    public FileTarget check(FileTarget fileTarget) {
        return fileTarget;
    }
}
