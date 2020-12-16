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

        classicModelsRepository.joinProductlineProductlinedetailViaOn();            // EXAMPLE 1
        classicModelsRepository.joinProductlineProductlinedetailViaOnRow();         // EXAMPLE 2
        //classicModelsRepository.joinProductlineProductlinedetailViaOnKey();         // EXAMPLE 3
        classicModelsRepository.joinProductlineProductlinedetailViaOnKeyTF();       // EXAMPLE 4
        classicModelsRepository.joinProductlineProductlinedetailViaOnKeyFK();       // EXAMPLE 5        
        
        classicModelsRepository.joinProductlineProductlinedetailViaOnKeyTF1();
    }
}
