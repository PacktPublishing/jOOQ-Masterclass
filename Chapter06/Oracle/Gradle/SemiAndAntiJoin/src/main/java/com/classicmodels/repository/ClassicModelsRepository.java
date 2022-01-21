package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
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

    // EXAMPLE 1 - SEMI JOIN via EXISTS
    public void joinEmployeeCustomerViaExists() {

        System.out.println("EXAMPLE 1\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .whereExists(selectOne().from(CUSTOMER)
                                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER)))
                        .fetch()
        );
    }

    // EXAMPLE 2 - SEMI JOIN via IN
    public void joinEmployeeCustomerViaIn() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.EMPLOYEE_NUMBER.in(
                                select(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER).from(CUSTOMER)))
                        .fetch()
        );
    }
    
    // EXAMPLE 3 - SEMI JOIN via LEFT JOIN and IS NOT NULL
    // It just doesn’t communicate the intent of an Anti Join at all
    public void badEmployeeCustomerViaLeftJoinAndIsNotNull() {

        System.out.println("EXAMPLE 3\n"
                + ctx.selectDistinct(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .leftOuterJoin(CUSTOMER)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER))
                        .where(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER.isNotNull())
                        .fetch()
        );
    }

    // EXAMPLE 4
    /*
    select 
      "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME", 
      "CLASSICMODELS"."EMPLOYEE"."LAST_NAME" 
    from 
      "CLASSICMODELS"."EMPLOYEE" 
    where 
      exists (
        select 
          1 "one" 
        from 
          "CLASSICMODELS"."CUSTOMER" 
        where 
          "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER" = "CLASSICMODELS"."CUSTOMER"."SALES_REP_EMPLOYEE_NUMBER"
      )    
     */
    public void joinEmployeeCustomerViaLeftSemiJoin() {

        System.out.println("EXAMPLE 4\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .leftSemiJoin(CUSTOMER)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER))
                        .fetch()
        );
    }
    
    // EXAMPLE 5
    /*
    select 
      "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME", 
      "CLASSICMODELS"."EMPLOYEE"."LAST_NAME" 
    from 
      "CLASSICMODELS"."EMPLOYEE" 
    where 
      (
        exists (
          select 
            1 "one" 
          from 
            "CLASSICMODELS"."CUSTOMER" 
          where 
            "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER" = "CLASSICMODELS"."CUSTOMER"."SALES_REP_EMPLOYEE_NUMBER"
        ) 
        and exists (
          select 
            1 "one" 
          from 
            "CLASSICMODELS"."SALE" 
          where 
            "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER" = "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER"
        )
      )   
    */
    public void joinEmployeeCustomerSaleViaLeftSemiJoin() {

        System.out.println("EXAMPLE 5\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .leftSemiJoin(CUSTOMER)                        
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER))
                        .leftSemiJoin(SALE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                        .fetch()
        );
    }

    /*
    select
        "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME",
        "CLASSICMODELS"."EMPLOYEE"."LAST_NAME",
        "CLASSICMODELS"."EMPLOYEE"."SALARY" 
    from
        "CLASSICMODELS"."EMPLOYEE" 
    where
        exists (
            select
                1 "one" 
            from
                (select
                    "CLASSICMODELS"."CUSTOMER"."CUSTOMER_NUMBER",
                    "CLASSICMODELS"."CUSTOMER"."SALES_REP_EMPLOYEE_NUMBER" "A" 
                from
                    "CLASSICMODELS"."CUSTOMER" 
                where
                    (
                        exists (
                            select
                                1 "one" 
                            from
                                (select
                                    "CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER" "B" 
                                from
                                    "CLASSICMODELS"."PAYMENT" 
                                where
                                    "CLASSICMODELS"."PAYMENT"."INVOICE_AMOUNT" > ?) "T1" 
                            where
                                "T1"."B" = "CLASSICMODELS"."CUSTOMER"."CUSTOMER_NUMBER"
                            ) 
                            and "CLASSICMODELS"."CUSTOMER"."CREDIT_LIMIT" > ?
                    )
                ) "T2" 
            where
                "T2"."A" = "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER"
            )    
    */
    // EXAMPLE 6
    public void joinEmployeeCustomerPaymentViaLeftSemiJoin() {

        System.out.println("EXAMPLE 6\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .leftSemiJoin(select(CUSTOMER.CUSTOMER_NUMBER,
                                CUSTOMER.SALES_REP_EMPLOYEE_NUMBER.as("A"))
                                .from(CUSTOMER)
                                .leftSemiJoin(select(PAYMENT.CUSTOMER_NUMBER.as("B"))
                                        .from(PAYMENT)
                                        .where(PAYMENT.INVOICE_AMOUNT.gt(BigDecimal.valueOf(100000))).asTable("T1"))
                                .on(field(name("T1", "B")).eq(CUSTOMER.CUSTOMER_NUMBER))
                                .where(CUSTOMER.CREDIT_LIMIT.gt(BigDecimal.ZERO)).asTable("T2")
                        ).on(field(name("T2", "A")).eq(EMPLOYEE.EMPLOYEE_NUMBER))
                        .fetch()
        );
    }
    
    /* ANTI JOIN */
    // EXAMPLE 7
    public void joinEmployeeCustomerViaNotExists() {

        System.out.println("EXAMPLE 7\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .whereNotExists(selectOne().from(CUSTOMER)
                                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER)))
                        .fetch()
        );
    }

    // EXAMPLE 8
    public void joinEmployeeCustomerViaNotIn() {

        System.out.println("EXAMPLE 8\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.EMPLOYEE_NUMBER.notIn(
                                select(nvl(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER, -1L)).from(CUSTOMER))) // nvl() or isnull()
                        .fetch()
        );
    }
    
    // EXAMPLE 9 - ANTI JOIN via LEFT JOIN and IS NULL
    // It just doesn’t communicate the intent of an Anti Join at all
    public void badEmployeeCustomerViaLeftJoinAndIsNull() {

        System.out.println("EXAMPLE 9\n"
                + ctx.selectDistinct(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .leftOuterJoin(CUSTOMER)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER))
                        .where(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER.isNull())
                        .fetch()
        );
    }

    // EXAMPLE 10
    /*
    select 
      "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME", 
      "CLASSICMODELS"."EMPLOYEE"."LAST_NAME" 
    from 
      "CLASSICMODELS"."EMPLOYEE" 
    where 
      not (
        exists (
          select 
            1 "one" 
          from 
            "CLASSICMODELS"."CUSTOMER" 
          where 
            "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER" = "CLASSICMODELS"."CUSTOMER"."SALES_REP_EMPLOYEE_NUMBER"
        )
      )    
     */
    public void joinEmployeeCustomerViaAntiJoin() {

        System.out.println("EXAMPLE 10\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .leftAntiJoin(CUSTOMER)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER))
                        .fetch()
        );
    }
    
    // EXAMPLE 11
    /*
    select 
      "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME", 
      "CLASSICMODELS"."EMPLOYEE"."LAST_NAME" 
    from 
      "CLASSICMODELS"."EMPLOYEE" 
    where 
      (
        not (
          exists (
            select 
              1 "one" 
            from 
              "CLASSICMODELS"."CUSTOMER" 
            where 
              "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER" = "CLASSICMODELS"."CUSTOMER"."SALES_REP_EMPLOYEE_NUMBER"
          )
        ) 
        and not (
          exists (
            select 
              1 "one" 
            from 
              "CLASSICMODELS"."SALE" 
            where 
              "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER" = "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER"
          )
        )
      )    
    */
    public void joinEmployeeCustomerSaleViaAntiJoin() {

        System.out.println("EXAMPLE 11\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .leftAntiJoin(CUSTOMER)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER))
                        .leftAntiJoin(SALE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                        .fetch()
        );
    }            
}