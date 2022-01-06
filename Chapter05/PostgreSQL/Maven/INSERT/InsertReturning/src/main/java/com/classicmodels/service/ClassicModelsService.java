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

        classicModelsRepository.returnOneId();                                   // EXAMPLE 1
        classicModelsRepository.returnMultipleIds();                             // EXAMPLE 2
        classicModelsRepository.insertReturningOfCustomerInCustomerDetail();     // EXAMPLE 3
        classicModelsRepository.insertEmployeeInManagerReturningId();            // EXAMPLE 4
        classicModelsRepository.insertNewManagerReturningId();                   // EXAMPLE 5
        classicModelsRepository.insertAndReturnMultipleColsProductline();        // EXAMPLE 6        
        classicModelsRepository.insertAndReturnAllColsProductline();             // EXAMPLE 7
        classicModelsRepository.insertReturningAndSerialInDepartment();          // EXAMPLE 8
    }
}
