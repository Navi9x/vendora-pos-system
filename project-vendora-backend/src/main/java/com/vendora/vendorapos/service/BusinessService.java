package com.vendora.vendorapos.service;

import com.vendora.vendorapos.dto.BusinessCreateDTO;
import com.vendora.vendorapos.entity.Business;
import com.vendora.vendorapos.entity.enums.SystemType;
import com.vendora.vendorapos.repo.BusinessRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {

    @Autowired
    BusinessRepo businessRepo;

    public String generateBusinessCode(SystemType systemType) {
        String prefix = switch(systemType) {
            case RESTAURANT -> "REST";
            case RETAIL -> "RET";
            case GROCERY -> "GROC";
            case PHARMACY -> "PHAR";
            case SERVICE -> "SERV";
            default -> "BUS";
        };

        // Get next sequence number
        long count = businessRepo.countBySystemType(systemType);

        return String.format("%s%04d", prefix, count + 1);
        // Results: REST0001, REST0002, RET0001, etc.
    }

    public Business createNewBusiness(BusinessCreateDTO businessCreateDTO) {
        Business business = new Business();
        business.setBusinessName(businessCreateDTO.getBusinessName());
        business.setBusinessCode(generateBusinessCode(businessCreateDTO.getSystemType()));
        business.setSystemType(businessCreateDTO.getSystemType());
        business.setSubscriptionPlan(businessCreateDTO.getSubscriptionPlan());

        return businessRepo.save(business);
    }

}
