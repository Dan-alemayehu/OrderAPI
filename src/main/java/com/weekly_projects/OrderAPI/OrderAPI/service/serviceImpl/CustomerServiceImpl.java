package com.weekly_projects.OrderAPI.OrderAPI.service.serviceImpl;

import com.weekly_projects.OrderAPI.OrderAPI.model.Customer;
import com.weekly_projects.OrderAPI.OrderAPI.repository.CustomerRepository;
import com.weekly_projects.OrderAPI.OrderAPI.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CustomerServiceImpl implements CustomerService {

    public final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer findCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(()  -> new EntityNotFoundException("Customer with id " + id + " not found"));
    }

    @Override
    public Customer createCustomer(String name, String email) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        return customerRepository.save(customer);
    }
    @Override
    public Customer updateCustomer(Long id, String name, String email) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id " + id));
        existingCustomer.setName(name);
        existingCustomer.setEmail(email);
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return updatedCustomer;
    }
    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
