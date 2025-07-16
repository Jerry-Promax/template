package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.example.demo.common.Ignore;
import com.example.demo.common.Result;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-01
 */
@RestController
@Slf4j
public class WebController {
    @Resource
    private UserService userService;

    @Ignore
    @PostMapping("/login")
    public Result login(@RequestBody UserDto user){
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error("数据输入不合法");
        }
        User loginUser = userService.login(user);
        return Result.success(loginUser);
    }

    @Ignore
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error("数据输入不合法");
        }
        userService.register(user);
        return Result.success();
    }
    @Ignore
    @PutMapping("/resetPwd")
    public Result resetPwd(@RequestBody UserDto userDto){
        userService.resetPwd(userDto);
        return Result.success();
    }
}