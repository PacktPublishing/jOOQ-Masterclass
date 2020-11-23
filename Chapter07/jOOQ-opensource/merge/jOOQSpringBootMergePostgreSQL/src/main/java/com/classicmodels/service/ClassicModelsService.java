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
        classicModelsRepository.insertPaymentOnDuplicateKeyIgnore();               // EXAMPLE 1
        classicModelsRepository.insertPaymentOnConflictDoNothing();                // EXAMPLE 2   
        classicModelsRepository.insertPaymentOnDuplicateCheckNumberDoNothing();    // EXAMPLE 3
        classicModelsRepository.insertPaymentOnConflictOnConstraintDoNothing();    // EXAMPLE 4
        classicModelsRepository.insertOrderOtherwiseUpdateIt();                    // EXAMPLE 5
        classicModelsRepository.insertPaymentOnConflictUpdateIt();                 // EXAMPLE 6
        classicModelsRepository.insertSaleRecordOnDuplicateKeyUpdateIt();          // EXAMPLE 7
        classicModelsRepository.insertPaymentRecordOnConflictUpdateIt();           // EXAMPLE 8
    }
}