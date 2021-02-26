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
        classicModelsRepository.cleanUpSaleTable();
        classicModelsRepository.loadCSVDefaults();

        classicModelsRepository.cleanUpSaleTable();
        classicModelsRepository.loadCSVOnlyCertainFields();

        classicModelsRepository.cleanUpSaleTable();
        classicModelsRepository.loadCSVDefaultsFromString();

        classicModelsRepository.loadCSVInTwoTables();

        classicModelsRepository.cleanUpSaleTable();
        classicModelsRepository.loadCSVCertainSettings();

        classicModelsRepository.cleanUpSaleTable();
        classicModelsRepository.loadCSVRowListeners();

        classicModelsRepository.cleanUpSaleTable();
        classicModelsRepository.loadCSVCommitNone();
        
        classicModelsRepository.cleanUpSaleTable();
        classicModelsRepository.loadCSVOnDuplicateKeyUpdate();

        classicModelsRepository.cleanUpSaleTable();
        classicModelsRepository.loadCSVOnDuplicateKeyIgnore();

        classicModelsRepository.cleanUpSaleTable();
        classicModelsRepository.loadCSVBulkBatchCommit();

        // the following examples throws exceptions
        // classicModelsRepository.cleanUpSaleTable();
        // classicModelsRepository.loadCSVonDuplicateKeyError();
        
        // classicModelsRepository.cleanUpSaleTable();
        // classicModelsRepository.loadCSVonErrorAbort();
    }
}
