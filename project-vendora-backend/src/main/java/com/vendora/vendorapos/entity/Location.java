package com.vendora.vendorapos.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "locations")
@Data
public class Location extends BusinessScopedEntity {

    @Column(nullable = false)
    private String locationName;

    private String address;
    private String city;
    private String postalCode;
    private String phone;

    @Column(nullable = false)
    private Boolean isMain = false; // Main branch

    @Column(nullable = false)
    private Boolean isActive = true;
}
