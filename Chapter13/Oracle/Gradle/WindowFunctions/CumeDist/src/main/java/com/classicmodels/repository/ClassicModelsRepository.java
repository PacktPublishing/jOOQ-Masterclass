package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.cumeDist;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.round;
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

    /*  The CUME_DIST() is a window function that returns the cumulative distribution of a value 
        within a set of values. It represents the number of rows with values less than or equal 
        to that row’s value divided by the total number of rows. The returned value of the CUME_DIST() 
        function is greater than zero and less than or equal one (0 < CUME_DIST() <= 1). 
        The repeated column values receive the same CUME_DIST() value. */
    
    public void cumeDistSalary() {

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                round(cumeDist().over().orderBy(EMPLOYEE.SALARY), 2).as("cume_dist"))
                .from(EMPLOYEE)
                .fetch();

        // emulate CUME_DIST() 
        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                round(((cast(count().over().orderBy(EMPLOYEE.SALARY).rangeUnboundedPreceding(), Double.class)).divide(
                        count().over().orderBy(EMPLOYEE.SALARY).rangeBetweenUnboundedPreceding()
                                .andUnboundedFollowing())), 2).as("cume_dist"))
                .from(EMPLOYEE)
                .fetch();
    }
    
        public void get25PercentTopSales() {

        ctx.select().from(
                select(concat(EMPLOYEE.FIRST_NAME, inline(" "), EMPLOYEE.LAST_NAME).as("name"),
                        SALE.SALE_, SALE.FISCAL_YEAR,
                        cumeDist().over().partitionBy(SALE.FISCAL_YEAR)
                                .orderBy(SALE.SALE_.desc()).as("cume_dist"))
                        .from(EMPLOYEE)
                        .join(SALE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER)
                                .and(SALE.FISCAL_YEAR.in(2003, 2004))).asTable("t"))
                .where(field(name("t", "cume_dist")).lt(0.25))
                .fetch();

        // same query via the QUALIFY clause
        ctx.select(concat(EMPLOYEE.FIRST_NAME, inline(" "), EMPLOYEE.LAST_NAME).as("name"),
                SALE.SALE_, SALE.FISCAL_YEAR)
                .from(EMPLOYEE)
                .join(SALE)
                .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER)
                        .and(SALE.FISCAL_YEAR.in(2003, 2004)))
                .qualify(cumeDist().over().partitionBy(SALE.FISCAL_YEAR)
                        .orderBy(SALE.SALE_.desc()).lt(BigDecimal.valueOf(0.25)))
                .fetch();
    }
}
