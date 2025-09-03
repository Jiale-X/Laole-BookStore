package com.laole.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "user")
public class User {
    private int id;
    private String account;
    private String name;
    private String password;
    private int sex;
    private int age;
    private float balance;
    private String phone;

}
