package com.weekly_projects.OrderAPI.OrderAPI.service;

import com.weekly_projects.OrderAPI.OrderAPI.dto.OrderDto;
import com.weekly_projects.OrderAPI.OrderAPI.event.CreatedOrderEvent;
import com.weekly_projects.OrderAPI.OrderAPI.model.Customer;
import com.weekly_projects.OrderAPI.OrderAPI.model.Order;
import com.weekly_projects.OrderAPI.OrderAPI.repository.CustomerRepository;
import com.weekly_projects.OrderAPI.OrderAPI.repository.OrderRepository;
import com.weekly_projects.OrderAPI.OrderAPI.service.serviceImpl.OrderCreatedEventProducer;
import com.weekly_projects.OrderAPI.OrderAPI.service.serviceImpl.OrderServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderCreatedEventProducer orderCreatedEventProducer;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void findOrderById_whenOrderExists_returnsOrder() {
        Order order = new Order();
        order.setOrderId(100L);

        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));

        Order result = orderService.findOrderById(100L);

        assertEquals(100L, result.getOrderId());
        verify(orderRepository).findById(100L);
    }

    @Test
    void findOrderById_whenOrderDoesNotExist_throwsEntityNotFoundException() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> orderService.findOrderById(999L)
        );

        assertEquals("Order with id 999 not found", exception.getMessage());
        verify(orderRepository).findById(999L);
    }

    @Test
    void createOrder_whenCustomerExists_savesOrderWithCustomerAndOrderDate() {
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(1L);

        Customer customer = createCustomer();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order orderToSave = invocation.getArgument(0);
            orderToSave.setOrderId(100L);
            return orderToSave;
        });

        orderService.createOrder(orderDto);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();

        assertEquals(customer, savedOrder.getCustomer());
        assertNotNull(savedOrder.getOrderDate());
        assertEquals(100L, savedOrder.getOrderId());
    }

    @Test
    void createOrder_whenCustomerExists_publishesCreatedOrderEvent() {
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(1L);

        Customer customer = createCustomer();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order orderToSave = invocation.getArgument(0);
            orderToSave.setOrderId(100L);
            return orderToSave;
        });

        orderService.createOrder(orderDto);

        ArgumentCaptor<CreatedOrderEvent> eventCaptor =
                ArgumentCaptor.forClass(CreatedOrderEvent.class);

        verify(orderCreatedEventProducer).sendOrderCreatedEvent(eventCaptor.capture());

        CreatedOrderEvent event = eventCaptor.getValue();

        assertNotNull(event.getEventId());
        assertEquals(1, event.getEventVersion());
        assertNotNull(event.getOccurredAt());
        assertEquals(100L, event.getOrderId());
        assertNotNull(event.getOrderDate());
        assertEquals(1L, event.getCustomerId());
    }

    @Test
    void createOrder_whenCustomerExists_returnsSavedOrder() {
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(1L);

        Customer customer = createCustomer();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order orderToSave = invocation.getArgument(0);
            orderToSave.setOrderId(100L);
            return orderToSave;
        });

        Order result = orderService.createOrder(orderDto);

        assertEquals(100L, result.getOrderId());
        assertEquals(customer, result.getCustomer());
        assertNotNull(result.getOrderDate());
    }

    @Test
    void createOrder_whenCustomerDoesNotExist_throwsEntityNotFoundException() {
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(999L);

        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> orderService.createOrder(orderDto)
        );

        verify(orderRepository, never()).save(any(Order.class));
        verify(orderCreatedEventProducer, never()).sendOrderCreatedEvent(any(CreatedOrderEvent.class));
    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Daniel Alemayehu");
        customer.setEmail("daniel@example.com");
        customer.setCreatedAt(Instant.parse("2026-07-25T03:48:48Z"));
        return customer;
    }
}