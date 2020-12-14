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

        classicModelsRepository.naturalJoinOfficeCustomerdetail();             // EXAMPLE 1
        classicModelsRepository.naturalLeftOuterJoinOfficeCustomerdetail();    // EXAMPLE 2
        classicModelsRepository.naturalRightOuterJoinOfficeCustomerdetail();   // EXAMPLE 3        
        classicModelsRepository.naturalFullOuterJoinOfficeCustomerdetail();    // EXAMPLE 4
        classicModelsRepository.naturalJoinOrderCustomerPayment();             // EXAMPLE 5
    }
}
