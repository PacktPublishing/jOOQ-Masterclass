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

        classicModelsRepository.cte1();
        classicModelsRepository.cte2();
        classicModelsRepository.cte3();
        classicModelsRepository.cte4();

        classicModelsRepository.cte5();
        classicModelsRepository.cte6();
        classicModelsRepository.cte7();
        
        classicModelsRepository.cte8();
        classicModelsRepository.cte9();
        
        classicModelsRepository.cte10();
        
        classicModelsRepository.cte11();
    }
}
