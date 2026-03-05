package com.vendora.vendorapos.dto;

import java.time.LocalDate;

public record ProductSummaryDTO(
        Long id,
        String name,
        String model,
        String sku,
        String barcode,

        Double price,
        Double costPrice,
        String image,

        String unitType,          // store as String for frontend friendliness
        Boolean isTaxable,

        // Pharmacy fields (optional)
        String genericName,
        String manufacturer,
        LocalDate expiryDate,
        String batchNumber,
        Boolean requiresPrescription,
        Boolean isActive,

        CategoryDTO category
) {

}
