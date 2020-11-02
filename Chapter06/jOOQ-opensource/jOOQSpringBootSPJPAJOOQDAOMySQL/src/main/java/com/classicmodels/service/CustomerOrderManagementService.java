package com.classicmodels.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.classicmodels.jpa.repository.CustomerRepository;
import com.classicmodels.jpa.repository.OrderRepository;
import jooq.generated.tables.pojos.Customer;
import jooq.generated.tables.pojos.Order;

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

    public List<Customer> fetchCustomerByPhone(String phone) {
        
        return customerRepository.findCustomerByPhone(phone);
    }
      
    public List<com.classicmodels.entity.Customer> fetchTop10By() {
                 
        return customerRepository.findTop10By();
    }
    
    public List<String> fetchOrderStatus() {
        
        return orderRepository.findOrderStatus();
    }
    
    public Order fetchOrderById(Long id) {
        
        return orderRepository.findOrderById(id);
    }
    
    public List<com.classicmodels.entity.Order> fetchFirst5ByStatusOrderByShippedDateAsc(String status) {
        
        return orderRepository.findFirst5ByStatusOrderByShippedDateAsc(status);
    }
}