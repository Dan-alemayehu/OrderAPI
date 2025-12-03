package com.weekly_projects.OrderAPI.OrderAPI.service.serviceImpl;

import com.weekly_projects.OrderAPI.OrderAPI.config.KafkaTopicConfig;
import com.weekly_projects.OrderAPI.OrderAPI.event.CreatedOrderEvent;
import com.weekly_projects.OrderAPI.OrderAPI.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderCreatedEventProducer {

    private static final String TOPIC = KafkaTopicConfig.ORDER_CREATED_TOPIC;

    private final KafkaTemplate<String, CreatedOrderEvent> kafkaTemplate;

    @Autowired
    public OrderCreatedEventProducer(KafkaTemplate<String, CreatedOrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderCreatedEvent(CreatedOrderEvent order) {

        kafkaTemplate.send(TOPIC, order)
                .whenComplete((result, error) -> {
                    if (error == null) {
                        log.info("Kafka publish succeeded: topic={}, partition={}, offset={}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    } else {
                        log.error("Kafka publish failed: {}", error.getMessage(), error);
                    }
                });
    }
}
