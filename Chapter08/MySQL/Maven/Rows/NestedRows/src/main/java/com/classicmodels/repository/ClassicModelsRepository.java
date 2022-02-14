package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.row;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void nestedRows() {

        var result = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME,
                row(EMPLOYEE.EMPLOYEE_NUMBER, row(EMPLOYEE.EMAIL, EMPLOYEE.JOB_TITLE,
                        row(SALE.HOT, SALE.TREND))).as("employee_status"),
                row(SALE.FISCAL_YEAR, row(SALE.SALE_ID, SALE.SALE_)).as("sale_status"))
                .from(EMPLOYEE)
                .join(SALE)
                .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                .fetch();

        System.out.println("Result:\n" + result);
        System.out.println("Result (JSON): " + result.formatJSON());
        System.out.println("Result (XML): " + result.formatXML());
    }
}
