package com.brokerage.service;

import com.brokerage.model.Customer;
import com.brokerage.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticate_Success() {
        Customer mockCustomer = new Customer(1L, "testUser", "testPass");
        when(customerRepository.findByUsernameAndPassword("testUser", "testPass"))
                .thenReturn(Optional.of(mockCustomer));

        Optional<Customer> result = customerService.authenticate("testUser", "testPass");

        assertTrue(result.isPresent());
        assertEquals("testUser", result.get().getUsername());
    }

    @Test
    void testAuthenticate_Failure() {
        when(customerRepository.findByUsernameAndPassword("wrongUser", "wrongPass"))
                .thenReturn(Optional.empty());

        Optional<Customer> result = customerService.authenticate("wrongUser", "wrongPass");

        assertFalse(result.isPresent());
    }
}
