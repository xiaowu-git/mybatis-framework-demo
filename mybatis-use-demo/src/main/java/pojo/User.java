package pojo;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private int id;
    private String userName;
    private int age;
    private Date birthday;
    private String address;
}
