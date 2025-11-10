package com.weekly_projects.OrderAPI.OrderAPI.repository;

import com.weekly_projects.OrderAPI.OrderAPI.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
