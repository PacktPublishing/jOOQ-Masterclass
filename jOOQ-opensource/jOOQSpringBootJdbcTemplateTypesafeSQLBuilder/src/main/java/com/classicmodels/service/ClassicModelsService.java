package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import com.classicmodels.pojo.DelayedPayment;
import com.classicmodels.pojo.Order;
import com.classicmodels.pojo.OrderAndNextOrderDate;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    @Transactional(readOnly = true)
    public Order fetchOrder(Long orderId) {

        return classicModelsRepository.findOrder(orderId);
    }

    @Transactional(readOnly = true)
    public List<OrderAndNextOrderDate> fetchOrderAndNextOrderDate() {

        return classicModelsRepository.findOrderAndNextOrderDate();
    }
    
    @Transactional(readOnly = true)
    public List<DelayedPayment> fetchDelayedPayments(
            LocalDate startDate, LocalDate endDate) {

        return classicModelsRepository.findDelayedPayments(startDate, endDate);
    }

}