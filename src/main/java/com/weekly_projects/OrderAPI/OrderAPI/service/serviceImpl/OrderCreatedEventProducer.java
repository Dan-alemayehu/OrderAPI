package com.weekly_projects.OrderAPI.OrderAPI.service.serviceImpl;

import com.weekly_projects.OrderAPI.OrderAPI.config.KafkaTopicConfig;
import com.weekly_projects.OrderAPI.OrderAPI.event.CreatedOrderEvent;
import com.weekly_projects.OrderAPI.OrderAPI.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderCreatedEventProducer {

    private final String topic;

    private final KafkaTemplate<String, CreatedOrderEvent> kafkaTemplate;

    @Autowired
    public OrderCreatedEventProducer(KafkaTemplate<String, CreatedOrderEvent> kafkaTemplate,
                                     @Value("${app.kafka.topics.order-created}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void sendOrderCreatedEvent(CreatedOrderEvent order) {

        kafkaTemplate.send(topic, order)
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
