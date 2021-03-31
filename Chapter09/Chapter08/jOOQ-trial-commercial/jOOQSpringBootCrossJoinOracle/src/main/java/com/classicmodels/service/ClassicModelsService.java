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

        classicModelsRepository.joinOfficeDepartmentViaImplicitCrossJoin();                 // EXAMPLE 1
        classicModelsRepository.joinOfficeDepartmentCertainColsViaImplicitCrossJoin();      // EXAMPLE 2
        classicModelsRepository.joinOfficeDepartmentViaCrossJoinAsInnerJoin();              // EXAMPLE 3
        classicModelsRepository.joinOfficeDepartmentViaCrossJoin();                         // EXAMPLE 4
        classicModelsRepository.joinOfficeDepartmentCertainColsViaCrossJoin();              // EXAMPLE 5
        classicModelsRepository.joinOfficeDepartmentConcatCertainColsViaCrossJoin();        // EXAMPLE 6
        classicModelsRepository.findProductsNoSalesAcrossCustomers();                       // EXAMPLE 7                
    }
}