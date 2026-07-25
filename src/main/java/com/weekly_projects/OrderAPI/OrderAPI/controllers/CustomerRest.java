package com.weekly_projects.OrderAPI.OrderAPI.controllers;

import com.weekly_projects.OrderAPI.OrderAPI.dto.CustomerDto;
import com.weekly_projects.OrderAPI.OrderAPI.model.Customer;
import com.weekly_projects.OrderAPI.OrderAPI.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/customers")
@Slf4j
public class CustomerRest {

    private final CustomerService customerService;

    @Autowired
    public CustomerRest(CustomerService customerService) {this.customerService = customerService;}

    //Get Mapping: retrieve customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Customer> findCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.findCustomerById(id));
    }

    //Post Mapping: Create a new customer
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody CustomerDto customer) {
        return ResponseEntity.ok(customerService.createCustomer(customer));
    }

    //Put Mapping: Update an existing customer
    @PutMapping
    public ResponseEntity<Customer> updateCustomer(@RequestBody CustomerDto customer) {
        return ResponseEntity.ok(customerService.updateCustomer(customer));
    }

    //Delete Mapping: Delete an existing customer
    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
