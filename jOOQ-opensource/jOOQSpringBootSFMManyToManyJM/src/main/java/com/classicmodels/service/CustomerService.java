package com.classicmodels.service;

import com.classicmodels.pojo.ManagerDTO;
import com.classicmodels.repository.ManagerRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final ManagerRepository customerRepository;

    public CustomerService(ManagerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public List<ManagerDTO> fetchCustomerByCreditLimit(float creditLimit) {

        return customerRepository.findCustomerByCreditLimit(creditLimit);
    }

}
