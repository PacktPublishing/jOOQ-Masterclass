package com.classicmodels.service;

import com.classicmodels.pojo.SimpleBManager;
import com.classicmodels.repository.ClassicModelsRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public List<SimpleBManager> fetchManyToManyUnidirectional() {

        return classicModelsRepository.fetchManyToManyUnidirectional();
    }
    
    public List<SimpleBManager> fetchManyToManyBidirectional() {

        return classicModelsRepository.fetchManyToManyBidirectional();
    }
}
