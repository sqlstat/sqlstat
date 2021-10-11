package com.gk.sqlstat.service.check.jarTest;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.io.File;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarCheckServiceTest {

    @Test
    public void testCheckJar() {
        try {
            SAXReader reader = new SAXReader();
            File file = new File("D:\\cdb\\gch\\code\\sqlstat-test\\sqlstat-1.0.1-all.jar");
            JarFile jar = new JarFile(file);
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                if (jarEntry.getName().endsWith(".xml")) {

                    Document document = reader.read(jar.getInputStream(jarEntry));
                    System.out.println(document.getDocType());
                }
                System.out.println(jarEntry.getName());

            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
