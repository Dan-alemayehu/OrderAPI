package com.weekly_projects.OrderAPI.OrderAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private String email;
    private String name;
}