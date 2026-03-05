package com.vendora.vendorapos.entity;
import com.vendora.vendorapos.entity.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
@Data
public class Expense extends BaseEntity {

    @Column(nullable = false)
    private LocalDate expenseDate;

    @Column(nullable = false)
    private String category; // Rent, Utilities, Salaries, etc.

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "recorded_by")
    private User recordedBy;

    @Column(columnDefinition = "TEXT")
    private String attachmentUrl;
}
