package com.vendora.vendorapos.repo;

import com.vendora.vendorapos.entity.Sale;
import com.vendora.vendorapos.entity.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface SaleItemRepo extends JpaRepository<SaleItem, Long> {
    List<SaleItem> findAllBySale(Sale sale);
}
