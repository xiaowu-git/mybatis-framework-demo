package com.weibin.mybatis.framework.sqlsource;

import lombok.Getter;
import lombok.Setter;

/**
 * 封装#{}解析后的参数名称及对应参数类型的映射信息
 */
@Getter@Setter
public class ParameterMapping {

    private String name;

    private Class parameterType;
}
