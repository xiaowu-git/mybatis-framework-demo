package com.weibin.mybatis.test;

import org.junit.Test;

import java.sql.*;

/**
 * @Author: wwb
 * @Description: jdbc 基本流程
 * @Date: Create in 20:38 2020/5/30
 */
public class JDBCDemo {

    @Test
    public void testJDBC() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // 1. 加载驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 2. 获取连接
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mybatis-test?characterEncoding=utf-8",
                    "root", "123456");
            // 3.创建sql语句
            String sql = "select * from User where username = ?";

            // 4.获取预处理 PreparedStatement
            stmt = conn.prepareStatement(sql);

            // 5.设置参数
            stmt.setString(1, "伟斌");

            // 6.执行sql，获取结果集
            rs = stmt.executeQuery();

            // 7.遍历输出结果集
            while (rs.next()) {
                System.out.println(rs.getString("username") + " | " + rs.getString("age") + " | " + rs.getString("address"));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
