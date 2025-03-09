package com.brokerage.service;

import com.brokerage.model.Asset;
import com.brokerage.repository.AssetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetService assetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAssetsByCustomer() {
        List<Asset> mockAssets = Arrays.asList(
                new Asset(1L, "TRY", 10000.0, 5000.0),
                new Asset(1L, "AAPL", 50.0, 30.0)
        );

        when(assetRepository.findByCustomerId(1L)).thenReturn(mockAssets);

        List<Asset> result = assetService.getAssetsByCustomer(1L);

        assertEquals(2, result.size());
        assertEquals("TRY", result.get(0).getAssetName());
        assertEquals("AAPL", result.get(1).getAssetName());
    }
}
