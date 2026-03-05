package com.vendora.vendorapos.dto;

import com.vendora.vendorapos.entity.Location;
import com.vendora.vendorapos.entity.enums.SubscriptionPlan;
import com.vendora.vendorapos.entity.enums.SystemType;
import lombok.Data;

@Data
public class BusinessCreateDTO {

    private String businessName;
    private String businessCode;

    private Location location;
    // Owner Info
    private String ownerName;
    private String email;
    private String phone;

    // Address Info
    private String city;
    private String address;
    private String postalCode;

    // POS System Type
    private SystemType systemType;

    // Subscription Plan
    private SubscriptionPlan subscriptionPlan;
}

