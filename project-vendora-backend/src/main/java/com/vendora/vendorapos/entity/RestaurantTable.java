package com.vendora.vendorapos.entity;
import com.vendora.vendorapos.entity.enums.TableStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tables")
@Data
public class RestaurantTable extends BaseEntity {

    @Column(nullable = false)
    private String tableNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TableStatus status = TableStatus.AVAILABLE; // AVAILABLE, OCCUPIED, RESERVED, CLEANING

}
