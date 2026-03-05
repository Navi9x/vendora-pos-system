package com.vendora.vendorapos.entity;

import com.vendora.vendorapos.entity.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "inventory_transactions")
@Data
public class InventoryTransaction extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType; // IN, OUT, ADJUSTMENT, RETURN, TRANSFER

    @Column(nullable = false)
    private Double quantity;

    private Double balanceAfter;

    private String reason;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String referenceNumber; // PO number, invoice number, etc.
}