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

        classicModelsRepository.joinOfficeDepartmentViaLeftJoin();           // EXAMPLE 1
        classicModelsRepository.joinOfficeDepartmentViaExists();             // EXAMPLE 2
        classicModelsRepository.joinOfficeDepartmentViaIn();                 // EXAMPLE 3
        classicModelsRepository.joinOfficeDepartmentViaLeftSemiJoin();       // EXAMPLE 4
        classicModelsRepository.joinOfficeDepartmentViaNotExists();          // EXAMPLE 5
        classicModelsRepository.joinOfficeDepartmentViaNotIn();              // EXAMPLE 6
        classicModelsRepository.joinOfficeDepartmentViaAntiJoin();           // EXAMPLE 7        
    }
}
