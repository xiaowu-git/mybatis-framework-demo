package com.weibin.mybatis.framework.sqlsource.support;

import com.weibin.mybatis.framework.sqlnode.SqlNode;
import com.weibin.mybatis.framework.sqlsource.BoundSql;
import com.weibin.mybatis.framework.sqlsource.SqlSource;

/**
 * 封装解析出来的SqlNode信息（包含动态标签或者${}）
 * 注：${}需要在每次执行getBoundSql的时候被解析一次
 */
public class DynamicSqlSource implements SqlSource {

    //解析出来的所有SqlNode节点信息
    private SqlNode rootSqlNode;

    public DynamicSqlSource(SqlNode rootSqlNode) {
        this.rootSqlNode = rootSqlNode;
    }

    @Override
    public BoundSql getBoundSql(Object params) {
        return null;
    }
}
