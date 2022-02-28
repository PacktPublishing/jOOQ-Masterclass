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

        classicModelsRepository.loadArraysDefaults();
        classicModelsRepository.loadArraysOnlyCertainFields();
        classicModelsRepository.loadArrayInTwoTables();
        classicModelsRepository.loadArrayRowListeners();
        classicModelsRepository.loadArraysCommitNone();
        classicModelsRepository.loadArraysOnDuplicateKeyUpdate();
        classicModelsRepository.loadArraysOnDuplicateKeyIgnore();
        classicModelsRepository.loadArraysBulkBatchCommit();
        
        // the following examples throws exceptions
        // classicModelsRepository.loadArraysOnDuplicateKeyError();
        // classicModelsRepository.loadArraysOnErrorAbort();
    }
}