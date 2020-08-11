package com.fan.sqlstat.util;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class FileUtilTest {

    @Test
    public void test01(){
        FileUtil.copy(new File("/tmp/1.txt"), new File("/tmp/aaa/bbb/ccc/1.txt"));
    }

    @Test
    public void test02() throws IOException {
        File file = new File("../abc/abc.txt");
        System.out.println(file.getCanonicalPath());
        System.out.println(file.getAbsolutePath());
        System.out.println(file.getName());
        System.out.println(file.getPath());
    }

    @Test
    public void test03(){
        System.out.println(System.getProperties().getProperty("os.name"));
    }
}
