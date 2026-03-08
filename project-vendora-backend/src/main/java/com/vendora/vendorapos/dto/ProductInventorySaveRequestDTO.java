package com.vendora.vendorapos.dto;

import lombok.Data;

@Data
public class ProductInventorySaveRequestDTO {

    private ProductSummaryDTO product;
    private InventorySaveDTO inventory;

    public ProductSummaryDTO getProduct() {
        return product;
    }

    public void setProduct(ProductSummaryDTO product) {
        this.product = product;
    }

    public InventorySaveDTO getInventory() {
        return inventory;
    }

    public void setInventory(InventorySaveDTO inventory) {
        this.inventory = inventory;
    }
}
