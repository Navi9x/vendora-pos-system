package com.vendora.vendorapos.repo;

import com.vendora.vendorapos.entity.Business;
import com.vendora.vendorapos.entity.enums.SystemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface BusinessRepo extends JpaRepository<Business,Long> {
    long countBySystemType(SystemType systemType);
}
