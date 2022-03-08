package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.denseRank;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
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

    /*  The DENSE_RANK() is a window function that assigns a rank to each 
        row within a partition or result set with no gaps in ranking values. */
    
    // simple use case, just assign a sequential number
    public void dummyAssignDenseRankNumberToSales() {

        ctx.select(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_,
                denseRank().over().partitionBy(SALE.FISCAL_YEAR)
                        .orderBy(SALE.SALE_.desc()).as("sales_rank"))
                .from(SALE)
                .fetch();
    }

    public void rankEmployeesInOfficeBySalary() {

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                OFFICE.CITY, OFFICE.COUNTRY, OFFICE.OFFICE_CODE,
                denseRank().over().partitionBy(OFFICE.OFFICE_CODE)
                        .orderBy(EMPLOYEE.SALARY.desc()).as("salary_rank"))
                .from(EMPLOYEE)
                .innerJoin(OFFICE)
                .on(OFFICE.OFFICE_CODE.eq(EMPLOYEE.OFFICE_CODE))
                .fetch();

        // select only the highest salary
        ctx.select(asterisk().except(field(name("t", "salary_rank")))).from(
                select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                        OFFICE.CITY, OFFICE.COUNTRY, OFFICE.OFFICE_CODE,
                        denseRank().over().partitionBy(OFFICE.OFFICE_CODE)
                                .orderBy(EMPLOYEE.SALARY.desc()).as("salary_rank"))
                        .from(EMPLOYEE)
                        .innerJoin(OFFICE)
                        .on(OFFICE.OFFICE_CODE.eq(EMPLOYEE.OFFICE_CODE)).asTable("t")
        ).where(field(name("t", "salary_rank")).eq(1))
                .fetch();
     
        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                OFFICE.CITY, OFFICE.COUNTRY, OFFICE.OFFICE_CODE)
                .from(EMPLOYEE)
                .innerJoin(OFFICE)
                .on(OFFICE.OFFICE_CODE.eq(EMPLOYEE.OFFICE_CODE))
                .qualify(denseRank().over().partitionBy(OFFICE.OFFICE_CODE)
                        .orderBy(EMPLOYEE.SALARY.desc()).eq(1))
                .fetch();        
    }    
}