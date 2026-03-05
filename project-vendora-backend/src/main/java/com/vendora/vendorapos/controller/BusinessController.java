package com.vendora.vendorapos.controller;

import com.vendora.vendorapos.dto.BusinessCreateDTO;
import com.vendora.vendorapos.entity.Business;
import com.vendora.vendorapos.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/business")
public class BusinessController {
    @Autowired
    private BusinessService businessService;

    @PostMapping("/create-new-business")
    public Business createNewBusiness(@RequestBody BusinessCreateDTO businessCreateDTO) {
        return businessService.createNewBusiness(businessCreateDTO);
    }
}
