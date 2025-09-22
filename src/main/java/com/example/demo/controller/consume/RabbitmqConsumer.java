//package com.example.demo.controller.consume;
//
//import com.example.demo.config.RabbitConfig;
//
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//import com.rabbitmq.client.Channel;
//
//import java.io.IOException;
//import java.util.Map;
//
///**
// * 类功能描述
// * <p>
// * 作者：jerry
// * 日期：2025-07-16
// */
//@Component
//@RabbitListener(queues = {RabbitConfig.RABBITMQ_DEMO_TOPIC})
//public class RabbitmqConsumer {
//    @RabbitHandler
//    public void process(Map map, Channel channel, Message message) throws IOException {
//        long deliveryTag = 0;
//        try {
//            deliveryTag = message.getMessageProperties().getDeliveryTag();
//            System.out.println("开始处理消息: " + map);
//            Thread.sleep(5000); // 模拟耗时操作，如2秒
//            System.out.println("完成处理消息: " + map);
//            channel.basicAck(deliveryTag, false); // 手动确认消息
//        } catch (Exception e) {
//            channel.basicNack(deliveryTag,false,true);
//        }
//
//    }
//}