package com.weibin.mybatis.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: wwb
 * @Description:
 * @Date: Create in 20:40 2020/5/30
 */
public class SimpleTypeRegistry {

    private static final Set<Class<?>> SIMPLE_TYPE_SET = new HashSet<>();

    static {
        SIMPLE_TYPE_SET.add(String.class);
        SIMPLE_TYPE_SET.add(Byte.class);
        SIMPLE_TYPE_SET.add(Short.class);
        SIMPLE_TYPE_SET.add(Character.class);
        SIMPLE_TYPE_SET.add(Integer.class);
        SIMPLE_TYPE_SET.add(Float.class);
        SIMPLE_TYPE_SET.add(Double.class);
        SIMPLE_TYPE_SET.add(Boolean.class);
        SIMPLE_TYPE_SET.add(Long.class);
        SIMPLE_TYPE_SET.add(Class.class);
        SIMPLE_TYPE_SET.add(Date.class);
        SIMPLE_TYPE_SET.add(BigInteger.class);
        SIMPLE_TYPE_SET.add(BigDecimal.class);
    }

    public static boolean isSimpleType(Class<?> clazz) {
        return SIMPLE_TYPE_SET.contains(clazz);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println(isSimpleType(Integer.class));
    }
}
