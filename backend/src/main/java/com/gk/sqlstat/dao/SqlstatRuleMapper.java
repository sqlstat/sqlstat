package com.gk.sqlstat.dao;

import com.gk.sqlstat.model.SqlstatRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SqlstatRuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SqlstatRule record);

    int insertSelective(SqlstatRule record);

    SqlstatRule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SqlstatRule record);

    int updateByPrimaryKey(SqlstatRule record);

    List<SqlstatRule> getRuleByType(Integer type);
}