package com.vendora.vendorapos.dto;

import java.time.LocalDateTime;

public record InventoryDTO(
        Long id,
        LocalDateTime date,
        int maxStockLevel,
        int minStockLevel,
        int qty,
        ProductDTO productDTO
) {
}
