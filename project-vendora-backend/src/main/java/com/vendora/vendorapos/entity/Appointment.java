package com.vendora.vendorapos.entity;
import com.vendora.vendorapos.entity.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
public class Appointment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Product service; // Services are products

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private User staff; // Assigned staff member

    @Column(nullable = false)
    private LocalDateTime appointmentDateTime;

    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    private String notes;
}
