package com.gk.sqltest.util.regex;

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

    @Test
    public void test09(){
        String sql = "SELECT  concat('a','b') /  ||'a''b' FROM test t ";
        System.out.println(sql);
        String regex = "\\|\\|";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        System.out.println("regex match? "+matcher.find());
    }


    @Test
    public void test10(){
        String sql = "SELECT sqname.nextval FROM test t ";
        System.out.println(sql);
        String regex = "\\.nextval";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        System.out.println("regex match? "+matcher.find());
    }

    @Test
    public void test11() {
        String sql="select rs.*, rownum from t order by rownum, rownum fdafs rownum";
//        String sql="listagg (rs.*, rownum from t order by) within group";
        System.out.println(sql);
        String regex = "rownum(\\s+?.*?)*\\s+rownum";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        System.out.println(String.format("regex match: %s, match count: %s",matcher.find(), matcher.groupCount()));
    }

    @Test
    public void test12() {
        String sql="select rs.* from t full  outer   join b";
//        String sql="listagg (rs.*, rownum from t order by) within group";
        System.out.println(sql);
        String regex = "full\\s+outer\\s+join";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        int matchCnt = 0;
        while(matcher.find()) {
            matchCnt ++;
        }
        System.out.println(String.format("regex match: %s, match count: %s",matchCnt >0?true:false, matchCnt));
    }

    @Test
    public void test13() {
        String sql="select rs.* from t where a <= 2";
//        String sql="listagg (rs.*, rownum from t order by) within group";
        System.out.println(sql);
//        String regex = "[><!]\\s+?[=>]";
        String regex = "([<>!]\\s+=)|<\\s+>";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        int matchCnt = 0;
        while(matcher.find()) {
            matchCnt ++;
        }
        System.out.println(String.format("regex match: %s, match count: %s",matchCnt >0?true:false, matchCnt));
    }

    @Test
    public void test14() {
        String sql="select a from b where a = #abv:DATE#";
//        String sql="listagg (rs.*, rownum from t order by) within group";
        System.out.println(sql);
//        String regex = "[><!]\\s+?[=>]";
        String regex = ":\\s*date\\s*\\#";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        int matchCnt = 0;
        while(matcher.find()) {
            matchCnt ++;
        }
        System.out.println(String.format("regex match: %s, match count: %s",matchCnt >0?true:false, matchCnt));
    }

    @Test
    public void test15() {
        String sql="<result property=\"A\" column=\"a\" jdbcType = \"DATE\"/>";
//        String sql="listagg (rs.*, rownum from t order by) within group";
        System.out.println(sql);
//        String regex = "[><!]\\s+?[=>]";
        String regex = "jdbcType\\s*=\\s*\"DATE\"";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        int matchCnt = 0;
        while(matcher.find()) {
            matchCnt ++;
        }
        System.out.println(String.format("regex match: %s, match count: %s",matchCnt >0?true:false, matchCnt));
    }

    @Test
    public void test16() {
        String sql="select * from a order by b last";
//        String sql="listagg (rs.*, rownum from t order by) within group";
        System.out.println(sql);
//        String regex = "[><!]\\s+?[=>]";
        String regex = "nulls\\s+(first|last)";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        int matchCnt = 0;
        while(matcher.find()) {
            matchCnt ++;
        }
        System.out.println(String.format("regex match: %s, match count: %s",matchCnt >0?true:false, matchCnt));
    }

    @Test
    public void test17() {
        String sql="select dense_rank() over() from a order by b last";
//        String sql="listagg (rs.*, rownum from t order by) within group";
        System.out.println(sql);
//        String regex = "[><!]\\s+?[=>]";
        String regex = "dense_rank\\s*?\\(.*?\\)\\s+?over";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        int matchCnt = 0;
        while(matcher.find()) {
            matchCnt ++;
        }
        System.out.println(String.format("regex match: %s, match count: %s",matchCnt >0?true:false, matchCnt));
    }

    @Test
    public void test18() {
        String sql="insert all s ";
//        String sql="listagg (rs.*, rownum from t order by) within group";
        System.out.println(sql);
//        String regex = "[><!]\\s+?[=>]";
        String regex = "INSERT\\s+ALL";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        int matchCnt = 0;
        while(matcher.find()) {
            matchCnt ++;
        }
        System.out.println(String.format("regex match: %s, match count: %s",matchCnt >0?true:false, matchCnt));
    }
}
