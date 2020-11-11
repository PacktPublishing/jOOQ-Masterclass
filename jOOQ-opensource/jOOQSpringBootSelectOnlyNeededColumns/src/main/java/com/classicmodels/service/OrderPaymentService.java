package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderPaymentService {

    private final ClassicModelsRepository classicModelsRepository;

    public OrderPaymentService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public void callAll() {
        classicModelsRepository.finxxdd();
    }
}
