package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.round;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.val;
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
      "CLASSICMODELS"."EMPLOYEE"."FIRST_NAME", 
      "CLASSICMODELS"."EMPLOYEE"."LAST_NAME" 
    from 
      "CLASSICMODELS"."EMPLOYEE" 
    where 
      "CLASSICMODELS"."EMPLOYEE"."SALARY" >= (
        select 
          (
            avg(
              "CLASSICMODELS"."EMPLOYEE"."SALARY"
            ) + ?
          ) 
        from 
          "CLASSICMODELS"."EMPLOYEE"
      )    
     */
    public void findSalaryGeAvgPlus25000() {

        System.out.println("EXAMPLE 1\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.SALARY.coerce(BigDecimal.class)
                                .ge(select(avg(EMPLOYEE.SALARY).plus(25000)).from(EMPLOYEE)))
                        .fetch()
        );
    }

    // EXAMPLE 2
    /*
    select 
      min(
        "CLASSICMODELS"."PAYMENT"."INVOICE_AMOUNT"
      ), 
      round(
        (
          select 
            min(
              "CLASSICMODELS"."PAYMENT"."INVOICE_AMOUNT"
            ) 
          from 
            "CLASSICMODELS"."PAYMENT"
        ), 
        ?
      ) "round_min" 
    from 
      "CLASSICMODELS"."PAYMENT" 
    group by 
      "CLASSICMODELS"."PAYMENT"."INVOICE_AMOUNT"    
     */
    public void findMinAndRoundMinInvoiceAmount() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select(min(PAYMENT.INVOICE_AMOUNT),
                        round(field(select(min(PAYMENT.INVOICE_AMOUNT))
                                .from(PAYMENT)), 0).as("round_min"))
                        .from(PAYMENT)
                        .groupBy(PAYMENT.INVOICE_AMOUNT)
                        .fetch()
        );
    }

    // EXAMPLE 3
    /*
    select 
      (
        "CLASSICMODELS"."EMPLOYEE"."SALARY" + (
          select 
            avg(
              "CLASSICMODELS"."EMPLOYEE"."SALARY"
            ) 
          from 
            "CLASSICMODELS"."EMPLOYEE"
        )
      ) "baseSalary" 
    from 
      "CLASSICMODELS"."EMPLOYEE"    
     */
    public void findBaseSalary() {

        System.out.println("EXAMPLE 3\n"
                + ctx.select(EMPLOYEE.SALARY.plus(
                        field(select(avg(EMPLOYEE.SALARY)).from(EMPLOYEE))).as("baseSalary"))
                        .from(EMPLOYEE)
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
      "CLASSICMODELS"."EMPLOYEE"."SALARY" >= (
        select 
          "CLASSICMODELS"."EMPLOYEE"."SALARY" 
        from 
          "CLASSICMODELS"."EMPLOYEE" 
        where 
          "CLASSICMODELS"."EMPLOYEE"."EMPLOYEE_NUMBER" = ?
      )    
     */
    public void findEmployeeWithSalaryGt() {

        System.out.println("EXAMPLE 4\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.SALARY.ge(
                                select(EMPLOYEE.SALARY).from(EMPLOYEE).
                                        where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1076L))))
                        .fetch()
        );
    }

    // EXAMPLE 5
    /*
    insert into "CLASSICMODELS"."PRODUCT" (
      "PRODUCT_ID", "PRODUCT_NAME", "PRODUCT_LINE", 
      "CODE", "PRODUCT_SCALE", "PRODUCT_DESCRIPTION", 
      "PRODUCT_VENDOR", "QUANTITY_IN_STOCK", 
      "BUY_PRICE", "MSRP"
    ) 
    values 
      (
        (
          select 
            max(
              (
                "CLASSICMODELS"."PRODUCT"."PRODUCT_ID" + ?
              )
            ) 
          from 
            "CLASSICMODELS"."PRODUCT"
        ), 
        ?, ?, 
        (
          select 
            min(
              "CLASSICMODELS"."PRODUCT"."CODE"
            ) 
          from 
            "CLASSICMODELS"."PRODUCT" 
          where 
            "CLASSICMODELS"."PRODUCT"."PRODUCT_LINE" = ?
        ), 
        ?, ?, ?, ?, 
        (
          select 
            avg(
              "CLASSICMODELS"."PRODUCT"."BUY_PRICE"
            ) 
          from 
            "CLASSICMODELS"."PRODUCT"
        ), 
        (
          select 
            avg(
              "CLASSICMODELS"."PRODUCT"."MSRP"
            ) 
          from 
            "CLASSICMODELS"."PRODUCT"
        )
      )    
     */
    @Transactional
    public void insertProduct() {

        System.out.println("EXAMPLE 5 (affected rows): "
                + ctx.insertInto(PRODUCT,
                        PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_LINE,
                        PRODUCT.CODE, PRODUCT.PRODUCT_SCALE, PRODUCT.PRODUCT_DESCRIPTION,
                        PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK,
                        PRODUCT.BUY_PRICE, PRODUCT.MSRP)
                        .values(field(select(max(PRODUCT.PRODUCT_ID.plus(1))).from(PRODUCT)),
                                val("1985s Green Bree Helicopter"), val("Planes"),
                                field(select(min(PRODUCT.CODE)).from(PRODUCT)
                                        .where(PRODUCT.PRODUCT_LINE.eq("Planes"))),
                                val("1:10"), val("Red Start Diecast"), val("PENDING"), val(0),
                                field(select(avg(PRODUCT.BUY_PRICE)).from(PRODUCT)),
                                field(select(avg(PRODUCT.MSRP)).from(PRODUCT)))
                        .execute()
        );
    }

    // EXAMPLE 6
    /*
    delete from 
      "CLASSICMODELS"."BANK_TRANSACTION" 
    where 
      "CLASSICMODELS"."BANK_TRANSACTION"."CUSTOMER_NUMBER" = (
        select 
          "CLASSICMODELS"."CUSTOMER"."CUSTOMER_NUMBER" 
        from 
          "CLASSICMODELS"."CUSTOMER" 
        where 
          "CLASSICMODELS"."CUSTOMER"."CUSTOMER_NAME" = ?
      )    
     */
    @Transactional
    public void deleteBankTransactionsOfAtelierGraphique() {

        System.out.println("EXAMPLE 6 (affected rows): "
                + ctx.deleteFrom(BANK_TRANSACTION)
                        .where(BANK_TRANSACTION.CUSTOMER_NUMBER.eq(
                                select(CUSTOMER.CUSTOMER_NUMBER).from(CUSTOMER)
                                        .where(CUSTOMER.CUSTOMER_NAME.eq("Atelier graphique"))
                        )).execute()
        );
    }

    // EXAMPLE 7
    /*
    update 
      "CLASSICMODELS"."EMPLOYEE" 
    set 
      "CLASSICMODELS"."EMPLOYEE"."SALARY" = (
        "CLASSICMODELS"."EMPLOYEE"."SALARY" + (
          select 
            min(
              "CLASSICMODELS"."EMPLOYEE"."SALARY"
            ) 
          from 
            "CLASSICMODELS"."EMPLOYEE" 
          where 
            "CLASSICMODELS"."EMPLOYEE"."REPORTS_TO" = ?
        )
      )    
     */
    @Transactional
    public void updateEmployeeSalary() {
        System.out.println("EXAMPLE 7 (affected rows): "
                + ctx.update(EMPLOYEE)
                        .set(EMPLOYEE.SALARY,
                                EMPLOYEE.SALARY.plus(field(select(min(EMPLOYEE.SALARY)).from(EMPLOYEE)
                                        .where(EMPLOYEE.REPORTS_TO.eq(1002L)))))
                        .execute()
        );

    }
}