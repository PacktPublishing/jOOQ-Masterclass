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

        classicModelsRepository.batchUpdateStatements();        
        classicModelsRepository.batchUpdateRecords1();
        classicModelsRepository.batchUpdateRecords2();
        classicModelsRepository.batchUpdateRecords3();
        classicModelsRepository.batchUpdateCollectionOfObjects();
    }
}
