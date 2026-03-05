package com.vendora.vendorapos.service;


import com.vendora.vendorapos.dto.BusinessDTO;
import com.vendora.vendorapos.dto.LoginRequest;
import com.vendora.vendorapos.dto.LoginResponse;
import com.vendora.vendorapos.dto.UserDTO;
import com.vendora.vendorapos.entity.User;
import com.vendora.vendorapos.repo.UserRepo;
import com.vendora.vendorapos.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username);
        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    getAuthorities(user)
            );
        }else {
            throw new UsernameNotFoundException(username);
        }
    }

    private Set<SimpleGrantedAuthority> getAuthorities(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+String.valueOf(user.getRole())));
        return authorities;
    }

    public LoginResponse createJwtToken(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getUserPassword();

        UserDetails userDetails = loadUserByUsername(username);
        String newGeneratedToken = jwtUtil.generateToken(userDetails);
        User user = userRepo.findByEmail(username);

        ArrayList<BusinessDTO> businessDTOs = new ArrayList<>();

        user.getMemberBusinesses().forEach(business -> {
            BusinessDTO dto = new BusinessDTO(
                    business.getId(),
                    business.getBusinessName(),
                    business.getBusinessCode(),
                    business.getSystemType().name(),
                    business.getSubscriptionPlan().name(),
                    business.getIsActive(),
                    business.getSubscriptionStartDate(),
                    business.getSubscriptionExpiryDate(),
                    business.getMaxUsers(),
                    business.getMaxProducts(),
                    business.getMaxLocations(),
                    business.getCreatedAt()
            );
            businessDTOs.add(dto);
        });



        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getRole().name(),
                user.getIsActive(),
                businessDTOs,
                user.getLocation() != null ? user.getLocation().getId() : null
        );

        return new LoginResponse(
                userDTO,
                newGeneratedToken
        );
    }

}
