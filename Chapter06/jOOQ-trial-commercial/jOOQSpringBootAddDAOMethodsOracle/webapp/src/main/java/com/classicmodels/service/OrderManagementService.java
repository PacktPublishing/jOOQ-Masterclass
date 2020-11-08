package com.classicmodels.service;

import java.time.LocalDate;
import java.util.List;
import jooq.generated.tables.daos.OrderRepository;
import jooq.generated.tables.pojos.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderManagementService {
    
    private final OrderRepository orderRepository;

    public OrderManagementService(OrderRepository orderRepository) {    
        this.orderRepository = orderRepository;
    }            
    
    // call findOrderByStatusAndOrderDate() - available only in OrderRepository
    @Transactional(readOnly=true)    
    public List<Order> fetchOrderByStatusAndOrderDate(String status, LocalDate orderDate) {
        
        return orderRepository.findOrderByStatusAndOrderDate(status, orderDate);
    }
    
    // call findLimitedTo() - available in all DAOs
    @Transactional(readOnly=true)    
    public List<Order> fetchLimitedTo(int limit) {
        
        return orderRepository.findLimitedTo(5);
    }
}