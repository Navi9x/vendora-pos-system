package com.vendora.vendorapos.dto;

import com.vendora.vendorapos.entity.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateSaleDTO {
    private String invoiceNumber;

    private int customerId;

    private List<SaleItemRequestDTO> items;

    private BigDecimal subtotal;

    private BigDecimal taxAmount;

    private BigDecimal discount;

    private BigDecimal deliveryCharge;

    private BigDecimal serviceCharge;

    private BigDecimal total;

    private String notes;

    private PaymentStatus paymentStatus;
}
