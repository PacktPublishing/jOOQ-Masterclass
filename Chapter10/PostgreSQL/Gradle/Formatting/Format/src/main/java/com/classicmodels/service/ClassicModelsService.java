package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public String formatResultAsJSON() {

        return classicModelsRepository.formatResultAsJSON();
    }
    
    public String formatResultAsXML() {

        return classicModelsRepository.formatResultAsXML();
    }
    
    public String formatResultAsHTML() {

        return classicModelsRepository.formatResultAsHTML();
    }
    
    public String formatResultAsCSV() {

        return classicModelsRepository.formatResultAsCSV();
    }
    
    public String formatResultAsText() {

        return classicModelsRepository.formatResultAsText();
    }
    
    public String formatResultAs1Chart() {

        return classicModelsRepository.formatResultAs1Chart();
    }
    
    public String formatResultAs2Chart() {

        return classicModelsRepository.formatResultAs2Chart();
    }
    
    public String formatResultAsInserts() {

        return classicModelsRepository.formatResultAsInserts();
    }
}