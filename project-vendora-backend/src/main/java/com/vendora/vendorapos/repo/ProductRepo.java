package com.vendora.vendorapos.repo;

import com.vendora.vendorapos.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findAllByBusiness_id(Long business_id);
    Product findByIdAndBusiness_id(Long id, Long business_id);
}
