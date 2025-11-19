package com.weekly_projects.OrderAPI.OrderAPI.service.serviceImpl;

import com.weekly_projects.OrderAPI.OrderAPI.event.CreatedOrderEvent;
import com.weekly_projects.OrderAPI.OrderAPI.model.Customer;
import com.weekly_projects.OrderAPI.OrderAPI.model.Order;
import com.weekly_projects.OrderAPI.OrderAPI.repository.CustomerRepository;
import com.weekly_projects.OrderAPI.OrderAPI.repository.OrderRepository;
import com.weekly_projects.OrderAPI.OrderAPI.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService {

    public final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public OrderServiceImpl(CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order findOrderById(long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with id " + id + " not found"));
    }

    @Override
    public Order createOrder(Long customerId) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setCustomer(customerRepository.findById(customerId).orElse(null));
        CreatedOrderEvent oce = new CreatedOrderEvent();
        oce.setOrderId(order.getOrderId());
        oce.setOrderDate(order.getOrderDate());
        oce.setCustomer(order.getCustomer());
        kafkaTemplate.send("OrderCreated", oce);
        return orderRepository.save(order);
    }
}
