package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
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
      "SYSTEM"."EMPLOYEE"."FIRST_NAME", 
      "SYSTEM"."EMPLOYEE"."LAST_NAME" 
    from 
      "SYSTEM"."EMPLOYEE" 
    where 
      exists (
        select 
          1 "one" 
        from 
          "SYSTEM"."CUSTOMER" 
        where 
          "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER" = "SYSTEM"."CUSTOMER"."SALES_REP_EMPLOYEE_NUMBER"
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
      "SYSTEM"."EMPLOYEE"."FIRST_NAME", 
      "SYSTEM"."EMPLOYEE"."LAST_NAME" 
    from 
      "SYSTEM"."EMPLOYEE" 
    where 
      (
        exists (
          select 
            1 "one" 
          from 
            "SYSTEM"."CUSTOMER" 
          where 
            "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER" = "SYSTEM"."CUSTOMER"."SALES_REP_EMPLOYEE_NUMBER"
        ) 
        and exists (
          select 
            1 "one" 
          from 
            "SYSTEM"."SALE" 
          where 
            "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER" = "SYSTEM"."SALE"."EMPLOYEE_NUMBER"
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
      "SYSTEM"."EMPLOYEE"."FIRST_NAME", 
      "SYSTEM"."EMPLOYEE"."LAST_NAME", 
      "SYSTEM"."EMPLOYEE"."SALARY" 
    from 
      "SYSTEM"."EMPLOYEE" 
    where 
      exists (
        select 
          1 "one" 
        from 
          (
            select 
              "SYSTEM"."CUSTOMER"."CUSTOMER_NUMBER", 
              "SYSTEM"."CUSTOMER"."SALES_REP_EMPLOYEE_NUMBER" "A" 
            from 
              "SYSTEM"."CUSTOMER" 
            where 
              (
                exists (
                  select 
                    1 "one" 
                  from 
                    (
                      select 
                        "SYSTEM"."PAYMENT"."CUSTOMER_NUMBER" "B" 
                      from 
                        "SYSTEM"."PAYMENT" 
                      where 
                        "SYSTEM"."PAYMENT"."INVOICE_AMOUNT" > ?
                    ) "alias_130796209" 
                  where 
                    B = "SYSTEM"."CUSTOMER"."CUSTOMER_NUMBER"
                ) 
                and "SYSTEM"."CUSTOMER"."CREDIT_LIMIT" > ?
              )
          ) "alias_80623592" 
        where 
          A = "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER"
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
                                        .where(PAYMENT.INVOICE_AMOUNT.gt(BigDecimal.valueOf(100000))))
                                .on(field("B").eq(CUSTOMER.CUSTOMER_NUMBER))
                                .where(CUSTOMER.CREDIT_LIMIT.gt(BigDecimal.ZERO))
                        ).on(field("A").eq(EMPLOYEE.EMPLOYEE_NUMBER))
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
      "SYSTEM"."EMPLOYEE"."FIRST_NAME", 
      "SYSTEM"."EMPLOYEE"."LAST_NAME" 
    from 
      "SYSTEM"."EMPLOYEE" 
    where 
      not (
        exists (
          select 
            1 "one" 
          from 
            "SYSTEM"."CUSTOMER" 
          where 
            "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER" = "SYSTEM"."CUSTOMER"."SALES_REP_EMPLOYEE_NUMBER"
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
      "SYSTEM"."EMPLOYEE"."FIRST_NAME", 
      "SYSTEM"."EMPLOYEE"."LAST_NAME" 
    from 
      "SYSTEM"."EMPLOYEE" 
    where 
      (
        not (
          exists (
            select 
              1 "one" 
            from 
              "SYSTEM"."CUSTOMER" 
            where 
              "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER" = "SYSTEM"."CUSTOMER"."SALES_REP_EMPLOYEE_NUMBER"
          )
        ) 
        and not (
          exists (
            select 
              1 "one" 
            from 
              "SYSTEM"."SALE" 
            where 
              "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER" = "SYSTEM"."SALE"."EMPLOYEE_NUMBER"
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