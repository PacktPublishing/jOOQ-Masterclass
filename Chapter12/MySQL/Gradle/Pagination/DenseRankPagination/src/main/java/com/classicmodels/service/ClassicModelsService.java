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

    public Map<Office, List<Employee>> loadOfficeWithEmployeeOffset(int page, int size) {

        return classicModelsRepository.fetchOfficeWithEmployeeOffset(page, size);
    }

    public Map<Office, List<Employee>> loadOfficeWithEmployeeSeek(String officeCode, int size) {

        return classicModelsRepository.fetchOfficeWithEmployeeSeek(officeCode, size);
    }

    public Map<Office, List<Employee>> loadOfficeWithEmployeeDR(int start, int end) {

        return classicModelsRepository.fetchOfficeWithEmployeeDR(start, end);
    }
}
