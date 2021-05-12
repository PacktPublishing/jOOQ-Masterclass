package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.ntile;
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

    /*  The NTILE() is a window function useful for distributing the 
        number of rows in the specified N number of groups. */
    
    public void ntileSalary() {

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                ntile(5).over().orderBy(EMPLOYEE.SALARY.desc()).as("salary_group"))
                .from(EMPLOYEE)
                .fetch();
    }

    public void ntileSalaryPerOffice() {

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                ntile(2).over().partitionBy(OFFICE.OFFICE_CODE)
                        .orderBy(EMPLOYEE.SALARY.desc()).as("salary_group"))
                .from(EMPLOYEE)
                .innerJoin(OFFICE)
                .on(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                .fetch();
    }
    
    public void ntilePrices() {
        
        ctx.select(min(field("PRICE")), max(field("PRICE")), count(), field("BUCKET"))
                .from(select(ORDERDETAIL.PRICE_EACH.as("PRICE"), 
                        ntile(10).over().orderBy(ORDERDETAIL.PRICE_EACH).as("BUCKET"))
                        .from(ORDERDETAIL))
                .groupBy(field("BUCKET"))
                .fetch();
    }
}
