package com.brokerage.controller;

import com.brokerage.model.Customer;
import com.brokerage.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        Optional<Customer> customer = customerService.authenticate(username, password);
        return customer.isPresent()
                ? ResponseEntity.ok("Login successful. User ID: " + customer.get().getId())
                : ResponseEntity.status(401).body("Invalid credentials");
    }
}
