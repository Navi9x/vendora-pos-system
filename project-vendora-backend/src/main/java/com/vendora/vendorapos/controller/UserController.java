package com.vendora.vendorapos.controller;

import com.vendora.vendorapos.dto.CashierDTO;
import com.vendora.vendorapos.entity.Business;
import com.vendora.vendorapos.entity.User;
import com.vendora.vendorapos.repo.BusinessRepo;
import com.vendora.vendorapos.repo.LocationRepo;
import com.vendora.vendorapos.repo.UserRepo;
import com.vendora.vendorapos.security.tenant.TenantContext;
import com.vendora.vendorapos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private LocationRepo locationRepo;

    @Autowired
    private BusinessRepo businessRepo;

    @GetMapping("/profile")
    public String getProfile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        System.out.println(token);
        return token;
    }

    @GetMapping("/profiles")
    public String getProfile(Authentication auth) {
        User user = userRepo.findByEmail(auth.getName());
        return "User name : "+user.getFirstName()+" "+user.getLastName()+", User email : "+user.getEmail()+" " +
                "User business name: "+user.getBusiness().getBusinessName();
    }

    @GetMapping( "/get-all")
    public List<CashierDTO> getCashiers() {
        Long tenantId = TenantContext.getTenantId();
        return userService.findAll(tenantId);
    }

}
