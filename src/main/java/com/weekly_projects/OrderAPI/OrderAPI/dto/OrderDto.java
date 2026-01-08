package com.weekly_projects.OrderAPI.OrderAPI.dto;

import com.weekly_projects.OrderAPI.OrderAPI.model.Customer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    @NotNull @Positive
    private Long customerId;
}