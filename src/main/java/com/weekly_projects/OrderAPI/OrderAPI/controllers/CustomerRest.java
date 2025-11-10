package com.weekly_projects.OrderAPI.OrderAPI.controllers;

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
    @PostMapping("/{name}/{email}")
    public ResponseEntity<Customer> createCustomer(@PathVariable String name, @PathVariable String email) {
        return new ResponseEntity<>(
                customerService.createCustomer(name, email),
                HttpStatus.CREATED
        );
    }

    //Put Mapping: Update an existing customer
    @PostMapping("/{id}/{name}/{email}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @PathVariable String name, @PathVariable String email) {
        Customer updatedCustomer = customerService.updateCustomer(id, name, email);
        return ResponseEntity.ok(updatedCustomer);
    }

    //Delete Mapping: Delete an existing customer
    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
