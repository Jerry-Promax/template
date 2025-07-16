package com.example.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 类功能描述
 *
 * 作者：jerry
 * 日期：2025-07-01
 */
@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String sex;
    private String tel;
    private String email;
    private String avatar;
    private String role;
    private String address;
    private LocalDateTime createTime;
    private Integer status;
    private String token;

}