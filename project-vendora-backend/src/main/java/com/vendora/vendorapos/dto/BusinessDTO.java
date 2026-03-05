package com.vendora.vendorapos.dto;

import java.time.LocalDateTime;

public record BusinessDTO(
        Long id,
        String businessName,
        String businessCode,
        String systemType,
        String subscriptionPlan,
        Boolean isActive,
        LocalDateTime subscriptionStartDate,
        LocalDateTime subscriptionExpiryDate,
        Integer maxUsers,
        Integer maxProducts,
        Integer maxLocations,
        LocalDateTime createdAt
) {
}
