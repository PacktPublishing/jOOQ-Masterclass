package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.cumeDist;
import static org.jooq.impl.DSL.round;
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
}
