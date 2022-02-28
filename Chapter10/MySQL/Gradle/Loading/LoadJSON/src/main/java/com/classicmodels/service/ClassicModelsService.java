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
        classicModelsRepository.loadJSONDefaults();
        
        classicModelsRepository.cleanUpSaleTable();
        classicModelsRepository.loadJSONDefaultsInlineFields1();
        
        classicModelsRepository.cleanUpSaleTable();
        classicModelsRepository.loadJSONDefaultsInlineFields2();
        
        classicModelsRepository.cleanUpSaleTable();
        classicModelsRepository.loadJSONDefaultsInlineFields3();

        classicModelsRepository.cleanUpSaleTable();
        classicModelsRepository.loadJSONDefaultsFromString();

        classicModelsRepository.cleanUpSaleTable();
        classicModelsRepository.loadJSONOnlyCertainFields1();
        
        classicModelsRepository.cleanUpSaleTable();
        classicModelsRepository.loadJSONOnlyCertainFields2();

        classicModelsRepository.cleanUpSaleTable();
        classicModelsRepository.loadJSONOnlyCertainInlineFields1();
        
        classicModelsRepository.cleanUpSaleTable();
        classicModelsRepository.loadJSONOnlyCertainInlineFields2();
          
        classicModelsRepository.loadJSONInTwoTables();

        classicModelsRepository.cleanUpSaleTable();
        classicModelsRepository.loadJSONRowListeners();

        classicModelsRepository.cleanUpSaleTable();
        classicModelsRepository.loadJSONCommitNone();

        classicModelsRepository.cleanUpSaleTable();
        classicModelsRepository.loadJSONOnDuplicateKeyUpdate();

        classicModelsRepository.cleanUpSaleTable();
        classicModelsRepository.loadJSONOnDuplicateKeyIgnore();

        classicModelsRepository.cleanUpSaleTable();
        classicModelsRepository.loadJSONBulkBatchCommit();

        // the following examples throws exceptions
        // classicModelsRepository.cleanUpSaleTable();
        // classicModelsRepository.loadJSONonDuplicateKeyError();
        // classicModelsRepository.cleanUpSaleTable();
        // classicModelsRepository.loadJSONonErrorAbort();
    }
}
