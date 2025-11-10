package com.weekly_projects.OrderAPI.OrderAPI.controllers;

import com.weekly_projects.OrderAPI.OrderAPI.model.Order;
import com.weekly_projects.OrderAPI.OrderAPI.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderRest {

    private final OrderService orderService;

    @Autowired
    public OrderRest(OrderService orderService) {
        this.orderService = orderService;
    }

    //Get Mapping: Find an order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> findOrderById(@PathVariable Long id){
        return ResponseEntity.ok(orderService.findOrderById(id));
    }

    //PostMapping: Create a new order
    @PostMapping("/{id}")
    public ResponseEntity<Order> createOrder(@PathVariable long id) {
        return new ResponseEntity<>(
                orderService.createOrder(id),
                HttpStatus.CREATED
        );
    }
}
