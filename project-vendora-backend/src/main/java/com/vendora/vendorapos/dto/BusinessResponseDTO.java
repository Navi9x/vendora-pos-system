package com.vendora.vendorapos.dto;

import com.vendora.vendorapos.entity.enums.SubscriptionPlan;
import com.vendora.vendorapos.entity.enums.SystemType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BusinessResponseDTO {

    private Long id;

    private String businessName;
    private String businessCode;

    private String ownerName;
    private String email;
    private String phone;

    private String city;
    private String address;
    private String postalCode;

    private SystemType systemType;
    private SubscriptionPlan subscriptionPlan;

    private Boolean isActive;

    private LocalDateTime subscriptionStartDate;
    private LocalDateTime subscriptionExpiryDate;

    private Integer maxUsers;
    private Integer maxProducts;
    private Integer maxLocations;

    private LocalDateTime createdAt;
}

