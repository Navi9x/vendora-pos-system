package com.vendora.vendorapos.service;

import com.vendora.vendorapos.controller.CustomerController;
import com.vendora.vendorapos.dto.CustomerDTO;
import com.vendora.vendorapos.entity.Customer;
import com.vendora.vendorapos.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepo customerRepo;

    public List<CustomerDTO> getCustomers(Long tenantId) {
        List<Customer> customers = customerRepo.getAllByBusiness_Id(tenantId);
        List<CustomerDTO> customerDTOs = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerDTO customerDTO = new CustomerDTO(customer.getId(),customer.getName(),customer.getPhone());
            customerDTOs.add(customerDTO);
        }
        return customerDTOs;
    }

}
