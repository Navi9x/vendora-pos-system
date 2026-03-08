package com.vendora.vendorapos.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InventorySaveDTO {
    private ProductSummaryDTO product;
    private Integer quantity;
    private Integer minStockLevel;
    private Integer maxStockLevel;
    private LocalDate lastRestocked;
}
