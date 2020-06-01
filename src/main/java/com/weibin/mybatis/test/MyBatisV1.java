package com.weibin.mybatis.test;

import com.weibin.mybatis.pojo.User;
import com.weibin.mybatis.utils.SimpleTypeRegistry;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: wwb
 * @Description: mybatis V1 版本
 *     实现思路：jdbc + properties配置文件， 实现 参数处理 和 结果映射
 * @Date: Create in 20:48 2020/5/31
 */
public class MyBatisV1 {

    private Properties properties = new Properties();

    @Test
    public void test() {
        loadProperties("jdbc.properties");
        // 封装入参
        /*Map<String, Object> map = new HashMap<>();
        map.put("username", "伟斌");
        map.put("age", 20);
        List<User> userList = selectList("queryUserByNameAndAge", map);*/
        User user = new User();
        user.setUsername("伟斌");
        user.setAge(20);
        List<User> userList = selectList("queryUserByNameAndAge", user);
        System.out.println(userList);
    }

    private void loadProperties(String path) {
        try(InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path)) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <T> List<T> selectList(String statmentId, Object params) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        List<T> resultList = new ArrayList<>();
        try{
            // 1.加载驱动
            Class.forName(properties.getProperty("db.driver"));
            // 2. 获取连接
            connection = DriverManager.getConnection(properties.getProperty("db.url"), properties.getProperty("db.username"), properties.getProperty("db.password"));
            // 3.获取statement
            statement = connection.prepareStatement(properties.getProperty("db.sql." + statmentId));
            // 4.获取入参类型并设置参数
            String parameterType = properties.getProperty("db.sql." + statmentId + ".parameterType");
            Class<?> parameterTypeClass = Class.forName(parameterType);
            if (SimpleTypeRegistry.isSimpleType(parameterTypeClass)) {
                statement.setObject(1, params);
            }else if (params instanceof Map) {
                Map map = (Map) params;
                // 获取入参字段
                String[] paramNames = properties.getProperty("db.sql." + statmentId + ".paramnames").split(",");
                for (int i = 0; i < paramNames.length; i++) {
                    Object value = map.get(paramNames[i]);
                    statement.setObject(i + 1, value);
                }
            }else {
                // 获取入参字段
                String[] paramNames = properties.getProperty("db.sql." + statmentId + ".paramnames").split(",");
                for (int i = 0; i < paramNames.length; i++) {
                    Field field = parameterTypeClass.getDeclaredField(paramNames[i]);
                    field.setAccessible(true);
                    Object value = field.get(params);
                    statement.setObject(i + 1, value);
                }
            }
            // 5.执行查询
            rs = statement.executeQuery();
            // 6.获取结果集并封装结果集
            String returnClassType = properties.getProperty("db.sql." + statmentId + ".returnClassType");
            Class<?> returnClass = Class.forName(returnClassType);
            Object resultPojo = null;
            while (rs.next()) {
                resultPojo = returnClass.newInstance();
                ResultSetMetaData rsMetaData = rs.getMetaData();
                int columnCount = rsMetaData.getColumnCount();
                // 利用反射为结果实例字段赋值
                for (int i = 1; i <= columnCount; i++) {
                    // 获取结果集列名
                    String columnName = rsMetaData.getColumnName(i);
                    Field field = returnClass.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(resultPojo, rs.getObject(i));
                }
                resultList.add((T) resultPojo);
            }
            return resultList;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultList;
    }
}
