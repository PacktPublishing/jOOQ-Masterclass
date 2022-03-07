package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.ratioToReport;
import static org.jooq.impl.DSL.round;
import static org.jooq.impl.DSL.sum;
import org.jooq.impl.SQLDataType;
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

        ctx.select(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, round(SALE.SALE_, 2).as("sale"),
                round(cast(ratioToReport(SALE.SALE_).over(), SQLDataType.NUMERIC), 2)
                        .as("ratio_to_report_sale"))
                .from(SALE)
                .fetch();

        // emulate RATIO_TO_REPORT()        
        ctx.select(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, round(SALE.SALE_, 2).as("sale"),
                round(SALE.SALE_.divide(sum(SALE.SALE_).over()), 2).as("ratio_to_report_sale"))
                .from(SALE)
                .fetch();
    }

    public void ratioToReportSalePerFiscalYear() {

        ctx.select(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_,
                round(ratioToReport(SALE.SALE_).over()
                        .partitionBy(SALE.FISCAL_YEAR), 2)
                        .as("ratio_to_report_sale"))
                .from(SALE)
                .fetch();
    }

    public void ratioToReportEmployeeSalary() {

        ctx.select(OFFICE.OFFICE_CODE, sum(EMPLOYEE.SALARY).as("salaries"),
                ratioToReport(sum(EMPLOYEE.SALARY)).over()
                        .mul(100).concat("%").as("ratio_to_report"))
                .from(OFFICE)
                .join(EMPLOYEE)
                .on(OFFICE.OFFICE_CODE.eq(EMPLOYEE.OFFICE_CODE))
                .groupBy(OFFICE.OFFICE_CODE)
                .orderBy(OFFICE.OFFICE_CODE)
                .fetch();
    }
}
