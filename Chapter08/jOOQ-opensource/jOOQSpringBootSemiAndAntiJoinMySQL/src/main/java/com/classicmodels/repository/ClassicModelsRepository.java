package com.classicmodels.repository;

import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectOne;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    /* SEMI JOIN */
    // EXAMPLE 1
    public void joinOfficeDepartmentViaLeftJoin() {

        System.out.println("EXAMPLE 1\n"
                + ctx.selectDistinct(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .leftOuterJoin(CUSTOMER)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER))
                        .where(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER.isNotNull())
                        .fetch()
        );
    }

    // EXAMPLE 2
    public void joinOfficeDepartmentViaExists() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .whereExists(selectOne().from(CUSTOMER)
                                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER)))
                        .fetch()
        );
    }

    // EXAMPLE 3
    public void joinOfficeDepartmentViaIn() {

        System.out.println("EXAMPLE 3\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.EMPLOYEE_NUMBER.in(
                                select(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER).from(CUSTOMER)))
                        .fetch()
        );
    }

    // EXAMPLE 4
    /*
    select 
      `classicmodels`.`employee`.`first_name`, 
      `classicmodels`.`employee`.`last_name` 
    from 
      `classicmodels`.`employee` 
    where 
      exists (
        select 
          1 as `one` 
        from 
          `classicmodels`.`customer` 
        where 
          `classicmodels`.`employee`.`employee_number` 
             = `classicmodels`.`customer`.`sales_rep_employee_number`
      )    
     */
    public void joinOfficeDepartmentViaLeftSemiJoin() {

        System.out.println("EXAMPLE 4\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .leftSemiJoin(CUSTOMER)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER))
                        .fetch()
        );
    }

    /* ANTI JOIN */
    // EXAMPLE 5
    public void joinOfficeDepartmentViaNotExists() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .whereNotExists(selectOne().from(CUSTOMER)
                                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER)))
                        .fetch()
        );
    }

    // EXAMPLE 6
    public void joinOfficeDepartmentViaNotIn() {

        System.out.println("EXAMPLE 6\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.EMPLOYEE_NUMBER.notIn(
                                select(nvl(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER, -1L)).from(CUSTOMER))) // nvl() or isnull()
                        .fetch()
        );
    }

    // EXAMPLE 7
    /*
    select 
      `classicmodels`.`employee`.`first_name`, 
      `classicmodels`.`employee`.`last_name` 
    from 
      `classicmodels`.`employee` 
    where 
      not (
        exists (
          select 
            1 as `one` 
          from 
            `classicmodels`.`customer` 
          where 
            `classicmodels`.`employee`.`employee_number` 
               = `classicmodels`.`customer`.`sales_rep_employee_number`
        )
      )    
     */
    public void joinOfficeDepartmentViaAntiJoin() {

        System.out.println("EXAMPLE 7\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .leftAntiJoin(CUSTOMER)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER))
                        .fetch()
        );
    }
}
