package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.sql.SQLException;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public void callAll() throws SQLException {

        classicModelsRepository.executePolymorphicFunction();
    }
}
