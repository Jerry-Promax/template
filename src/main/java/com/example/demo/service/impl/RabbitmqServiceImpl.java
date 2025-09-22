package com.example.demo.service.impl;

import cn.hutool.core.lang.UUID;
import com.example.demo.config.RabbitConfig;
import com.example.demo.entity.User;
import com.example.demo.service.RabbitmqService;
import jakarta.annotation.Resource;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-16
 */
@Service
public class RabbitmqServiceImpl implements RabbitmqService{
    //日期格式化
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public String sendMsg(String msg) {
        try {
            String msgId = UUID.randomUUID().toString().replace("-","").substring(0,16);
            String sendTime = sdf.format(new Date());
            Map<String,Object> map = new HashMap<>();
            map.put("id",msgId);
            map.put("time",sendTime);
            map.put("msg",msg);

//            rabbitTemplate.convertAndSend(RabbitConfig.RABBITMQ_DEMO_DIRECT_EXCHANGE,RabbitConfig.RABBITMQ_DEMO_ROUTING,map);
            rabbitTemplate.convertAndSend(RabbitConfig.FANOUT_EXCHANGE_DEMO_NAME, "", map);
            return "ok";
        } catch (AmqpException e) {
            e.printStackTrace();
            return "error";
        }
    }
}