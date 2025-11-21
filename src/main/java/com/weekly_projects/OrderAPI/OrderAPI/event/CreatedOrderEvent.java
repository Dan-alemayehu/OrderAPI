package com.weekly_projects.OrderAPI.OrderAPI.event;

import com.weekly_projects.OrderAPI.OrderAPI.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatedOrderEvent {
    private Long orderId;
    private LocalDateTime orderDate;
    private Customer customer;
}