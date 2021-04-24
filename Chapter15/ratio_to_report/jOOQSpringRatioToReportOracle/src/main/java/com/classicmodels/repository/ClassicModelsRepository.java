package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
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
    
    public void ratioToReportSalary() {

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                round(ratioToReport(EMPLOYEE.SALARY).over(), 2).as("ratio_to_report_salary"))
                .from(EMPLOYEE)
                .fetch();

        // emulate RATIO_TO_REPORT()        
        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                round(EMPLOYEE.SALARY.cast(Double.class).divide(sum(EMPLOYEE.SALARY
                        .cast(Double.class)).over()), 2).as("ratio_to_report_salary"))
                .from(EMPLOYEE)
                .fetch();
    }

    public void ratioToReportSalaryPerOffice() {

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                round(ratioToReport(EMPLOYEE.SALARY).over()
                        .partitionBy(OFFICE.OFFICE_CODE), 2).as("ratio_to_report_salary"))
                .from(EMPLOYEE)
                .innerJoin(OFFICE)
                .on(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                .fetch();
    }
}
