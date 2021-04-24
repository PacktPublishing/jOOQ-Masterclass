package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.ratioToReport;
import static org.jooq.impl.DSL.round;
import static org.jooq.impl.DSL.sum;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    /*  The RATIO_TO_REPORT() is a window function (analytic function) that
        computes the ratio of the specified value to the sum of values in the set. */
    
    public void ratioToReportSale() {

        ctx.select(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_,
                round(ratioToReport(SALE.SALE_).over(), 2)
                        .as("ratio_to_report_sale"))
                .from(SALE)
                .fetch();

        // emulate RATIO_TO_REPORT()        
        ctx.select(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_,
                round(SALE.SALE_.divide(sum(SALE.SALE_).over()), 2).as("ratio_to_report_sale"))
                .from(SALE)
                .fetch();
    }

    public void ratioToReportSalePerEmployee() {

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_,
                round(ratioToReport(SALE.SALE_).over()
                        .partitionBy(SALE.EMPLOYEE_NUMBER), 2)
                        .as("ratio_to_report_sale"))
                .from(EMPLOYEE)
                .innerJoin(SALE)
                .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                .fetch();
    }
}
