package com.vendora.vendorapos.repo;

import com.vendora.vendorapos.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {
    List<Customer> getAllByBusiness_Id(Long businessId);
}
