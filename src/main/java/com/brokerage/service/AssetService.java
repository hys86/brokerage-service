package com.brokerage.service;

import com.brokerage.model.Asset;
import com.brokerage.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;

    public List<Asset> getAssetsByCustomer(Long customerId) {
        return assetRepository.findByCustomerId(customerId);
    }

    public Asset getAsset(Long customerId, String assetName) {
        return assetRepository.findByCustomerIdAndAssetName(customerId, assetName);
    }

    public void updateAsset(Asset asset) {
        assetRepository.save(asset);
    }
}
