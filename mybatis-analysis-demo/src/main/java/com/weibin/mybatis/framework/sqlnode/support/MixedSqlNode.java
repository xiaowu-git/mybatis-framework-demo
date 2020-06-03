package com.weibin.mybatis.framework.sqlnode.support;

import com.weibin.mybatis.framework.sqlnode.DynamicContext;
import com.weibin.mybatis.framework.sqlnode.SqlNode;

import java.util.List;

/**
 * 存放同一级别的Sql文本信息
 */
public class MixedSqlNode implements SqlNode {

    private List<SqlNode> sqlNodes;

    public MixedSqlNode(List<SqlNode> sqlNodes) {
        this.sqlNodes = sqlNodes;
    }

    @Override
    public void apply(DynamicContext context) {

    }
}
