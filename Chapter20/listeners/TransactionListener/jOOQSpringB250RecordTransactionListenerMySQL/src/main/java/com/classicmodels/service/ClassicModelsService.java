package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import jooq.generated.tables.records.EmployeeRecord;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public void manageEmployee() {

      EmployeeRecord employee = classicModelsRepository.insertEmployee();
      classicModelsRepository.updateEmployee(employee);
    }
}
