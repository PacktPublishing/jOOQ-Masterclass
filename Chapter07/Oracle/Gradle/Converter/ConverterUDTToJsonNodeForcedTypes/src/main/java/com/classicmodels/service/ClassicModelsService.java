package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public void callAll() throws JsonProcessingException {

        classicModelsRepository.insertManagerEvaluation();
        classicModelsRepository.fetchManagerEvaluation();
    }
}
