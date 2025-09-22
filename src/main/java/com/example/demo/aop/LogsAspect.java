package com.example.demo.aop;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import com.example.demo.common.BaseContext;
import com.example.demo.common.HoneyLogs;
import com.example.demo.entity.Logs;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.LogsService;
import com.example.demo.utils.IpUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-26
 */
@Aspect
@Component
@Slf4j
public class LogsAspect {
    @Resource
    private UserMapper userMapper;
    @Resource
    private LogsService logsService;
    @AfterReturning(pointcut = "@annotation(honeyLogs)",returning = "jsonResult")
    public void recordLog(JoinPoint joinPoint, HoneyLogs honeyLogs,Object jsonResult){
        // 获取当前登录用户
        Integer currentId = BaseContext.getCurrentId();
        User user = userMapper.selectById(currentId);
        if (user == null) {
            Object[] args = joinPoint.getArgs();
            if (ArrayUtil.isNotEmpty(args)) {
                if (args[0] instanceof User){
                    user = (User) args[0];
                }
            }
        }
        if (user == null) {
            return;
        }
        // 获取HttpServletRequest对象
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        // 获取ip信息
        String ipAddr = IpUtils.getIpAddr(request);
        Logs logs = Logs.builder()
                .user(user.getUsername())
                .operation(honeyLogs.operation())
                .type(honeyLogs.type().getValue())
                .ip(ipAddr)
                .time(LocalDateTime.now())
                .build();
        // 插入数据库,通过多线程来插入，异步的方式执行
        ThreadUtil.execAsync(()->{
            logsService.save(logs);
        });
    }
}