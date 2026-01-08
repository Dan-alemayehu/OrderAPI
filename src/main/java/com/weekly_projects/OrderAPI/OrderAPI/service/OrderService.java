package com.weekly_projects.OrderAPI.OrderAPI.service;

import com.weekly_projects.OrderAPI.OrderAPI.dto.OrderDto;
import com.weekly_projects.OrderAPI.OrderAPI.model.Customer;
import com.weekly_projects.OrderAPI.OrderAPI.model.Order;

public interface OrderService {

    Order findOrderById(long id);
    Order createOrder(OrderDto order);

}
