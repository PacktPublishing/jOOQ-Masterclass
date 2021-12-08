package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import com.classicmodels.pojo.Office;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }
    
    public List<Office> fetchOfficesInTerritory(String territory) {

        return classicModelsRepository.findOfficesInTerritory(territory);
    }
}
