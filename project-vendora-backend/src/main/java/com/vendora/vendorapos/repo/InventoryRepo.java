package com.vendora.vendorapos.repo;

import com.vendora.vendorapos.entity.Business;
import com.vendora.vendorapos.entity.Inventory;
import com.vendora.vendorapos.entity.Product;
import com.vendora.vendorapos.entity.enums.StockStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@EnableJpaRepositories
public interface InventoryRepo extends JpaRepository<Inventory, Long> {

    @EntityGraph(attributePaths = {"product", "product.category"})
    @Query("""
SELECT i
FROM Inventory i
JOIN i.product p
LEFT JOIN p.category c
WHERE i.business.id = :tenantId
  AND i.isDeleted = false

  AND (:categoryId IS NULL OR c.id = :categoryId)

  AND (
        COALESCE(:searchText, '') = ''
        OR LOWER(p.name) LIKE LOWER(CONCAT('%', :searchText, '%'))
      )

  AND (
        :date IS NULL OR FUNCTION('DATE', i.lastRestocked) = :date
      )

  AND (
        :allStock = true
        OR (:outOfStock = true AND i.quantity <= 0)
        OR (:lowStock = true AND i.quantity > 0
             AND i.minStockLevel IS NOT NULL
             AND i.quantity <= i.minStockLevel)
        OR (:inStock = true AND i.quantity > 0
             AND (i.minStockLevel IS NULL OR i.quantity > i.minStockLevel))
      )

ORDER BY p.name ASC
""")
    Page<Inventory> searchInventory(
            @Param("tenantId") Long tenantId,
            @Param("searchText") String searchText,
            @Param("categoryId") Long categoryId,
            @Param("date") LocalDate date,
            @Param("allStock") boolean allStock,
            @Param("outOfStock") boolean outOfStock,
            @Param("lowStock") boolean lowStock,
            @Param("inStock") boolean inStock,
            Pageable pageable
    );

    List<Inventory> findAllByBusiness(Business business);

    Inventory findByProductAndBusiness(Product product, Business business);

    List<Inventory> findByBusiness(Business business);
}
