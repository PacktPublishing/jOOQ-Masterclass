package com.classicmodels.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.classicmodels.repository.CustomerRepository;
import com.classicmodels.repository.OrderRepository;
import jooq.generated.tables.pojos.Customer;
import jooq.generated.tables.pojos.Order;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerOrderManagementService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public CustomerOrderManagementService(CustomerRepository customerRepository,
            OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }
   
    public List<Customer> fetchCustomersOrderedByCreditLimit(){

        return customerRepository.findCustomersOrderedByCreditLimit();
    }

    @Transactional(readOnly = true)
    public List<Customer> fetchCustomerByPhone(String phone) {
        
        return customerRepository.fetchByPhone(phone); // call jOOQ DAO
    }        
    
    public List<String> fetchOrderStatus() {
        
        return orderRepository.findOrderStatus();
    }
    
    @Transactional(readOnly = true)
    public Order fetchOrderById(Long id) {
        
        return orderRepository.findById(id); // call jOOQ DAO
    }
}