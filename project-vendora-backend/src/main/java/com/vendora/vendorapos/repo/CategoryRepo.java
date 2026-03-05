package com.vendora.vendorapos.repo;

import com.vendora.vendorapos.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface CategoryRepo extends JpaRepository<Category, Long> {
    List<Category> findByBusiness_Id(Long tenantId);
}
