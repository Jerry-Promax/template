package com.example.demo.config;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-16
 */
public class RabbitConfig {
    // 队列主题名称
    public static final String RABBITMQ_DEMO_TOPIC = "rabbitmqDemoTopic";
    // direct交换机名称
    public static final String RABBITMQ_DEMO_DIRECT_EXCHANGE = "rabbitmqDemoDirectExchange";
    // direct交换机和队列绑定的匹配键
    public static final String RABBITMQ_DEMO_ROUTING = "rabbitmqDemoDirectRouting";

    /**
     * RabbitMQ的FANOUT_EXCHANG交换机类型的队列 A 的名称
     */
    public static final String FANOUT_EXCHANGE_QUEUE_TOPIC_A = "fanout.A";

    /**
     * RabbitMQ的FANOUT_EXCHANG交换机类型的队列 B 的名称
     */
    public static final String FANOUT_EXCHANGE_QUEUE_TOPIC_B = "fanout.B";

    /**
     * RabbitMQ的FANOUT_EXCHANG交换机类型的名称
     */
    public static final String FANOUT_EXCHANGE_DEMO_NAME = "fanout.exchange.demo.name";
}