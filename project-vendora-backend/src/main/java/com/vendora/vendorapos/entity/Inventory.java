package com.vendora.vendorapos.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Data
public class Inventory extends BusinessScopedEntity {

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(nullable = false)
    private Double quantity = 0.0; // Using Double for weight-based items

    private Double minStockLevel;
    private Double maxStockLevel;

    private LocalDateTime lastRestocked;
}
