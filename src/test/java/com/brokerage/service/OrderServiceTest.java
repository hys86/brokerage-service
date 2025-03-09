package com.brokerage.service;

import com.brokerage.model.*;
import com.brokerage.repository.AssetRepository;
import com.brokerage.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder_Success() {
        Asset mockAsset = new Asset(1L, "TRY", 10000.0, 5000.0);
        when(assetRepository.findByCustomerIdAndAssetName(1L, "TRY")).thenReturn(mockAsset);

        Order mockOrder = new Order(1L, "AAPL", OrderSide.BUY, 10.0, 150.0, OrderStatus.PENDING);
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        Order result = orderService.createOrder(1L, "AAPL", OrderSide.BUY, 10.0, 150.0);

        assertNotNull(result);
        assertEquals(OrderStatus.PENDING, result.getStatus());
        verify(assetRepository, times(1)).save(mockAsset);
    }

    @Test
    void testCancelOrder_Success() {
        Order mockOrder = new Order(1L, "AAPL", OrderSide.BUY, 10.0, 150.0, OrderStatus.PENDING);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));

        Asset mockAsset = new Asset(1L, "TRY", 10000.0, 5000.0);
        when(assetRepository.findByCustomerIdAndAssetName(1L, "TRY")).thenReturn(mockAsset);

        orderService.deleteOrder(1L);

        assertEquals(OrderStatus.CANCELED, mockOrder.getStatus());
        verify(orderRepository, times(1)).save(mockOrder);
        verify(assetRepository, times(1)).save(mockAsset);
    }
}
