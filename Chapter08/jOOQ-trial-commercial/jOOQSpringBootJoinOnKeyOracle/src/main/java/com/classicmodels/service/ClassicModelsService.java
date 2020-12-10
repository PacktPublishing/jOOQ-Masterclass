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
        
        classicModelsRepository.joinPaymentPaymentdetailViaOn();            // EXAMPLE 1
        classicModelsRepository.joinPaymentPaymentdetailViaOnRow();         // EXAMPLE 2
        classicModelsRepository.joinPaymentPaymentdetailViaOnKey();         // EXAMPLE 3
        classicModelsRepository.joinPaymentPaymentdetailViaOnKeyFK();       // EXAMPLE 4
    }
}
