package com.weekly_projects.OrderAPI.OrderAPI.event;

import com.weekly_projects.OrderAPI.OrderAPI.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class CreatedOrderEvent {
    private String eventId;
    private int eventVersion;
    private Instant occurredAt;

    private Long orderId;
    private Instant orderDate;
    private Long customerId;
}