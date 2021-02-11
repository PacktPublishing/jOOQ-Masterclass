package com.classicmodels.repository;

import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Payment.PAYMENT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.min;
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

    // EXAMPLE 1
    /*
    select       
      "SYSTEM"."EMPLOYEE"."LAST_NAME", 
      "SYSTEM"."EMPLOYEE"."FIRST_NAME"
    from 
      "SYSTEM"."EMPLOYEE" 
    where 
      "SYSTEM"."EMPLOYEE"."SALARY" >= (
        select 
          (
            avg("SYSTEM"."EMPLOYEE"."SALARY") + ?
          ) 
        from 
          "SYSTEM"."EMPLOYEE"
      )
     */
    public void findSalaryGeAvgPlus25000() {

        System.out.println("EXAMPLE 1\n" +
                ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.SALARY.ge(
                                select(avg(EMPLOYEE.SALARY).plus(25000)).from(EMPLOYEE).asField()))
                        .fetch()
        );
    }

    // EXAMPLE 2
    /*
    select 
      min(
        "SYSTEM"."PAYMENT"."INVOICE_AMOUNT"
      ), 
      round(
        (
          select 
            min(
              "SYSTEM"."PAYMENT"."INVOICE_AMOUNT"
            ) 
          from 
            "SYSTEM"."PAYMENT"
        ), 
        ?
      ) "round_min" 
    from 
      "SYSTEM"."PAYMENT" 
    group by 
      "SYSTEM"."PAYMENT"."INVOICE_AMOUNT"    
     */
    public void findMinAndRoundMinInvoiceAmount() {

        System.out.println("EXAMPLE 2\n" +
                ctx.select(min(PAYMENT.INVOICE_AMOUNT),
                        round(select(min(PAYMENT.INVOICE_AMOUNT))
                                .from(PAYMENT).asField(), 0).as("round_min"))
                        .from(PAYMENT)
                        .groupBy(PAYMENT.INVOICE_AMOUNT)
                        .fetch()
        );
    }

    // EXAMPLE 3
    /*
    select 
      (
        "SYSTEM"."EMPLOYEE"."SALARY" + (
          select 
            avg("SYSTEM"."EMPLOYEE"."SALARY") 
          from 
            "SYSTEM"."EMPLOYEE"
        )
      ) "baseSalary" 
    from 
      "SYSTEM"."EMPLOYEE"    
     */
    public void findBaseSalary() {

        System.out.println("EXAMPLE 3\n" +
                ctx.select(EMPLOYEE.SALARY.plus(
                        select(avg(EMPLOYEE.SALARY)).from(EMPLOYEE).asField()).as("baseSalary"))
                        .from(EMPLOYEE)
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
      "SYSTEM"."EMPLOYEE"."SALARY" >= (
        select 
          "SYSTEM"."EMPLOYEE"."SALARY" 
        from 
          "SYSTEM"."EMPLOYEE" 
        where 
          "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER" = ?
      )      
     */
    public void findEmployeeWithSalaryGt() {

        System.out.println("EXAMPLE 4\n" +
                ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.SALARY.ge(
                                select(EMPLOYEE.SALARY).from(EMPLOYEE).
                                        where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1076L))))
                        .fetch()
        );
    }

    // EXAMPLE 5
    /*
    insert into "SYSTEM"."EMPLOYEE" (
      "EMPLOYEE_NUMBER", "LAST_NAME", "FIRST_NAME", 
      "EXTENSION", "EMAIL", "OFFICE_CODE", 
      "SALARY", "REPORTS_TO", "JOB_TITLE", 
      "EMPLOYEE_OF_YEAR", "MONTHLY_BONUS"
    ) 
    values 
      (
        (
          select 
            max(
              (
                "SYSTEM"."EMPLOYEE"."EMPLOYEE_NUMBER" + ?
              )
            ) 
          from 
            "SYSTEM"."EMPLOYEE"
        ), 
        ?, ?, ?, ?, ?, 
        (
          select 
            avg("SYSTEM"."EMPLOYEE"."SALARY") 
          from 
            "SYSTEM"."EMPLOYEE"
        ), 
        ?, ?, ?, ?
      )    
     */
    @Transactional
    public void insertEmployee() {
        
        System.out.println("EXAMPLE 5 (affected rows): " +
                + ctx.insertInto(EMPLOYEE)
                        .values(select(max(EMPLOYEE.EMPLOYEE_NUMBER.plus(1))).from(EMPLOYEE),
                                "Mark", "Janel", "x4443", "markjanel@classicmodelcars.com", "1",
                                select(avg(EMPLOYEE.SALARY)).from(EMPLOYEE),
                                1002L, "VP Of Engineering", null, null)
                        .execute()
        );
    }

    // EXAMPLE 6
    /*
    delete from 
      "SYSTEM"."PAYMENT" 
    where 
      "SYSTEM"."PAYMENT"."CUSTOMER_NUMBER" = (
        select 
          "SYSTEM"."CUSTOMER"."CUSTOMER_NUMBER" 
        from 
          "SYSTEM"."CUSTOMER" 
        where 
          "SYSTEM"."CUSTOMER"."CUSTOMER_NAME" = ?
      )    
     */
    @Transactional
    public void deletePaymentsOfAtelierGraphique() {

        System.out.println("EXAMPLE 6 (affected rows): " +
                + ctx.deleteFrom(PAYMENT)
                        .where(PAYMENT.CUSTOMER_NUMBER.eq(
                                select(CUSTOMER.CUSTOMER_NUMBER).from(CUSTOMER)
                                        .where(CUSTOMER.CUSTOMER_NAME.eq("Atelier graphique"))
                        )).execute()
        );
    }

    // EXAMPLE 7
    /*
    update 
      "SYSTEM"."EMPLOYEE" 
    set 
      "SYSTEM"."EMPLOYEE"."SALARY" = (
        "SYSTEM"."EMPLOYEE"."SALARY" + (
          select 
            min("SYSTEM"."EMPLOYEE"."SALARY") 
          from 
            "SYSTEM"."EMPLOYEE" 
          where 
            "SYSTEM"."EMPLOYEE"."REPORTS_TO" = ?
        )
      )    
    */
    @Transactional
    public void updateEmployeeSalary() {
        System.out.println("EXAMPLE 7 (affected rows): " +
                + ctx.update(EMPLOYEE)
                        .set(EMPLOYEE.SALARY,
                                EMPLOYEE.SALARY.plus(select(min(EMPLOYEE.SALARY)).from(EMPLOYEE)
                                        .where(EMPLOYEE.REPORTS_TO.eq(1002L)).asField()))
                        .execute()
        );

    }
}