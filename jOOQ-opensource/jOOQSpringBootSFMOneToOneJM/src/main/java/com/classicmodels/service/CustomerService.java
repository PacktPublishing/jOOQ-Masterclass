package com.classicmodels.service;

import com.classicmodels.pojo.CustomerDTO;
import com.classicmodels.repository.CustomerRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public List<CustomerDTO> fetchCustomerByCreditLimit(float creditLimit) {

        return customerRepository.findCustomerByCreditLimit(creditLimit);
    }

}
