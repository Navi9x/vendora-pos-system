package com.vendora.vendorapos.controller;


import com.vendora.vendorapos.dto.LoginRequest;
import com.vendora.vendorapos.dto.LoginResponse;
import com.vendora.vendorapos.entity.Business;
import com.vendora.vendorapos.entity.Location;
import com.vendora.vendorapos.entity.User;
import com.vendora.vendorapos.entity.enums.SubscriptionPlan;
import com.vendora.vendorapos.entity.enums.SystemType;
import com.vendora.vendorapos.entity.enums.UserRole;
import com.vendora.vendorapos.repo.BusinessRepo;
import com.vendora.vendorapos.repo.LocationRepo;
import com.vendora.vendorapos.repo.UserRepo;
import com.vendora.vendorapos.service.AuthService;
import com.vendora.vendorapos.service.BusinessService;
import com.vendora.vendorapos.service.JwtService;
import com.vendora.vendorapos.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
public class LoginController {

    private final AuthService authService;
    private final JwtService jwtService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private LocationRepo locationRepo;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private BusinessRepo businessRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public LoginController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }


    public void defaultSave(){

        Business business = new Business();
        business.setCreatedAt(LocalDateTime.now());
        business.setBusinessName("Green Park");
        business.setIsActive(true);
        business.setBusinessCode("A1B2C3");
        business.setSystemType(SystemType.RESTAURANT);
        business.setSubscriptionStartDate(LocalDateTime.now());
        business.setSubscriptionExpiryDate(LocalDateTime.now().plusDays(30));
        business.setSubscriptionPlan(SubscriptionPlan.ENTERPRISE);
        business.setUsers(new ArrayList<>());
        Business savedBusiness = businessRepo.save(business);

        Location location = new Location();
        location.setCreatedAt(LocalDateTime.now());
        location.setAddress("Colombo road");
        location.setCity("Madurankuliya");
        location.setIsActive(true);
        location.setIsMain(true);
        location.setPhone("+94771112226");
        location.setPostalCode("63270");
        location.setLocationName("Main Branch");
        location.setBusiness(savedBusiness);
        Location savedLocation = locationRepo.save(location);

        User user = new User();
        user.setCreatedAt(LocalDateTime.now());
        user.setPhone("+94771112228");
        user.setPassword(passwordEncoder.encode("123"));
        user.setEmail("navi@gmail.com");
        user.setFirstName("Navindu");
        user.setLastName("Rathnayaka");
        user.setBusiness(savedBusiness);
        user.setRole(UserRole.ADMIN);
        user.setLocation(savedLocation);
        user.setMemberBusinesses(new ArrayList<>());
        user.setOwnedBusinesses(new ArrayList<>());
        User savedUser = userRepo.save(user);

        savedBusiness.setOwner(savedUser);
        businessRepo.save(savedBusiness);

        savedUser.getMemberBusinesses().add(savedBusiness);
        userRepo.save(savedUser);

    }

    @PostMapping("/authentication")
    public LoginResponse loginWithGeneratingJWT(@RequestBody LoginRequest loginRequest) throws Exception{
        authService.authenticate(loginRequest.getUsername(),loginRequest.getUserPassword());
        return jwtService.createJwtToken(loginRequest);
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> isTokenValid(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
            }

            String token = authHeader.substring(7);
            String username = jwtUtil.getUsernameFromToken(token);
            UserDetails userDetails = jwtService.loadUserByUsername(username);

            return ResponseEntity.ok(jwtUtil.validateToken(token, userDetails));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
    }

    @PostMapping("/test")
    public String testRequest(){
        return "test 2026";
    }
}
