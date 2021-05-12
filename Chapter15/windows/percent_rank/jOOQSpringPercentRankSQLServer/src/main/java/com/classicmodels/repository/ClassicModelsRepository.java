package com.classicmodels.repository;

import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.case_;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.percentRank;
import static org.jooq.impl.DSL.rank;
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

    /*  The PERCENT_RANK() window function calculates the percentile  
        ((rank - 1) / (total_rows - 1)) rankings of rows in a result set. */
    
    public void percentileRankEmployeesBySalary() {

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                round(percentRank().over().orderBy(EMPLOYEE.SALARY), 2).as("percentile_rank"))
                .from(EMPLOYEE)
                .fetch();

        // emulate percentRank() via rank()        
        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY, round(case_()
                .when(count().over().orderBy(EMPLOYEE.SALARY).rangeBetweenUnboundedPreceding()
                        .andUnboundedFollowing().eq(1), cast(0, Double.class))
                .else_((cast(rank().over().orderBy(EMPLOYEE.SALARY), Double.class).minus(1d))
                        .divide((count().over().orderBy(EMPLOYEE.SALARY).rangeBetweenUnboundedPreceding()
                                .andUnboundedFollowing()).minus(1d))), 2).as("percentile_rank"))
                .from(EMPLOYEE)
                .fetch();
    }

    public void percentileRankEmployeesBySalaryAndOffice() {

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.COUNTRY,
                round(percentRank().over().partitionBy(OFFICE.OFFICE_CODE)
                        .orderBy(EMPLOYEE.SALARY).mul(100), 2).concat("%").as("percentile_rank"))
                .from(EMPLOYEE)
                .innerJoin(OFFICE)
                .on(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                .fetch();

        // emulate percentRank() via rank()           
        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY, round(case_()
                .when(count().over().partitionBy(OFFICE.OFFICE_CODE)
                        .orderBy(EMPLOYEE.SALARY).rangeBetweenUnboundedPreceding()
                        .andUnboundedFollowing().eq(1), cast(0, Double.class))
                .else_((cast(rank().over().partitionBy(OFFICE.OFFICE_CODE)
                        .orderBy(EMPLOYEE.SALARY), Double.class).minus(1d))
                        .divide((count().over().partitionBy(OFFICE.OFFICE_CODE)
                                .orderBy(EMPLOYEE.SALARY).rangeBetweenUnboundedPreceding()
                                .andUnboundedFollowing()).minus(1d))), 2).as("percentile_rank"))
                .from(EMPLOYEE)
                .innerJoin(OFFICE)
                .on(EMPLOYEE.OFFICE_CODE.eq(OFFICE.OFFICE_CODE))
                .fetch();
    }
    
    public void computeProfitCategories() {

        ctx.select(count().filterWhere(field("p").lt(0.2)).as("low_profit"),
                count().filterWhere(field("p").between(0.2, 0.8)).as("good_profit"),
                count().filterWhere(field("p").gt(0.8)).as("high_profit")).from(
                select(percentRank().over().orderBy(DEPARTMENT.PROFIT).as("p"))
                        .from(DEPARTMENT)
                        .where(DEPARTMENT.PROFIT.isNotNull()))
                .fetch();                
    }
}
