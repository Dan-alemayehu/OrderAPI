package com.weekly_projects.OrderAPI.OrderAPI.service.serviceImpl;

import com.weekly_projects.OrderAPI.OrderAPI.dto.OrderDto;
import com.weekly_projects.OrderAPI.OrderAPI.event.CreatedOrderEvent;
import com.weekly_projects.OrderAPI.OrderAPI.model.Customer;
import com.weekly_projects.OrderAPI.OrderAPI.model.Order;
import com.weekly_projects.OrderAPI.OrderAPI.repository.CustomerRepository;
import com.weekly_projects.OrderAPI.OrderAPI.repository.OrderRepository;
import com.weekly_projects.OrderAPI.OrderAPI.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    public final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    private final OrderCreatedEventProducer orderCreatedEventProducer;


    @Override
    public Order findOrderById(long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with id " + id + " not found"));
    }

    @Override
    public Order createOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setOrderDate(Instant.now());
        Customer customer = customerRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer with id " + orderDto.getCustomerId() + " not found"
                ));

        order.setCustomer(customer);
        orderRepository.save(order);
        CreatedOrderEvent oce = new CreatedOrderEvent();
        oce.setEventId(UUID.randomUUID().toString());
        oce.setEventVersion(1);
        oce.setOccurredAt(Instant.now());
        oce.setOrderId(order.getOrderId());
        oce.setOrderDate(order.getOrderDate());
        oce.setCustomerId(order.getCustomer().getId());
        orderCreatedEventProducer.sendOrderCreatedEvent(oce);
        return order;
    }
}
