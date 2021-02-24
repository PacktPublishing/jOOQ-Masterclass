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

        classicModelsRepository.batchInsertStatements();
        classicModelsRepository.batchInsertRecords1();
        classicModelsRepository.batchInsertRecords2();
        classicModelsRepository.batchInsertRecords3();
        classicModelsRepository.forceNumberOfBatches();
    }
}
