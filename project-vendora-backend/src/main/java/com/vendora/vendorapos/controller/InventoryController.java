package com.vendora.vendorapos.controller;

import com.vendora.vendorapos.dto.InventoryQuery;
import com.vendora.vendorapos.dto.InventoryResponseDTO;
import com.vendora.vendorapos.dto.ProductInventorySaveRequestDTO;
import com.vendora.vendorapos.security.tenant.TenantContext;
import com.vendora.vendorapos.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/inventory")
@Validated
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/loadAll")
    public List<InventoryResponseDTO> loadAll(@Valid InventoryQuery query) {
        return inventoryService.getInventory(TenantContext.getTenantId(),query);
    }

    @GetMapping("/loadAllInventory")
    public List<InventoryResponseDTO> loadAllInventory(@Valid InventoryQuery query) {
        return inventoryService.getAllInventory(TenantContext.getTenantId());
    }

    @PostMapping("/saveProduct")
    public ResponseEntity<?> saveProduct(@RequestBody ProductInventorySaveRequestDTO request){
        inventoryService.saveInventoryAndProduct(request,TenantContext.getTenantId());
        return ResponseEntity.ok().build();
    }
}
