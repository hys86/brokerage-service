package com.brokerage.controller;

import com.brokerage.model.Order;
import com.brokerage.model.OrderSide;
import com.brokerage.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public Order createOrder(
            @RequestParam Long customerId,
            @RequestParam String assetName,
            @RequestParam OrderSide side,
            @RequestParam Double size,
            @RequestParam Double price) {
        return orderService.createOrder(customerId, assetName, side, size, price);
    }

    @GetMapping("/list")
    public List<Order> listOrders(
            @RequestParam Long customerId,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        return orderService.getOrders(customerId, startDate, endDate);
    }

    @DeleteMapping("/delete/{orderId}")
    public String deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return "Order deleted successfully.";
    }
}
