package com.gk.sqlstat.service.check;

import com.gk.sqlstat.model.FileTarget;
import org.springframework.stereotype.Component;

@Component
public class JavaFileCheckService implements ChechService {
    @Override
    public FileTarget check(FileTarget fileTarget) {
        return fileTarget;
    }
}
