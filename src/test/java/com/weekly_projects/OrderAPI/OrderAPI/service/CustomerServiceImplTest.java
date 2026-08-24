package com.weekly_projects.OrderAPI.OrderAPI.service;

import com.weekly_projects.OrderAPI.OrderAPI.dto.CustomerDto;
import com.weekly_projects.OrderAPI.OrderAPI.event.CreatedCustomerEvent;
import com.weekly_projects.OrderAPI.OrderAPI.model.Customer;
import com.weekly_projects.OrderAPI.OrderAPI.repository.CustomerRepository;
import com.weekly_projects.OrderAPI.OrderAPI.service.serviceImpl.CustomerCreatedEventProducer;
import com.weekly_projects.OrderAPI.OrderAPI.service.serviceImpl.CustomerServiceImpl;
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
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerCreatedEventProducer customerCreatedEventProducer;

    @InjectMocks
    private CustomerServiceImpl customerService;

    //Test for finding a customer by customer ID
    @Test
    void findCustomerById_whenCustomerExists_returnsCustomer() {
        Customer customer = createCustomer();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = customerService.findCustomerById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Daniel Alemayehu", result.getName());
        assertEquals("daniel@example.com", result.getEmail());

        verify(customerRepository).findById(1L);
    }

    //Test for Customer not found exception
    @Test
    void findCustomerById_whenCustomerDoesNotExist_throwsEntityNotFoundException() {
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> customerService.findCustomerById(999L)
        );

        assertEquals("Customer with id 999 not found", exception.getMessage());
        verify(customerRepository).findById(999L);
    }

    //Test for saving a customer with the proper name and email
    @Test
    void createCustomer_savesCustomerWithNameEmailAndCreatedAt() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("Daniel Alemayehu");
        customerDto.setEmail("daniel@example.com");

        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer customerToSave = invocation.getArgument(0);
            customerToSave.setId(1L);
            return customerToSave;
        });

        customerService.createCustomer(customerDto);

        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerRepository).save(customerCaptor.capture());

        Customer savedCustomer = customerCaptor.getValue();

        assertEquals("Daniel Alemayehu", savedCustomer.getName());
        assertEquals("daniel@example.com", savedCustomer.getEmail());
        assertNotNull(savedCustomer.getCreatedAt());
    }

    //Test for creating a customer event when a customer is created
    @Test
    void createCustomer_publishesCreatedCustomerEvent() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("Daniel Alemayehu");
        customerDto.setEmail("daniel@example.com");

        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer customerToSave = invocation.getArgument(0);
            customerToSave.setId(1L);
            return customerToSave;
        });

        customerService.createCustomer(customerDto);

        ArgumentCaptor<CreatedCustomerEvent> eventCaptor =
                ArgumentCaptor.forClass(CreatedCustomerEvent.class);

        verify(customerCreatedEventProducer).sendCustomerCreatedEvent(eventCaptor.capture());

        CreatedCustomerEvent event = eventCaptor.getValue();

        assertNotNull(event.getEventId());
        assertEquals(1, event.getEventVersion());
        assertNotNull(event.getOccurredAt());
        assertEquals(1L, event.getCustomerId());
        assertEquals("daniel@example.com", event.getEmail());
        assertEquals("Daniel Alemayehu", event.getName());
    }

    //Test to make sure saved customers make it to the repository
    @Test
    void createCustomer_returnsSavedCustomer() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("Daniel Alemayehu");
        customerDto.setEmail("daniel@example.com");

        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer customerToSave = invocation.getArgument(0);
            customerToSave.setId(1L);
            return customerToSave;
        });

        Customer result = customerService.createCustomer(customerDto);

        assertEquals(1L, result.getId());
        assertEquals("Daniel Alemayehu", result.getName());
        assertEquals("daniel@example.com", result.getEmail());
        assertNotNull(result.getCreatedAt());
    }

    //Test for updating a customer
    @Test
    void updateCustomer_whenCustomerExists_updatesCustomerAndPublishesEvent() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(1L);
        customerDto.setName("Updated Daniel");
        customerDto.setEmail("updated@example.com");

        Customer existingCustomer = createCustomer();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(existingCustomer)).thenReturn(existingCustomer);

        Customer result = customerService.updateCustomer(customerDto);

        assertEquals(1L, result.getId());
        assertEquals("Updated Daniel", result.getName());
        assertEquals("updated@example.com", result.getEmail());

        verify(customerRepository).findById(1L);
        verify(customerRepository).save(existingCustomer);

        ArgumentCaptor<CreatedCustomerEvent> eventCaptor =
                ArgumentCaptor.forClass(CreatedCustomerEvent.class);

        verify(customerCreatedEventProducer).sendCustomerCreatedEvent(eventCaptor.capture());

        CreatedCustomerEvent event = eventCaptor.getValue();

        assertNotNull(event.getEventId());
        assertEquals(1, event.getEventVersion());
        assertNotNull(event.getOccurredAt());
        assertEquals(1L, event.getCustomerId());
        assertEquals("updated@example.com", event.getEmail());
        assertEquals("Updated Daniel", event.getName());
    }

    //Testing the Customer not found exception when trying to
    //update a customer that does not exist
    @Test
    void updateCustomer_whenCustomerDoesNotExist_throwsEntityNotFoundException() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(999L);
        customerDto.setName("Missing Customer");
        customerDto.setEmail("missing@example.com");

        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> customerService.updateCustomer(customerDto)
        );

        assertEquals("Customer not found", exception.getMessage());

        verify(customerRepository).findById(999L);
        verify(customerRepository, never()).save(any(Customer.class));
        verify(customerCreatedEventProducer, never()).sendCustomerCreatedEvent(any(CreatedCustomerEvent.class));
    }

    //Test for deleting a customer
    @Test
    void deleteCustomer_deletesCustomerById() {
        customerService.deleteCustomer(1L);

        verify(customerRepository).deleteById(1L);
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