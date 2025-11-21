package com.weekly_projects.OrderAPI.OrderAPI.service.serviceImpl;

import com.weekly_projects.OrderAPI.OrderAPI.event.CreatedOrderEvent;
import com.weekly_projects.OrderAPI.OrderAPI.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedEventProducer {

    private final String TOPIC = "OrderCreated";

    private final KafkaTemplate<String, CreatedOrderEvent> kafkaTemplate;

    @Autowired
    public OrderCreatedEventProducer(KafkaTemplate<String, CreatedOrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderCreatedEvent(CreatedOrderEvent order) {
        kafkaTemplate.send(TOPIC, order);
    }
}
