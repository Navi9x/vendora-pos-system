package com.vendora.vendorapos.entity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_sales_summary")
@Data
public class DailySalesSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long businessId;

    @Column(nullable = false)
    private LocalDate summaryDate;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    // Transaction Summary
    private Integer totalTransactions = 0;
    private Integer totalItems = 0;
    private Double totalSales = 0.0;
    private Double totalTax = 0.0;
    private Double totalDiscount = 0.0;
    private Double netSales = 0.0;

    // Payment Method Breakdown
    private Double cashSales = 0.0;
    private Double cardSales = 0.0;
    private Double digitalSales = 0.0;

    // Customer Analytics
    private Integer newCustomers = 0;
    private Integer returningCustomers = 0;

    // Restaurant specific
    private Integer totalCovers = 0; // Number of guests served
    private Double averageCheckSize = 0.0;

    @CreationTimestamp
    private LocalDateTime createdAt;
}

