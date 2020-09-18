package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.util.List;
import java.util.Map;
import jooq.generated.tables.pojos.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderPaymentService {

    private final ClassicModelsRepository classicModelsRepository;

    public OrderPaymentService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    @Transactional(readOnly = true)
    public Order fetchOrder(Long orderId) {

        return classicModelsRepository.findOrder(orderId);
    }

    @Transactional(readOnly = true)
    public Map<Long, List<Order>> fetchAndGroupOrdersByCustomerId() {

        return classicModelsRepository.findAndGroupOrdersByCustomerId();
    }
    
    @Transactional(readOnly = true)
    public List<Order> fetchCustomerOrders(Long customerNumber) {
     
        return classicModelsRepository.findCustomerOrders(customerNumber);
    }    
}
