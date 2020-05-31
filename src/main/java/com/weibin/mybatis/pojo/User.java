package com.weibin.mybatis.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @Author: wwb
 * @Description:
 * @Date: Create in 20:35 2020/5/30
 */
@Data
public class User {
    private int id;
    private String name;
    private int age;
    private Date birthday;
    private String address;
}
