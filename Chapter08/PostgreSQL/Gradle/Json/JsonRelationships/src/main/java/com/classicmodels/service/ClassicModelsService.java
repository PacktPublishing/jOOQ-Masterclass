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
        
        classicModelsRepository.arrayToJson();
        classicModelsRepository.UDTToJson();
        
        classicModelsRepository.oneToOneToJson();
        classicModelsRepository.oneToOneToJsonLimit();
        
        classicModelsRepository.oneToManyToJson();
        classicModelsRepository.oneToManyToJsonLimit();
        
        classicModelsRepository.manyToManyToJsonManagersOffices();
        classicModelsRepository.manyToManyToJsonOfficesManagers();
        classicModelsRepository.manyToManyToJsonManagersOfficesLimit();                 
    }
}
