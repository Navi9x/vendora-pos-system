package com.vendora.vendorapos.controller;

import com.vendora.vendorapos.dto.CustomerDTO;
import com.vendora.vendorapos.security.tenant.TenantContext;
import com.vendora.vendorapos.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/get-all")
    public List<CustomerDTO> getCustomers() {
        Long tenantId = TenantContext.getTenantId();
        return customerService.getCustomers(tenantId);
    }

}
