package com.vendora.vendorapos.dto;

import com.vendora.vendorapos.entity.enums.OrderType;
import com.vendora.vendorapos.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateSaleRequestDTO {
    // Business / Tenant
    @NotNull
    private Long businessId;

    // Branch
    @NotNull
    private Long locationId;

    // Logged-in cashier
    @NotNull
    private Long cashierId;

    // Optional customer (walk-in allowed)
    private Long customerId;

    // Restaurant table (only for DINE_IN)
    private Long tableId;

    // Invoice number generated on frontend or backend
    @NotBlank
    private String invoiceNumber;

    // Sale time (frontend or backend)
    @NotNull
    private LocalDateTime saleDateTime;

    // Money
    @NotNull
    private BigDecimal subtotal;

    private BigDecimal taxAmount = BigDecimal.ZERO;

    private BigDecimal discount = BigDecimal.ZERO;

    private BigDecimal deliveryCharge = BigDecimal.ZERO;

    private BigDecimal serviceCharge = BigDecimal.ZERO;

    @NotNull
    private BigDecimal total;

    // Enums
    @NotNull
    private PaymentStatus paymentStatus;

    private OrderType orderType; // DINE_IN / TAKEAWAY / DELIVERY / ONLINE

    // Optional
    private String notes;

    // Line items
    @NotEmpty
    private List<SaleItemRequestDTO> items;
}
