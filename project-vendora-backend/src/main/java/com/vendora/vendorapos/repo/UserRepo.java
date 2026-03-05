package com.vendora.vendorapos.repo;

import com.vendora.vendorapos.entity.Business;
import com.vendora.vendorapos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    User findByEmail(String email);
    List<User> findByBusiness(Business business);
}
