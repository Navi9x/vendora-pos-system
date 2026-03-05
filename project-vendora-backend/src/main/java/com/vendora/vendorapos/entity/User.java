package com.vendora.vendorapos.entity;
import com.vendora.vendorapos.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

    @OneToMany(mappedBy = "owner")
    private List<Business> ownedBusinesses;

    @ManyToMany
    @JoinTable(
            name = "user_business",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "business_id")
    )
    private List<Business> memberBusinesses;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role; // OWNER, ADMIN, MANAGER, CASHIER, WAITER, CHEF, STAFF

    @Column(nullable = false)
    private Boolean isActive = true;

    // For access control
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location; // Which branch/location they work at

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
