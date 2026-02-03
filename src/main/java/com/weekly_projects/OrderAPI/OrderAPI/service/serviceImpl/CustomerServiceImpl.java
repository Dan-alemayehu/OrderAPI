package com.weekly_projects.OrderAPI.OrderAPI.service.serviceImpl;

import com.weekly_projects.OrderAPI.OrderAPI.dto.CustomerDto;
import com.weekly_projects.OrderAPI.OrderAPI.event.CreatedCustomerEvent;
import com.weekly_projects.OrderAPI.OrderAPI.model.Customer;
import com.weekly_projects.OrderAPI.OrderAPI.repository.CustomerRepository;
import com.weekly_projects.OrderAPI.OrderAPI.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    public final CustomerRepository customerRepository;

    private final CustomerCreatedEventProducer customerCreatedEventProducer;

    public Customer findCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(()  -> new EntityNotFoundException("Customer with id " + id + " not found"));
    }

    @Override
    public Customer createCustomer(CustomerDto  customerDto) {
        Customer customer = new Customer();
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setCreatedAt(Instant.now());
        customerRepository.save(customer);
        CreatedCustomerEvent cce = new CreatedCustomerEvent();
        cce.setEventId(UUID.randomUUID().toString());
        cce.setEventVersion(1);
        cce.setOccurredAt(Instant.now());
        cce.setCustomerId(customer.getId());
        cce.setEmail(customer.getEmail());
        cce.setName(customer.getName());
        customerCreatedEventProducer.sendCustomerCreatedEvent(cce);
        return customer;
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
