package com.classicmodels.repository;

import jooq.generated.tables.records.EmployeeRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public EmployeeRecord insertEmployee() {

        EmployeeRecord employee = new EmployeeRecord();

        ctx.transaction(configuration -> {

            employee.attach(configuration);

            employee.setLastName("John");
            employee.setFirstName("McRatinger");
            employee.setEmail("mcritj@.classicmodelcars.com");
            employee.setOfficeCode("1");
            employee.setSalary(65000);
            employee.setJobTitle("Sales Rep");

            employee.insert();
        });

        return employee;
    }

    public void updateEmployee(EmployeeRecord employee) {

        ctx.transaction(configuration -> {

            employee.attach(configuration);

            employee.setOfficeCode("2");
            employee.setCommission(100);
            employee.update();
        });

        ctx.transaction(configuration -> {

            employee.attach(configuration);

            employee.setSalary(67000);
            employee.update();
        });

        ctx.transaction(configuration -> {

            employee.attach(configuration);

            employee.setSalary(60000);
            employee.update();
        });
    }
}
