package com.weibin.mybatis.framework.sqlnode;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * SqlNode处理sql过程中的上下文对象
 */
public class DynamicContext {
    // 存放所有SqlNode处理sql后的信息，即拼接完整的一条sql语句
    private StringBuffer sb = new StringBuffer();

    // 存放SqlNode执行过程中需要的一些信息
    @Getter
    private Map<String, Object> bindings = new HashMap<>();

    public DynamicContext(Object param) {
        this.bindings.put("_parameter", param);
    }

    public String getSql() {
        return sb.toString();
    }

    public void appendSql(String sqlText) {
        this.sb.append(sqlText).append(" ");
    }

    public void addBindings(String name, Object binding) {
        this.bindings.put(name, binding);
    }

}
