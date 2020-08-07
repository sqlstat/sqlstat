package com.fan.sqltest.util.regex;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {

    @Test
    public void test01(){
        String sql = "select a.*, b.* from test a, test b where a.id=b.id(+)";
        System.out.println(sql);
        String regex = "\\(\\+\\)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        System.out.println("regex match? "+matcher.find());
    }

    @Test
    public void test02(){
        String sql = "select to_char  " +
                "(sysdate, '%Y%m%d') from dual";
        System.out.println(sql);
        String regex = "to_char\\s*\\(";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        System.out.println("regex match? "+matcher.find());
    }

    @Test
    public void test03(){
        String sql = "select to_char    \n" +
                "(sysdate, '%Y%m%d') from dual";
        System.out.println(sql);
        String regex = "select\\s+?\\S+?\\s+?(?:\\S+?\\s+?)*?from";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        System.out.println("regex match? "+matcher.find());
    }

    @Test
    public void test04(){
        String sql = "select to_char\n" +
                "(sysdate, '%Y%m%d')\nfrom dual where rowNum < 10";
        System.out.println(sql);
        String regex = "rownum";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        System.out.println("regex match? "+matcher.find());
    }


    @Test
    public void test05(){
        String sql = "\t\tSELECT ID,NAME,ROW_NUMBER\n()\n" +
                "\t\t\tOVER(partition by ID order by name desc) as RN FROM test;";
        System.out.println(sql);
        String regex = "row_number\\s*?\\(.*?\\)\\s+?over";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        System.out.println("regex match? "+matcher.find());
    }

    @Test
    public void test06(){
        String sql = "SELECT id, name, max\t\n(id\t)\t over(partition by name order by  name) as maxId from test";
        System.out.println(sql);
        String regex = "max\\s*?\\(.*?\\)\\s+?over";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        System.out.println("regex match? "+matcher.find());
    }

    @Test
    public void test07(){
        String sql = "SELECT id, listagg (name, \t',') WITHIN GROUP (ORDER BY name) names FROM test t GROUP BY id";
        System.out.println(sql);
        String regex = "listagg\\s*?\\(.*?\\)\\s+?WITHIN\\s+?GROUP";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        System.out.println("regex match? "+matcher.find());
    }


    @Test
    public void test08(){
        String sql = "SELECT id, \nlistagg (name, \t',') \nWITHIN GROUP (ORDER BY name) names FROM test t GROUP BY id";
        System.out.println(sql.replace("\n"," "));

    }

}
