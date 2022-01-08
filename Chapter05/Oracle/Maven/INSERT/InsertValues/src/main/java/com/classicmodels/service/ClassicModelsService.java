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

        classicModelsRepository.insertOrderAutoGenKey();                // EXAMPLE 1
        classicModelsRepository.insertOrderManualKey();                 // EXAMPLE 2
        classicModelsRepository.insertMultipleOrderAutoGenKey();        // EXAMPLE 3
        classicModelsRepository.insertMultipleOrderManualKey();         // EXAMPLE 4
        classicModelsRepository.insertPaymentCompositeKey();            // EXAMPLE 5
        classicModelsRepository.insertOneSaleRecord();                  // EXAMPLE 6
        classicModelsRepository.insertTwoSaleRecord();                  // EXAMPLE 7
        classicModelsRepository.insertCollectionOfSaleRecord();         // EXAMPLE 8
        classicModelsRepository.insertNewRecord();                      // EXAMPLE 9
        classicModelsRepository.insertRecordAfterResettingPK();         // EXAMPLE 10
        classicModelsRepository.usingFunctionsInInsert();               // EXAMPLE 11        
        classicModelsRepository.insertDepartment();                     // EXAMPLE 12
        classicModelsRepository.insertOrderBetweenDates();              // EXAMPLE 13
        classicModelsRepository.insertAndUDTRecord();                   // EXAMPLE 14        
    }
}