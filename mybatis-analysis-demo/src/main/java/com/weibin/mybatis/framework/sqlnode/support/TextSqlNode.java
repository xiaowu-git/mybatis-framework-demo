package com.weibin.mybatis.framework.sqlnode.support;

import com.weibin.mybatis.framework.sqlnode.DynamicContext;
import com.weibin.mybatis.framework.sqlnode.SqlNode;

/**
 * 存放带有${}的SQL文本信息
 */
public class TextSqlNode implements SqlNode {

    private String sqlText;

    public TextSqlNode(String sqlText) {
        this.sqlText = sqlText;
    }

    public boolean isDynamic() {
        return this.sqlText.indexOf("${") > -1;
    }

    @Override
    public void apply(DynamicContext context) {

    }
}
