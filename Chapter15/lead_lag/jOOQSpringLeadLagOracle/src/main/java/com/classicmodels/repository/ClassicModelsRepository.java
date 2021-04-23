package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.lag;
import static org.jooq.impl.DSL.lead;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.round;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    /*  - The LEAD() function allows you to look forward a number of rows and access data 
          of that row from the current row. 
        - The LAG() function allows you to look back a number of rows and access data 
          of that row from the current row.*/
    
    public void leadOrder() {

        ctx.select(ORDER.ORDER_ID, ORDER.CUSTOMER_NUMBER, ORDER.STATUS, ORDER.ORDER_DATE,
                lead(ORDER.ORDER_DATE, 1).over().orderBy(ORDER.ORDER_DATE).as("next_order"))
                .from(ORDER)
                .fetch();
    }

    public void leadSalaryByOffice() {

        ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                lead(EMPLOYEE.SALARY.coerce(Integer.class), 1, 0).over().partitionBy(OFFICE.OFFICE_CODE)
                        .orderBy(EMPLOYEE.SALARY).as("next_salary"))
                .from(OFFICE)
                .innerJoin(EMPLOYEE)
                .on(OFFICE.OFFICE_CODE.eq(EMPLOYEE.OFFICE_CODE))
                .fetch();
    }

    public void lagYoY() {

        ctx.select(table(name("t")).asterisk(), round(field(name("t", "sale"), Double.class)
                .minus(field(name("t", "prev_sale"), Double.class)).mul(100d)
                .divide(field(name("t", "prev_sale"), Double.class)), 1)
                .concat("%").as("YoY"))
                .from(
                        select(SALE.SALE_ID, SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, round(SALE.SALE_, 1),
                                round(lag(SALE.SALE_, 1).over().partitionBy(SALE.EMPLOYEE_NUMBER)
                                        .orderBy(SALE.FISCAL_YEAR, SALE.SALE_), 1))
                                .from(SALE)
                                .asTable("t", "sid", "sen", "fy", "sale", "prev_sale"))
                .fetch();
    }

    public void leadLagSalary() {

        ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                lead(EMPLOYEE.SALARY.coerce(Integer.class), 1, 0).over().partitionBy(OFFICE.OFFICE_CODE)
                        .orderBy(EMPLOYEE.SALARY.desc().nullsLast()).as("next_salary"),
                lag(EMPLOYEE.SALARY.coerce(Integer.class), 1, 0).over().partitionBy(OFFICE.OFFICE_CODE)
                        .orderBy(EMPLOYEE.SALARY.desc().nullsLast()).as("prev_salary"))
                .from(OFFICE)
                .innerJoin(EMPLOYEE)
                .on(OFFICE.OFFICE_CODE.eq(EMPLOYEE.OFFICE_CODE))
                .fetch();
    }
}
