package com.weibin.mybatis.framework.config;

import lombok.Getter;
import lombok.Setter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 封装mybatis全局配置文件信息
 */
public class Configuration {

    @Setter@Getter
    private DataSource dataSource;

    // 每个MappedStatement 对应一个映射文件里面的一个statement（即一个CRUD标签）
    private Map<String, MappedStatement> mappedStatements = new HashMap<>();

    public MappedStatement getMappedStatementById(String statementId) {
        return mappedStatements.get(statementId);
    }

    public void addMappedStatement(String statementId, MappedStatement mappedStatement) {
        this.mappedStatements.put(statementId, mappedStatement);
    }
}
