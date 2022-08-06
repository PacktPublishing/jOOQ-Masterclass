package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
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

    public void multisetAggFromJoin() {

        ctx.select(EMPLOYEE.EMPLOYEE_NUMBER,
                multisetAgg(EMPLOYEE.JOB_TITLE, EMPLOYEE.SALARY, SALE.FISCAL_YEAR, SALE.SALE_))
                .from(EMPLOYEE)
                .join(SALE)
                .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                .groupBy(EMPLOYEE.EMPLOYEE_NUMBER)
                .fetch();
    }
}
