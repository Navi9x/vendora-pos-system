package com.vendora.vendorapos.dto;

import java.time.LocalDateTime;

public record InventoryResponseDTO(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean isDeleted,

        Long businessId,

        Double quantity,
        Double minStockLevel,
        Double maxStockLevel,
        LocalDateTime lastRestocked,

        ProductSummaryDTO product
) {

}
