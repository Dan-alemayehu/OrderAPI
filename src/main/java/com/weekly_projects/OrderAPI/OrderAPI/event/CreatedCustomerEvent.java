package com.weekly_projects.OrderAPI.OrderAPI.event;

import lombok.Data;

@Data
public class CreatedCustomerEvent {
    private Long id;
    private String name;
}
