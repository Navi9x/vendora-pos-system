package com.vendora.vendorapos.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_variants")
@Data
public class ProductVariant extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String variantName; // e.g., "Red - Large"

    private String sku;
    private String barcode;

    @Column(nullable = false)
    private Double price;

    // Variant attributes
    private String size;
    private String color;
    private String material;

    private Boolean isActive = true;
}
