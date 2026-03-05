package com.vendora.vendorapos.controller;

import com.vendora.vendorapos.dto.InventoryQuery;
import com.vendora.vendorapos.dto.InventoryResponseDTO;
import com.vendora.vendorapos.entity.Inventory;
import com.vendora.vendorapos.security.tenant.TenantContext;
import com.vendora.vendorapos.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/inventory")
@Validated
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/loadAll")
    public List<InventoryResponseDTO> loadAll(@Valid InventoryQuery query) {
        System.out.println("loadAll");
        return inventoryService.getInventory(TenantContext.getTenantId(),query);
    }
}
