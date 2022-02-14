package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.array;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
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

        var result1 = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME,
                row(EMPLOYEE.EMPLOYEE_NUMBER, row(EMPLOYEE.EMAIL, EMPLOYEE.JOB_TITLE,
                        row(SALE.HOT, SALE.TREND))).as("employee_status"),
                row(SALE.FISCAL_YEAR, row(SALE.SALE_ID, SALE.SALE_)).as("sale_status"))
                .from(EMPLOYEE)
                .join(SALE)
                .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                .fetch();

        System.out.println("Result:\n" + result1);
        System.out.println("Result (JSON): " + result1.formatJSON());
        System.out.println("Result (XML): " + result1.formatXML());
        
        var result2 = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME,
                array(select(row(SALE.FISCAL_YEAR, SALE.SALE_))
                        .from(SALE)
                        .where(SALE.EMPLOYEE_NUMBER.eq(EMPLOYEE.EMPLOYEE_NUMBER))).as("sale"))
                .from(EMPLOYEE)
                .fetch();

        System.out.println("Result:\n" + result2);
        System.out.println("Result (JSON): " + result2.formatJSON());
        System.out.println("Result (XML): " + result2.formatXML());
    }
}
