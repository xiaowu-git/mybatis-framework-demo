package com.weibin.mybatis.framework.sqlsource.support;

import com.weibin.mybatis.framework.sqlnode.SqlNode;
import com.weibin.mybatis.framework.sqlsource.BoundSql;
import com.weibin.mybatis.framework.sqlsource.SqlSource;

/**
 * 封装解析出来的SqlNode信息（包含非动态标签或者#{}
 * 注：#{}只需被解析一次就可以了
 */
public class RawSqlSource implements SqlSource {

    public RawSqlSource(SqlNode rootSqlNode) {
        // TODO 解析#{}
    }

    @Override
    public BoundSql getBoundSql(Object params) {
        return null;
    }
}
