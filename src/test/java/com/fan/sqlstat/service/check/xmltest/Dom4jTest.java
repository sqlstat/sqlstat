package com.fan.sqlstat.service.check.xmltest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.io.File;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dom4jTest {

    @Test
    public void test01() {
        System.out.println(System.getProperty("user.dir"));
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(new File("src/test/resource/ibatisTestFile/UserPo.xml"));
//            Document document = reader.read(new File("src/test/resource/env.sh"));
            System.out.println("document name:"+document.getName());
            if(document.getDocType().getElementName().equals("sqlMap") &&
                    document.getDocType().getSystemID().equals("http://www.ibatis.com/dtd/sql-map-2.dtd")){
                System.out.println("ibatis mapping file found");
                Element xmlroot = document.getRootElement();
                Iterator it = xmlroot.elementIterator();
                int sqlNum=0;
                while (it.hasNext()) {
                    Element element = (Element) it.next();
                    if(element.getName().equals("select") || element.getName().equals("insert")
                            || element.getName().equals("update") || element.getName().equals("delete")){
                        String sql = element.getStringValue();
                        System.out.println("sql found:"+sql);
                        String regex = "select\\s+(\\S\\s*){1,}from";
                        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                        Matcher matcher = pattern.matcher(sql);
                        System.out.println("regex match? "+matcher.find());
                        sqlNum++;
                    }
                }
                System.out.println("sqlNum:"+sqlNum);
            }



        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
