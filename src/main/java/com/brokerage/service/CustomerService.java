package com.brokerage.service;

import com.brokerage.model.Customer;
import com.brokerage.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<Customer> authenticate(String username, String password) {
        Optional<Customer> customer = customerRepository.findByUsername(username);
        return customer.filter(c -> passwordEncoder.matches(password, c.getPassword()));
    }

    public boolean isAdmin(Long customerId) {
        return customerRepository.findById(customerId)
                .map(Customer::isAdmin)
                .orElse(false);
    }
}
