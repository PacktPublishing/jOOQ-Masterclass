package com.classicmodels.repository;

import static jooq.generated.Routines.concatenate;
import static jooq.generated.tables.Employee.EMPLOYEE;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void callConcatenate() {

        ctx.select(concatenate(EMPLOYEE.FIRST_NAME))
                .from(EMPLOYEE)
                .where(EMPLOYEE.FIRST_NAME.like("M%"))
                .fetch();
    }
}
