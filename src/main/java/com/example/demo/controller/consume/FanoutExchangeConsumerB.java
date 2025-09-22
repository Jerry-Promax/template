//package com.example.demo.controller.consume;
//
//import com.example.demo.config.RabbitConfig;
//import org.springframework.amqp.rabbit.annotation.Queue;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
///**
// * 类功能描述
// * <p>
// * 作者：jerry
// * 日期：2025-07-16
// */
//@Component
//@RabbitListener(queuesToDeclare = @Queue(RabbitConfig.FANOUT_EXCHANGE_QUEUE_TOPIC_B))
//public class FanoutExchangeConsumerB {
//    @RabbitHandler
//    public void process(Map<String, Object> map){
//        System.out.println("队列B收到消息：" + map.toString());
//    }
//}