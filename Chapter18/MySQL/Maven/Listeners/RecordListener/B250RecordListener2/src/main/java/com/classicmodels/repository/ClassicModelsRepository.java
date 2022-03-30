package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import jooq.generated.tables.records.EmployeeRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void insertEmployee() {

        ctx.transaction(configuration -> {

            EmployeeRecord employee = configuration.dsl().newRecord(EMPLOYEE);

            employee.setLastName("John");
            employee.setFirstName("McRit");
            employee.setEmail("mcritj@.classicmodelcars.com");
            employee.setOfficeCode("1");
            employee.setSalary(65000);
            employee.setJobTitle("Sales Rep");

            employee.insert();
        });
    }
}
