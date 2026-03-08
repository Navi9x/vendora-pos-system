package com.vendora.vendorapos.service;

import com.vendora.vendorapos.dto.*;
import com.vendora.vendorapos.entity.Business;
import com.vendora.vendorapos.entity.Category;
import com.vendora.vendorapos.entity.Inventory;
import com.vendora.vendorapos.entity.Product;
import com.vendora.vendorapos.entity.enums.StockStatus;
import com.vendora.vendorapos.entity.enums.UnitType;
import com.vendora.vendorapos.repo.BusinessRepo;
import com.vendora.vendorapos.repo.CategoryRepo;
import com.vendora.vendorapos.repo.InventoryRepo;
import com.vendora.vendorapos.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Autowired
    private CategoryRepo categoryRepo;

    private InventoryQuery query;

    public void reduceStock(Long tenantId, Long id,Integer quantity) {
        Business business = businessRepo.findById(tenantId).get();
        Product product = productRepo.findById(id).get();
        Inventory inventory = inventoryRepo.findByProductAndBusiness(product, business);
        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepo.save(inventory);
    }

    public List<InventoryResponseDTO> getInventory(Long tenantId,InventoryQuery query) {
        boolean outOfStock = query.getStockStatus() == StockStatus.OUT_OF_STOCK;
        boolean lowStock = query.getStockStatus() == StockStatus.LOW_STOCK;
        boolean inStock = query.getStockStatus() == StockStatus.IN_STOCK;

        this.query = query;
        if(query.getDate()!=null){
            query.setDate(query.getDate().plusDays(1));
        }
        Page<Inventory> inventoryList = inventoryRepo.searchInventory(
                tenantId,
                query.getSearchText(),
                query.getCategoryId(),
                query.getDate(),
                query.getStockStatus() == null,
                outOfStock,
                lowStock,
                inStock,
                PageRequest.of(query.getPage(),query.getSize())
        );
        List<InventoryResponseDTO> inventoryResponseDTOList = new ArrayList<>();
        for (Inventory inventory : inventoryList) {
            inventoryResponseDTOList.add(toInventoryDTO(inventory, tenantId));
        }
        return inventoryResponseDTOList;
    }

    public List<InventoryResponseDTO> getAllInventory(Long tenantId) {
        List<Inventory> inventoryList = inventoryRepo.findAllByBusiness(businessRepo.findById(tenantId).get());
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

    public void saveInventoryAndProduct(ProductInventorySaveRequestDTO request, Long tenantId) {
        ProductSummaryDTO productDTO = request.getProduct();
        InventorySaveDTO inventoryDTO = request.getInventory();
        CategoryDTO categoryDTO = productDTO.category();
        Category category = categoryRepo.findById(categoryDTO.id())
                .orElseThrow(()->new RuntimeException("Category not found"));
        Business business = businessRepo.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        Product product = new Product();

        product.setName(productDTO.name());
        product.setModel(productDTO.model());
        product.setSku(productDTO.sku());
        product.setBarcode(productDTO.barcode());
        product.setPrice(productDTO.price());
        product.setCostPrice(productDTO.costPrice());
        product.setImage(productDTO.image());
        product.setCategory(category);
        product.setIsTaxable(productDTO.isTaxable());
        product.setGenericName(productDTO.genericName());
        product.setManufacturer(productDTO.manufacturer());
        product.setExpiryDate(productDTO.expiryDate());
        product.setBatchNumber(productDTO.batchNumber());
        product.setRequiresPrescription(productDTO.requiresPrescription());
        product.setIsActive(productDTO.isActive());
        product.setUnitType(productDTO.unitType() !=null ? UnitType.valueOf(productDTO.unitType()):null);
        product.setBusiness(business);
        productRepo.save(product);

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setBusiness(business);
        inventory.setQuantity(inventoryDTO.getQuantity() != null ? Double.valueOf(inventoryDTO.getQuantity()):0);
        inventory.setMinStockLevel(inventoryDTO.getMinStockLevel() != null ? Double.valueOf(inventoryDTO.getMinStockLevel()) : 0);
        inventory.setMaxStockLevel( inventoryDTO.getMinStockLevel() != null ? Double.valueOf(inventoryDTO.getMinStockLevel()):0);
        inventory.setLastRestocked(
                inventoryDTO.getLastRestocked() != null
                        ? inventoryDTO.getLastRestocked().atStartOfDay()
                        : null
        );

        inventoryRepo.save(inventory);

    }
}
