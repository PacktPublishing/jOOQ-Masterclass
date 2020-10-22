package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    @Transactional
    public int jooqQuery() {

        return classicModelsRepository.jooqQuery();
    }

    @Transactional(readOnly = true)
    public List<String> jooqResultQuery() {
        
        return classicModelsRepository.jooqResultQuery();
    }
}