package com.classicmodels.service;

import com.classicmodels.pojo.SimpleManager;
import com.classicmodels.pojo.SimpleOffice;
import com.classicmodels.repository.ClassicModelsRepository;
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
    
    public List<SimpleManager> fetchManagerAndOffice() {

        return classicModelsRepository.findManagerAndOffice();
    }

    public List<SimpleOffice> fetchOfficeAndManager() {

        return classicModelsRepository.findOfficeAndManager();
    }
}