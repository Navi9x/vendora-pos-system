package com.vendora.vendorapos.controller;

import com.vendora.vendorapos.dto.CreateSaleDTO;
import com.vendora.vendorapos.dto.ProductDTO;
import com.vendora.vendorapos.dto.SaleItemRequestDTO;
import com.vendora.vendorapos.entity.User;
import com.vendora.vendorapos.security.SecurityUtils;
import com.vendora.vendorapos.security.tenant.TenantContext;
import com.vendora.vendorapos.service.ProductService;
import com.vendora.vendorapos.service.SaleService;
import com.vendora.vendorapos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/sales")
public class SalesController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SaleService saleService;

    @Autowired
    private UserService userService;

    private Long tenantId;

    @GetMapping
    public String sales() {
        return "sales";
    }

    @GetMapping("{business_id}")
    public ResponseEntity<List<ProductDTO>> productsById(@PathVariable(value = "business_id") Long business_id) {
        List<ProductDTO> productDTOS = productService.getAllProducts(business_id);
        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }

    @GetMapping("/total")
    public ResponseEntity<Integer> getTotalSales() {
        tenantId = TenantContext.getTenantId(); // from JWT
        int total = saleService.getTotalSales(tenantId);
        System.out.println("Total sales of"+tenantId+"- "+total);
        return ResponseEntity.ok(total);
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody CreateSaleDTO createSaleDTO) {
        String userEmail = SecurityUtils.getCurrentUserId();
        if(tenantId == null){
            tenantId = TenantContext.getTenantId();
        }
        saleService.saveSale(createSaleDTO,tenantId,userEmail);
        return null;
    }


}
