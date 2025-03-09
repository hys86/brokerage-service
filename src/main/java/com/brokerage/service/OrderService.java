package com.brokerage.service;

import com.brokerage.model.*;
import com.brokerage.repository.OrderRepository;
import com.brokerage.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AssetRepository assetRepository;

    public Order createOrder(Long customerId, String assetName, OrderSide side, Double size, Double price) {
        Asset tryAsset = assetRepository.findByCustomerIdAndAssetName(customerId, "TRY");
        Asset stockAsset = assetRepository.findByCustomerIdAndAssetName(customerId, assetName);

        if (side == OrderSide.BUY && tryAsset.getUsableSize() < size * price) {
            throw new IllegalArgumentException("Insufficient TRY balance.");
        }
        if (side == OrderSide.SELL && (stockAsset == null || stockAsset.getUsableSize() < size)) {
            throw new IllegalArgumentException("Insufficient asset balance.");
        }

        if (side == OrderSide.BUY) {
            tryAsset.setUsableSize(tryAsset.getUsableSize() - (size * price));
            assetRepository.save(tryAsset);
        } else {
            stockAsset.setUsableSize(stockAsset.getUsableSize() - size);
            assetRepository.save(stockAsset);
        }

        Order order = new Order();
        order.setCustomerId(customerId);
        order.setAssetName(assetName);
        order.setOrderSide(side);
        order.setSize(size);
        order.setPrice(price);
        order.setStatus(OrderStatus.PENDING);
        order.setCreateDate(LocalDateTime.now());
        return orderRepository.save(order);
    }

    public List<Order> getOrders(Long customerId, LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByCustomerIdAndCreateDateBetween(customerId, startDate, endDate);
    }

    public void deleteOrder(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new IllegalArgumentException("Order not found.");
        }

        Order order = orderOptional.get();
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Only pending orders can be canceled.");
        }

        Asset asset = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName());
        Asset tryAsset = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), "TRY");

        if (order.getOrderSide() == OrderSide.BUY) {
            tryAsset.setUsableSize(tryAsset.getUsableSize() + (order.getSize() * order.getPrice()));
        } else {
            asset.setUsableSize(asset.getUsableSize() + order.getSize());
        }

        assetRepository.save(tryAsset);
        assetRepository.save(asset);

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }
}
