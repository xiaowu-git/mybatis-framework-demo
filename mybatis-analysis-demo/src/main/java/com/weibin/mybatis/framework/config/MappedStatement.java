package com.weibin.mybatis.framework.config;

import com.weibin.mybatis.framework.sqlsource.SqlSource;
import lombok.Getter;
import lombok.Setter;

/**
 * 封装Mapper映射文件的每一个标签内容的信息（CRUD）
 */
@Getter@Setter
public class MappedStatement {

    private String statementId; // namespace与标签sqlId的结合

    private SqlSource sqlSource;

    private String statementType;

    private Class<?> parameterTypeClass;

    private Class<?> resultTypeClass;

    public MappedStatement(String statementId, Class<?> parameterTypeClass, Class<?> resultTypeClass, String statementType, SqlSource sqlSource) {
        this.statementId = statementId;
        this.parameterTypeClass = parameterTypeClass;
        this.resultTypeClass = resultTypeClass;
        this.statementType = statementType;
        this.sqlSource = sqlSource;
    }
}
