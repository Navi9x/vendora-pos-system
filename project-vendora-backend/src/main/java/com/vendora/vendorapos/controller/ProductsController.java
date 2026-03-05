package com.vendora.vendorapos.controller;

import com.vendora.vendorapos.dto.CategoryDTO;
import com.vendora.vendorapos.security.tenant.TenantContext;
import com.vendora.vendorapos.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
public class ProductsController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryDTO> getCategories() {
        Long tenantId = TenantContext.getTenantId();
        return categoryService.getCategories(tenantId);
    }

}
