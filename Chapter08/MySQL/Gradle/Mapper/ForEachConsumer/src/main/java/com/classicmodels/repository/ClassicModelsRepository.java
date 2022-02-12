package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import org.jooq.DSLContext;
import org.jooq.Record3;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void sendEmailToEmployee() {

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.EMAIL)
                .from(EMPLOYEE)
                .forEach((Record3<String, String, String> record) -> {
                    System.out.println("\n\nTo: " + record.getValue(EMPLOYEE.EMAIL));
                    System.out.println("From: " + "hrdepartment@classicmodelcars.com");
                    System.out.println("Body: \n   Dear, "
                            + record.getValue(EMPLOYEE.FIRST_NAME)
                            + " " + record.getValue(EMPLOYEE.LAST_NAME) + " ...");
                });
    }
}
