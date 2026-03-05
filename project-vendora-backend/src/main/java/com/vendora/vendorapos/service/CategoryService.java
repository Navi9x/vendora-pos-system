package com.vendora.vendorapos.service;

import com.vendora.vendorapos.dto.CategoryDTO;
import com.vendora.vendorapos.entity.Category;
import com.vendora.vendorapos.repo.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    public List<CategoryDTO> getCategories(Long tenantId) {
        List<Category> categories = categoryRepo.findByBusiness_Id(tenantId);
        List<CategoryDTO> categoriesDTO = new ArrayList<>();
        for (Category category : categories) {
            System.out.println("C work: "+category.getName());
            CategoryDTO categoryDTO = new CategoryDTO(
                    category.getId(),
                    category.getName()
            );
            categoriesDTO.add(categoryDTO);
        }
        return categoriesDTO;
    }

}
