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
        classicModelsRepository.insertPaymentOnDuplicateKeyIgnore();            // EXAMPLE 1
        classicModelsRepository.insertPaymentOnConflictDoNothing();             // EXAMPLE 2
        classicModelsRepository.insertOrderOtherwiseUpdateIt();                 // EXAMPLE 3
        classicModelsRepository.insertPaymentOnConflictUpdateIt();              // EXAMPLE 4
        classicModelsRepository.insertSaleRecordOnDuplicateKeyUpdateIt();       // EXAMPLE 5
        classicModelsRepository.insertPaymentRecordOnConflictUpdateIt();        // EXAMPLE 6
    }
}