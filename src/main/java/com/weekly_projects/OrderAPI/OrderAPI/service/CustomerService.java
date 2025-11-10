package com.weekly_projects.OrderAPI.OrderAPI.service;

import com.weekly_projects.OrderAPI.OrderAPI.model.Customer;

public interface CustomerService {

    Customer createCustomer(String name, String email);
    Customer findCustomerById(Long id);
    Customer updateCustomer(Long id, String name, String email);
    void deleteCustomer(Long id);
}
