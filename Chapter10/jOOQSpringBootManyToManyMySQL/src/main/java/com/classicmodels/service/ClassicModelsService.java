package com.classicmodels.service;

import com.classicmodels.pojo.SimpleManager;
import com.classicmodels.repository.ClassicModelsRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public List<SimpleManager> fetchManyToMany() {

        return classicModelsRepository.fetchManyToMany();
    }
}
