package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import com.classicmodels.pojo.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderPaymentService {

    private final ClassicModelsRepository classicModelsRepository;

    public OrderPaymentService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    @Transactional(readOnly = true)
    public Order fetchOrderTooMuchFields(Long orderId) {

        return classicModelsRepository.findOrderTooMuchFields(orderId);
    }
    
    @Transactional(readOnly = true)
    public Order fetchOrderExplicitFields(Long orderId) {

        return classicModelsRepository.findOrderExplicitFields(orderId);
    }
    
    @Transactional(readOnly = true)
    public Order fetchOrderAsteriskExcept(Long orderId) {

        return classicModelsRepository.findOrderAsteriskExcept(orderId);
    }
}
