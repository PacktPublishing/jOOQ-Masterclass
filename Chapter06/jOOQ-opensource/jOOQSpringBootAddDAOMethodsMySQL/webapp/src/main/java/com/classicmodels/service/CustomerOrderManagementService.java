package com.classicmodels.service;

import java.time.LocalDate;
import java.util.List;
import jooq.generated.tables.daos.CustomerRepository;
import jooq.generated.tables.daos.OrderRepository;
import jooq.generated.tables.pojos.Customer;
import jooq.generated.tables.pojos.Order;
import org.springframework.stereotype.Service;
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

    @Transactional(readOnly=true)
    public List<Customer> fetchFirstNCustomers(int limit) {
        
        return customerRepository.fetchLimitedTo(limit);
    }
    
    @Transactional(readOnly=true)    
    public List<Order> fetchStatusAndOrderDate(String status, LocalDate orderDate) {
        
        return orderRepository.fetchStatusAndOrderDate(status, orderDate);
    }
}
