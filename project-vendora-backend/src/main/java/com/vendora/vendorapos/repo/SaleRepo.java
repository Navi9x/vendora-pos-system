package com.vendora.vendorapos.repo;

import com.vendora.vendorapos.entity.Sale;
import com.vendora.vendorapos.entity.enums.PaymentStatus;
import com.vendora.vendorapos.entity.enums.SaleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@EnableJpaRepositories
public interface SaleRepo extends JpaRepository<Sale, Long> {
    int countByBusiness_Id(Long businessId);
    @Query("""
        SELECT s
        FROM Sale s
        WHERE s.business.id = :tenantId
          AND (:cashierId IS NULL OR s.cashier.id = :cashierId)
          AND (:date IS NULL OR FUNCTION('DATE', s.createdAt) = :date)
          AND (:saleStatus IS NULL OR s.status = :saleStatus)
          AND (:paymentStatus IS NULL OR s.paymentStatus = :paymentStatus)
        ORDER BY s.createdAt DESC
    """)
    Page<Sale> searchSales(@Param("tenantId") Long tenantId,
                           @Param("cashierId") Long cashierId,
                           @Param("date") LocalDate date,
                           @Param("saleStatus") SaleStatus saleStatus,
                           @Param("paymentStatus") PaymentStatus paymentStatus,
                           Pageable pageable);
}
