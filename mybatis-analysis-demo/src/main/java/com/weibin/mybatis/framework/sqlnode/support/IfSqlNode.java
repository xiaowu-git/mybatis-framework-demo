package com.weibin.mybatis.framework.sqlnode.support;

import com.weibin.mybatis.framework.sqlnode.DynamicContext;
import com.weibin.mybatis.framework.sqlnode.SqlNode;

/**
 * 存放if标签对应的sql文本信息
 */
public class IfSqlNode implements SqlNode {

    private String test; //test属性
    private MixedSqlNode mixedSqlNode;

    public IfSqlNode(String test, MixedSqlNode mixedSqlNode) {
        this.test = test;
        this.mixedSqlNode = mixedSqlNode;
    }

    @Override
    public void apply(DynamicContext context) {

    }
}
