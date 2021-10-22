package com.gk.sqlstat.service.check;

import com.gk.sqlstat.constant.FileType;
import com.gk.sqlstat.model.FileTarget;
import com.gk.sqlstat.util.ScanFileFilter;
import com.gk.sqlstat.util.SpringContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;
import java.util.zip.Checksum;

@Component
public class JarFileCheckService implements CheckService {


    @Resource
    private ScanFileFilter scanFileFilter;

    @Autowired
    private CheckService sqlStatCheckService;

    @Override
    public FileTarget check(FileTarget fileTarget) {
        FileType actualFileType = null;
        for (String ext: scanFileFilter.getFileExtentions()) {
            String innerPath = fileTarget.getJarEntry().getName();
            if (innerPath.endsWith(ext) || innerPath.endsWith(ext.toUpperCase())) {
                actualFileType = FileType.getFileTypeByExt(ext);
                fileTarget.setFileType(actualFileType);
            }
        }
        if (actualFileType != null) {
            fileTarget = sqlStatCheckService.check(fileTarget);
        }
        return fileTarget;
    }
}
