package com.example.demo.vo;

import com.example.demo.common.ValidPassword;
import com.example.demo.entity.Authority;
import com.example.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
//    private Integer id;
//    private String username;
////    @ValidPassword
////    private String password;
//    private String name;
//    private String sex;
//    private String tel;
//    private String email;
//    private String avatar;
//    private String role;
//    private String address;
//    private LocalDateTime createTime;
//    private Integer status;
    private User user;
    private String token;
    private List<String> authorities;
//    private UserDetails userDetails;
}