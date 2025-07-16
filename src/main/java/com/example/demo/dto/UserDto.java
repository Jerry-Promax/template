package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer id;
    private String username;
    private String password;
    private String token;
    private String name;
    private String sex;
    private String tel;
    private String email;
    private String avatar;
    private String role;
    private String address;
}