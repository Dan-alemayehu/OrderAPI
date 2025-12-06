package com.weekly_projects.OrderAPI.OrderAPI.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String ORDER_CREATED_TOPIC = "OrderCreated";
    public static final String CUSTOMER_CREATED_TOPIC = "CustomerCreated";

    @Bean
    public NewTopic orderCreatedTopic() {
        return TopicBuilder.name(ORDER_CREATED_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic customerCreatedTopic(){
        return TopicBuilder.name(CUSTOMER_CREATED_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
