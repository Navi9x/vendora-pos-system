package com.vendora.vendorapos.controller;

import com.vendora.vendorapos.dto.SaleResponseDTO;
import com.vendora.vendorapos.entity.enums.PaymentStatus;
import com.vendora.vendorapos.entity.enums.SaleStatus;
import com.vendora.vendorapos.security.tenant.TenantContext;
import com.vendora.vendorapos.service.SalesHistoryService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sales-history/")
public class SalesHistoryController {

    @Autowired
    private SalesHistoryService salesHistoryService;

    @GetMapping(path = "load",params = {"page","size"})
    public List<SaleResponseDTO> loadSalesHistory(
            @RequestParam(value = "page") @Min(0) int page,
            @RequestParam(value = "size") @Max(50) @Min(0) int size,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Long cashierId,
            @RequestParam(required = false) SaleStatus saleStatus,
            @RequestParam(required = false) PaymentStatus paymentStatus
            ){
        System.out.println("Page: " + page+" Size: " + size);
        Long tenantId = TenantContext.getTenantId();
        return salesHistoryService.loadSalesHistory(tenantId, page, size,date,cashierId,saleStatus,paymentStatus);
    }

}
