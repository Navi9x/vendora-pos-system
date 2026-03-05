package com.vendora.vendorapos.dto;

import java.time.LocalDateTime;

public record InventoryUpsertDTO(
        Long productId,
        Double quantity,
        Double minStockLevel,
        Double maxStockLevel,
        LocalDateTime lastRestocked
) {
}
