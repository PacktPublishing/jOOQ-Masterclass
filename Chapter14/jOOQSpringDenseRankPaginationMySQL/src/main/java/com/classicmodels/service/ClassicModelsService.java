package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.util.List;
import java.util.Map;
import jooq.generated.tables.pojos.Employee;
import jooq.generated.tables.pojos.Office;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public Map<Office, List<Employee>> loadOfficeWithEmploeePage(int start, int end) {

        return classicModelsRepository.fetchOfficeWithEmploeePage(start, end);
    }
}
