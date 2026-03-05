package com.vendora.vendorapos.service;

import com.vendora.vendorapos.dto.CategoryDTO;
import com.vendora.vendorapos.dto.InventoryQuery;
import com.vendora.vendorapos.dto.InventoryResponseDTO;
import com.vendora.vendorapos.dto.ProductSummaryDTO;
import com.vendora.vendorapos.entity.Business;
import com.vendora.vendorapos.entity.Category;
import com.vendora.vendorapos.entity.Inventory;
import com.vendora.vendorapos.entity.Product;
import com.vendora.vendorapos.repo.BusinessRepo;
import com.vendora.vendorapos.repo.InventoryRepo;
import com.vendora.vendorapos.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private BusinessRepo businessRepo;

    @Autowired
    private InventoryRepo inventoryRepo;

    private InventoryQuery query;

    public void reduceStock(Long tenantId, Long id,Integer quantity) {
        Business business = businessRepo.findById(tenantId).get();
        Product product = productRepo.findById(id).get();
        Inventory inventory = inventoryRepo.findByProductAndBusiness(product, business);
        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepo.save(inventory);
    }

    public List<InventoryResponseDTO> getInventory(Long tenantId,InventoryQuery query) {
        this.query = query;
        Page<Inventory> inventoryList = inventoryRepo.searchInventory(tenantId,query.getSearchText(),query.getCategoryId(),query.getStockStatus(),query.getDate(), PageRequest.of(query.getPage(),query.getSize()));
        List<InventoryResponseDTO> inventoryResponseDTOList = new ArrayList<>();
        for (Inventory inventory : inventoryList) {
            inventoryResponseDTOList.add(toInventoryDTO(inventory, tenantId));
        }
        return inventoryResponseDTOList;
    }

    public InventoryResponseDTO toInventoryDTO(Inventory inventory, Long tenantId) {
            Product product = inventory.getProduct();
            Category category = product.getCategory();
            CategoryDTO categoryDTO = new CategoryDTO(
                    category.getId(),
                    category.getName()
            );
            ProductSummaryDTO productSummaryDTO = new ProductSummaryDTO(
                    product.getId(),
                    product.getName(),
                    product.getModel(),
                    product.getSku(),
                    product.getBarcode(),
                    product.getPrice(),
                    product.getCostPrice(),
                    product.getImage(),
                    product.getUnitType()!=null?product.getUnitType().name():null,
                    product.getIsTaxable(),
                    product.getGenericName(),
                    product.getManufacturer(),
                    product.getExpiryDate(),
                    product.getBatchNumber(),
                    product.getRequiresPrescription(),
                    product.getIsActive(),
                    categoryDTO
            );
        return
                new InventoryResponseDTO(
                inventory.getId(),
                inventory.getCreatedAt(),
                inventory.getUpdatedAt(),
                inventory.getIsDeleted(),
                tenantId,
                inventory.getQuantity(),
                inventory.getMinStockLevel(),
                inventory.getMaxStockLevel(),
                inventory.getLastRestocked(),
                productSummaryDTO
        );
    }
}
