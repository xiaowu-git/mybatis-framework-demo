package com.weibin.mybatis.framework.sqlsource;

/**
 * 封装映射文件SQL标签的信息,包括SqlNode一些信息，提供sql解析结果的接口
 */
public interface SqlSource {
    /**
     * 解析SqlNode，并返回BoundSql
     * @param params
     * @return
     */
    BoundSql getBoundSql(Object params);
}
