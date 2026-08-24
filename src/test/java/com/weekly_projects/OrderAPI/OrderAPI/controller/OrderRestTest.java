package com.weekly_projects.OrderAPI.OrderAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weekly_projects.OrderAPI.OrderAPI.controllers.OrderRest;
import com.weekly_projects.OrderAPI.OrderAPI.dto.OrderDto;
import com.weekly_projects.OrderAPI.OrderAPI.model.Customer;
import com.weekly_projects.OrderAPI.OrderAPI.model.Order;
import com.weekly_projects.OrderAPI.OrderAPI.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderRest.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderRestTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @Test
    void findOrderById_whenOrderExists_returnsOk() throws Exception {
        Customer customer = createCustomer();

        Order order = new Order();
        order.setOrderId(100L);
        order.setOrderDate(Instant.parse("2026-07-25T03:48:48Z"));
        order.setCustomer(customer);

        when(orderService.findOrderById(100L)).thenReturn(order);

        mockMvc.perform(get("/orders/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(100));

        verify(orderService).findOrderById(100L);
    }

    @Test
    void createOrder_whenValidRequest_returnsCreated() throws Exception {
        OrderDto request = new OrderDto();
        request.setCustomerId(1L);

        Customer customer = createCustomer();

        Order createdOrder = new Order();
        createdOrder.setOrderId(100L);
        createdOrder.setOrderDate(Instant.parse("2026-07-25T03:48:48Z"));
        createdOrder.setCustomer(customer);

        when(orderService.createOrder(any(OrderDto.class))).thenReturn(createdOrder);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value(100));

        verify(orderService).createOrder(any(OrderDto.class));
    }

    @Test
    void createOrder_whenRequestBodyIsMissing_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createOrder_whenJsonIsMalformed_returnsBadRequest() throws Exception {
        String malformedJson = """
                {
                    "customerId":
                }
                """;

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest());
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