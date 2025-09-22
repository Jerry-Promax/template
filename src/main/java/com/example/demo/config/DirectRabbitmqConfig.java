package com.example.demo.config;



import jakarta.annotation.Resource;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-16
 */
@Configuration
public class DirectRabbitmqConfig {
//  Direct Exchange

//    // 声明队列
//    @Bean
//    public Queue rabbitmqDirectQueue(){
//        /**
//         * 1、name:    队列名称
//         * 2、durable: 是否持久化
//         * 3、exclusive: 是否独享、排外的。如果设置为true，定义为排他队列。则只有创建者可以使用此队列。也就是private私有的。
//         * 4、autoDelete: 是否自动删除。也就是临时队列。当最后一个消费者断开连接后，会自动删除。
//         * */
//        return new Queue(RabbitConfig.RABBITMQ_DEMO_TOPIC,true,false,false);
//    }
//    // 声明交换机
//    @Bean
//    public DirectExchange rabbitmqDirectExchange(){
//        return new DirectExchange(RabbitConfig.RABBITMQ_DEMO_DIRECT_EXCHANGE,true,false);
//    }
//    // 绑定交换机和队列，并设置匹配键
//    @Bean
//    public Binding bindDirect(){
//        return BindingBuilder.bind(rabbitmqDirectQueue()).to(rabbitmqDirectExchange()).with(RabbitConfig.RABBITMQ_DEMO_ROUTING);
//    }

// fanout exchange

    // 队列A
    @Bean
    public Queue fanoutExchangeQueueA(){
        return new Queue(RabbitConfig.FANOUT_EXCHANGE_QUEUE_TOPIC_A,true,false,false);
    }
    // 队列B
    @Bean
    public Queue fanoutExchangeQueueB() {
        return new Queue(RabbitConfig.FANOUT_EXCHANGE_QUEUE_TOPIC_B, true, false, false);
    }
    // 声明交换机
    @Bean
    public FanoutExchange rabbitmqDemoFanoutExchange(){
        return new FanoutExchange(RabbitConfig.FANOUT_EXCHANGE_DEMO_NAME,true,false);
    }
    // 队列A绑定到FanoutExchange交换机
    @Bean
    public Binding bindFanoutA(){
        return BindingBuilder.bind(fanoutExchangeQueueA()).to(rabbitmqDemoFanoutExchange());
    }
    // 队列B绑定到FanoutExchange交换机
    @Bean
    public Binding bindFanoutB() {
        return BindingBuilder.bind(fanoutExchangeQueueB()).to(rabbitmqDemoFanoutExchange());
    }



    // 配置 JSON 消息转换器
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    // 给 RabbitTemplate 设置转换器（生产者用）
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    // 给消费者监听容器设置转换器
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter()); // 消费者用同样的转换器
        factory.setPrefetchCount(1);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 手动确认
        return factory;
    }
}