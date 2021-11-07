package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.util.List;
import java.util.Map;
import jooq.generated.tables.daos.OrderRepository;
import jooq.generated.tables.pojos.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderPaymentService {

    private final ClassicModelsRepository classicModelsRepository;
    private final OrderRepository orderRepository;

    public OrderPaymentService(ClassicModelsRepository classicModelsRepository,
            OrderRepository orderRepository) {
        this.classicModelsRepository = classicModelsRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public Order fetchOrder(Long orderId) {

        /* using jOOQ generated DAOs */
        return orderRepository.fetchOneByOrderId(orderId);
    }

    @Transactional(readOnly = true)
    public List<Order> fetchCustomerOrders(Long customerNumber) {
     
        /* using jOOQ generated DAOs */
        return orderRepository.fetchByCustomerNumber(customerNumber);
    } 
    
    @Transactional(readOnly = true)
    public Map<Long, List<Order>> fetchAndGroupOrdersByCustomerId() {

        /* using application DAOs */
        return classicModelsRepository.findAndGroupOrdersByCustomerId();
    }           
}
