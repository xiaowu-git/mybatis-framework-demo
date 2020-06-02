package com.weibin.mybatis.framework.sqlsource;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 封装解析SQL标签后的sql语句及对应的#{}的参数映射信息
 */
public class BoundSql {

    @Setter@Getter
    private String sql; // jdbc可执行的SQL语句
    @Getter
    private List<ParameterMapping> parameterMappings;

    public void addParameterMapping(ParameterMapping parameterMapping) {
        this.parameterMappings.add(parameterMapping);
    }
}
