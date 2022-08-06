package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.multisetAgg;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }
    
    public void collectionOfEmployeesHavingSameSalary() {
        
        ctx.select(EMPLOYEE.EMPLOYEE_NUMBER,
                multisetAgg(concat(EMPLOYEE.FIRST_NAME, inline(" "), EMPLOYEE.LAST_NAME), 
                        EMPLOYEE.EMAIL, EMPLOYEE.SALARY)
                 .over().partitionBy(EMPLOYEE.JOB_TITLE).orderBy(EMPLOYEE.SALARY)
                        .groupsBetweenCurrentRow().andCurrentRow()
                        .excludeCurrentRow().as("same_salary"))
                .from(EMPLOYEE)
                .orderBy(EMPLOYEE.JOB_TITLE, EMPLOYEE.SALARY)
                .fetch();
    }
}
