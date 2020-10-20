package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import com.classicmodels.pojo.Office;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;    

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;  
    }

    @Transactional(readOnly = true)
    public List<Office> fetchOfficesInTerritory1(String territory) {

        return classicModelsRepository.findOfficesInTerritory1(territory);
    }

    @Transactional(readOnly = true)
    public List<Office> fetchOfficesInTerritory2(String territory) {

        return classicModelsRepository.findOfficesInTerritory2(territory);
    }
    
    @Transactional(readOnly = true)
    public List<Office> fetchOfficesInTerritory3(String territory) {

        return classicModelsRepository.findOfficesInTerritory3(territory);
    }
    
    @Transactional(readOnly = true)
    public List<Office> fetchOfficesInTerritory4(String territory) {

        return classicModelsRepository.findOfficesInTerritory4(territory);
    }
        
    public String fetchOfficesInTerritory5(String territory) {

        return classicModelsRepository.findOfficesInTerritory5(territory);
    }
    
    @Transactional(readOnly = true)
    public List<Office> fetchOfficesInTerritory6(String territory) {

        return classicModelsRepository.findOfficesInTerritory6(territory);
    }
}