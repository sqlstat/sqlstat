package com.fan.sqlstat.util;

import org.junit.Test;

import java.io.File;

public class FileUtilTest {

    @Test
    public void test01(){
        FileUtil.copy(new File("/tmp/1.txt"), new File("/tmp/aaa/bbb/ccc/1.txt"));
    }
}
