package com.gk.sqlstat.service.check.xmltest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;
import org.dom4j.tree.DefaultEntity;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Iterator;
import java.util.List;

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
                    DefaultElement element = (DefaultElement) it.next();
                    if(element.getName().equals("select") || element.getName().equals("insert")
                            || element.getName().equals("update") || element.getName().equals("delete")){
                        sqlNum++;
                        System.out.println("element:"+element.toString());
                        System.out.println("test:"+element.getText());
                        System.out.println("data:"+element.getData());
                        System.out.println("nodes:"+element.content());
                        System.out.println("asXml:"+element.asXML());
                        System.out.println("stringValue:"+element.getStringValue());

                        List<Node> list = element.content();

                        StringBuilder stringBuilder = new StringBuilder();
                        for(Node node : list){
                            stringBuilder.append(node.asXML());
                        }
                        System.out.println("contents asXml:"+stringBuilder.toString());

                        element.clearContent();
                        String resultStr = stringBuilder.toString().replaceAll("decode\\s*?\\(", "newFunc(");
                        DefaultEntity entity = new DefaultEntity("tmp", resultStr);
                        element.add(entity);
                    }
                }
                System.out.println("sqlNum:"+sqlNum);
                System.out.println("newDoc:"+document.asXML());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
