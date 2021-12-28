package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public int jooqQuery() {

        return classicModelsRepository.jooqQuery();
    }
    
    public List<String> jooqResultQuery() {
        
        return classicModelsRepository.jooqResultQuery();
    }
    
    public void iterableResultQuery() {
        
        classicModelsRepository.iterableResultQuery();
    }
}