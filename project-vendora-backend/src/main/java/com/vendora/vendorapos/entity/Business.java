package com.vendora.vendorapos.entity;
import com.vendora.vendorapos.entity.enums.SubscriptionPlan;
import com.vendora.vendorapos.entity.enums.SystemType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "businesses")
@Data
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String businessName;

    @Column(unique = true, nullable = false)
    private String businessCode; // Unique identifier

    // System Type - Determines which UI and features they get
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SystemType systemType; // RESTAURANT, RETAIL, GROCERY, PHARMACY, SERVICE

    // Subscription Management
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionPlan subscriptionPlan; // BASIC, PROFESSIONAL, ENTERPRISE

    @Column(nullable = false)
    private Boolean isActive = true;

    private LocalDateTime subscriptionStartDate;
    private LocalDateTime subscriptionExpiryDate;

    // Limits based on plan
    private Integer maxUsers;
    private Integer maxProducts;
    private Integer maxLocations;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany(mappedBy = "memberBusinesses")
    private List<User> users;


}
