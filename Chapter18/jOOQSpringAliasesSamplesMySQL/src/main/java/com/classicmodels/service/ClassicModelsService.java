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

        classicModelsRepository.sample1();
        classicModelsRepository.sample2();
        classicModelsRepository.sample3();
        classicModelsRepository.sample4();
        classicModelsRepository.sample5();
        classicModelsRepository.sample6();
    }
}
