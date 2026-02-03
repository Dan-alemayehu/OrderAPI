package com.weekly_projects.OrderAPI.OrderAPI.event;

import lombok.Data;

import java.time.Instant;

@Data
public class CreatedCustomerEvent {
    private String eventId;
    private int eventVersion;
    private Instant occurredAt;

    private Long customerId;
    private String email;
    private String name;
}