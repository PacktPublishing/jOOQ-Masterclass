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

        classicModelsRepository.executeStoredProcedureInAndOut();
        classicModelsRepository.executeStoredProcedureInOut();
        classicModelsRepository.executeStoredProcedureSelect();
        classicModelsRepository.executeStoredProcedureMultipleSelect();
        classicModelsRepository.executeStoredProcedureViaCallStatement();
    }
}
