package com.brokerage.controller;

import com.brokerage.model.Asset;
import com.brokerage.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assets")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @GetMapping("/{customerId}")
    public List<Asset> listAssets(@PathVariable Long customerId) {
        return assetService.getAssetsByCustomer(customerId);
    }
}
