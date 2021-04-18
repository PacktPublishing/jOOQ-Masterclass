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

        classicModelsRepository.ddlFromJavaSchema();
        classicModelsRepository.createSchema();
        classicModelsRepository.createSequence();
        classicModelsRepository.populateSchema();
        classicModelsRepository.alterSchema();
        classicModelsRepository.createDropIndexes();
        classicModelsRepository.createTableFromAnotherTable();
        classicModelsRepository.createTempTable1();
        classicModelsRepository.createTempTable2();
        classicModelsRepository.createView();
    }
}
