package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import jooq.generated.tables.records.CustomerRecord;
import jooq.generated.tables.records.CustomerdetailRecord;
import jooq.generated.tables.records.DepartmentRecord;
import jooq.generated.tables.records.EmployeeRecord;
import jooq.generated.tables.records.OfficeRecord;
import org.jooq.Result;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public Result<DepartmentRecord> loadDepartments() {

        return classicModelsRepository.fetchDepartments();
    }

    public OfficeRecord loadOfficeOfDepartment(DepartmentRecord dr) {

        return classicModelsRepository.fetchOfficeOfDepartment(dr);
    }

    public Result<EmployeeRecord> loadEmployeeOfOffice(OfficeRecord or) {

        return classicModelsRepository.fetchEmployeesOfOffice(or);
    }

    public Result<CustomerRecord> loadCustomersOfEmployee(EmployeeRecord er) {

        return classicModelsRepository.fetchCustomersOfEmployee(er);
    }

    public CustomerdetailRecord loadCustomerdetailOfCustomer(CustomerRecord cr) {

        return classicModelsRepository.fetchCustomerdetailOfCustomer(cr);
    }
}
