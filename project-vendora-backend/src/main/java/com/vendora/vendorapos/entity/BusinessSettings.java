package com.vendora.vendorapos.entity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "business_settings")
@Data
public class BusinessSettings {
    @Id
    private Long businessId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "business_id")
    private Business business;

    // Tax Configuration
    private Double taxRate;
    private String taxName; // VAT, GST, Sales Tax
    private Boolean taxInclusive = false;
    private String taxRegistrationNumber;

    // Receipt Settings
    private String receiptHeader;
    private String receiptFooter;
    private Boolean printReceipt = true;
    private String receiptTemplate;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
