package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void selfJoinComparingEmployeeViaNavigationMethod() {

        /*
        System.out.println("DEFAULT GENERATOR STRATEGY:\n"
                + ctx.select(concat(EMPLOYEE.FIRST_NAME, val(" "), EMPLOYEE.LAST_NAME).as("employee"),
                        concat(EMPLOYEE.employee().FIRST_NAME, val(" "), EMPLOYEE.employee().LAST_NAME).as("reports_to"))
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.JOB_TITLE.eq(EMPLOYEE.employee().JOB_TITLE))
                        .fetch()
        );
        */
        
        System.out.println("CUSTOM GENERATOR STRATEGY:\n"
                + ctx.select(concat(EMPLOYEE.FIRST_NAME, val(" "), EMPLOYEE.LAST_NAME).as("employee"),
                        concat(EMPLOYEE.reportsTo().FIRST_NAME, val(" "), EMPLOYEE.reportsTo().LAST_NAME).as("reports_to"))
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.JOB_TITLE.eq(EMPLOYEE.reportsTo().JOB_TITLE))
                        .fetch()
        );
    }
}
