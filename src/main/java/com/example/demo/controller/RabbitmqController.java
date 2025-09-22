package com.example.demo.controller;

import com.example.demo.common.Ignore;
import com.example.demo.service.RabbitmqService;
import com.example.demo.service.impl.RabbitmqServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-16
 */
@RestController
@RequestMapping("/api")
public class RabbitmqController {
    @Resource
    private RabbitmqService rabbitmqService;

    /**
     * 生产者发送消息
     * @param msg
     * @return
     */
    @Ignore
    @PostMapping("/sendMsg")
    public String sendMsg(@RequestParam String msg){
        rabbitmqService.sendMsg(msg);
        return "ok";
    }
}