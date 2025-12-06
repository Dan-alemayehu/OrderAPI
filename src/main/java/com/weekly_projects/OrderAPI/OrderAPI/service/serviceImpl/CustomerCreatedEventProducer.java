package com.weekly_projects.OrderAPI.OrderAPI.service.serviceImpl;

import com.weekly_projects.OrderAPI.OrderAPI.config.KafkaTopicConfig;
import com.weekly_projects.OrderAPI.OrderAPI.event.CreatedCustomerEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerCreatedEventProducer {

    private static final String TOPIC = KafkaTopicConfig.CUSTOMER_CREATED_TOPIC;

    private final KafkaTemplate<String, CreatedCustomerEvent> kafkaTemplate;

    @Autowired
    public CustomerCreatedEventProducer(KafkaTemplate<String, CreatedCustomerEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCustomerCreatedEvent(CreatedCustomerEvent customer) {
        kafkaTemplate.send(TOPIC, customer)
                .whenComplete((event, throwable) -> {
                    if (throwable == null) {
                        log.info("Kafka publish succeeded: topic={}, partition={}, offset={}",
                                event.getRecordMetadata().topic(),
                                event.getRecordMetadata().partition(),
                                event.getRecordMetadata().offset());
                    } else {
                        log.error("Kafka publish failed: {}", throwable.getMessage(), throwable);
                    }
                });
    }
}
