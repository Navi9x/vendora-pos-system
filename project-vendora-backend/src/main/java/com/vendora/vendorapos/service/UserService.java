package com.vendora.vendorapos.service;

import com.vendora.vendorapos.dto.CashierDTO;
import com.vendora.vendorapos.entity.Business;
import com.vendora.vendorapos.entity.User;
import com.vendora.vendorapos.repo.BusinessRepo;
import com.vendora.vendorapos.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    BusinessRepo businessRepo;

    public User save() {
//        if(!userRepo.existsById(signupRequest.getUserName())){
//            User user = new User();
//            user.setUserName(signupRequest.getUserName());
//            user.setUserPassword(signupRequest.getUserPassword());
//            user.setUserFirstName(signupRequest.getUserFirstName());
//            user.setUserLastName(signupRequest.getUserLastName());
//            Set<Role> roles = new HashSet<>();
//
//            if(signupRequest.getUserRole().equals("USER")){
//                Role role = new Role();
//                role.setRoleName(signupRequest.getUserRole());
//                roles.add(role);
//            }else {
//                throw new RuntimeException("Not a valid user role");
//            }
//            user.setRoles(roles);
//            return userRepo.save(user);
//        }else{
//            throw new RuntimeException("Already exists user");
//        }
        return null;
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public List<CashierDTO> findAll(Long businessId) {
        Business business = businessRepo.findById(businessId).get();
        List<User> users = userRepo.findByBusiness(business);
        List<CashierDTO> cashierDTOS = new ArrayList<>();
        for (User user : users) {
            CashierDTO cashierDTO = new CashierDTO(
                    user.getId(),
                    user.getFirstName()+" "+user.getLastName()
            );
            cashierDTOS.add(cashierDTO);
        }
        return cashierDTOS;
    }
}
