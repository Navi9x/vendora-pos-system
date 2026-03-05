package com.vendora.vendorapos.entity;

import com.vendora.vendorapos.entity.enums.OrderType;
import com.vendora.vendorapos.entity.enums.PaymentStatus;
import com.vendora.vendorapos.entity.enums.SaleStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "sales",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_sale_business_invoice", columnNames = {"business_id", "invoice_number"})
        },
        indexes = {
                @Index(name = "idx_sale_business_date", columnList = "business_id, sale_date_time"),
                @Index(name = "idx_sale_location_date", columnList = "location_id, sale_date_time"),
                @Index(name = "idx_sale_cashier_date", columnList = "cashier_id, sale_date_time")
        }
)
@Data
public class Sale extends BusinessScopedEntity {

    @Column(name = "invoice_number", nullable = false)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cashier_id", nullable = false)
    private User cashier;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal subtotal;

    @Column(precision = 19, scale = 2)
    private BigDecimal taxAmount = new BigDecimal(0);

    @Column(precision = 19, scale = 2)
    private BigDecimal discount = new BigDecimal(0);

    @Column(precision = 19, scale = 2)
    private BigDecimal deliveryCharge = new BigDecimal(0);

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SaleStatus status = SaleStatus.COMPLETED;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PAID;

    private String notes;
}