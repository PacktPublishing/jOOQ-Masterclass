package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public void callAll() {

        classicModelsRepository.joinCustomerPaymentIdentifyDataGaps();         // EXAMPLE 1
        classicModelsRepository.joinCustomerPaymentFillGaps();                 // EXAMPLE 2
        classicModelsRepository.joinCustomerPaymentFillGapsOracleStyle();      // EXAMPLE 3
    }
}