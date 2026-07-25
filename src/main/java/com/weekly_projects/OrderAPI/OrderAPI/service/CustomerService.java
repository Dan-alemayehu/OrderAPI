package com.weekly_projects.OrderAPI.OrderAPI.service;

import com.weekly_projects.OrderAPI.OrderAPI.dto.CustomerDto;
import com.weekly_projects.OrderAPI.OrderAPI.model.Customer;

import java.util.Optional;

public interface CustomerService {

    Customer createCustomer(CustomerDto customerDto);
    Customer findCustomerById(Long id);
    Customer updateCustomer(CustomerDto customerDto);
    void deleteCustomer(Long id);
}
