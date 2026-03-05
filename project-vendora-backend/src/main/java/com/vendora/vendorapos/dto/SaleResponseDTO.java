package com.vendora.vendorapos.dto;

import com.vendora.vendorapos.entity.enums.PaymentStatus;
import com.vendora.vendorapos.entity.enums.SaleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleResponseDTO {

    // From BaseEntity
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;

    // From Sale entity
    private String invoiceNumber;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discount;
    private BigDecimal deliveryCharge;
    private BigDecimal total;
    private SaleStatus status;
    private PaymentStatus paymentStatus;
    private String notes;

    // Nested objects - Summary DTOs
    private CustomerDTO customer;
    private UserDTO cashier;
    private BusinessDTO business;

    // Sale items with full details
    private List<SaleItemResponseDTO> items;

    // Computed fields for convenience
    private Integer totalItems;
    private Integer totalQuantity;
}
