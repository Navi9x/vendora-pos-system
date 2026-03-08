package com.vendora.vendorapos.service;

import com.vendora.vendorapos.dto.ProductDTO;
import com.vendora.vendorapos.entity.Product;
import com.vendora.vendorapos.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public List<ProductDTO> getAllProducts(Long business_id) {
        List<Product> products = productRepo.findAllByBusiness_id(business_id);
        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = new ProductDTO(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getCategory().getName(),
                    product.getImage()!=null?product.getImage():"",
                    product.getIsActive()
            );
            productDTOs.add(productDTO);
        }
        return productDTOs;
    }
}
