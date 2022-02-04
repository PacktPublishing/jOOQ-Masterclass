package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    // if you get, ORA-40478: output value too large (maximum: 4000)
    // then you should set MAX_STRING_SIZE to EXTENTED instead of STANDARD   
    
    public void callAll() {
        
      classicModelsRepository.arrayToJson();
        
      classicModelsRepository.oneToOneToJson();
      classicModelsRepository.oneToOneToJsonLimit();
        
      classicModelsRepository.oneToManyToJson();
      classicModelsRepository.oneToManyToJsonLimit();
        
      classicModelsRepository.manyToManyToJsonManagersOffices();
      classicModelsRepository.manyToManyToJsonOfficesManagers();
      classicModelsRepository.manyToManyToJsonManagersOfficesLimit(); 
    }
}
