package com.vendora.vendorapos.entity;

import com.vendora.vendorapos.entity.enums.ProductType;
import com.vendora.vendorapos.entity.enums.UnitType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;


@Entity
@Table(name = "products")
@Data
public class Product extends BusinessScopedEntity {

    @Column(nullable = false)
    private String name;

    private String model;

    @Column(unique = true)
    private String sku;

    private String barcode;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private Double price;

    private Double costPrice;

    private String image;


    // Grocery specific
    @Enumerated(EnumType.STRING)
    private UnitType unitType; // PIECE, KG, GRAM, LITER, ML

    private Boolean isTaxable = false;

    // Pharmacy specific
    private String genericName;
    private String manufacturer;
    private LocalDate expiryDate;
    private String batchNumber;
    private Boolean requiresPrescription = false;
    private Boolean isActive = true;

}
