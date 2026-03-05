package com.vendora.vendorapos.entity;
import com.vendora.vendorapos.entity.enums.SystemType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "system_features")
@Data
public class SystemFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String featureCode; // e.g., "TABLE_MANAGEMENT", "BARCODE_SCANNING"

    @Column(nullable = false)
    private String featureName;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SystemType applicableSystem; // Which system type this feature belongs to

    @Column(nullable = false)
    private Boolean isCore = true; // Core feature or addon

    @Column(nullable = false)
    private Boolean isActive = true;
}
