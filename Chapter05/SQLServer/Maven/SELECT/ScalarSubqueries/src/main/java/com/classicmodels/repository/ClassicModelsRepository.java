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
      [classicmodels].[dbo].[employee].[last_name], 
      [classicmodels].[dbo].[employee].[first_name]
    from 
      [classicmodels].[dbo].[employee] 
    where 
      [classicmodels].[dbo].[employee].[salary] >= (
        select 
          (
            avg(
              [classicmodels].[dbo].[employee].[salary]
            ) + ?
          ) 
        from 
          [classicmodels].[dbo].[employee]
      )    
     */
    public void findSalaryGeAvgPlus25000() {

        System.out.println("EXAMPLE 1\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.SALARY.coerce(BigDecimal.class)
                                .ge(select(avg(EMPLOYEE.SALARY).plus(25000))
                                        .from(EMPLOYEE)))
                                .fetch()
                        );
    }

    // EXAMPLE 2
    /*
    select 
      min(
        [classicmodels].[dbo].[payment].[invoice_amount]
      ), 
      round(
        (
          select 
            min(
              [classicmodels].[dbo].[payment].[invoice_amount]
            ) 
          from 
            [classicmodels].[dbo].[payment]
        ), 
        ?
      ) [round_min] 
    from 
      [classicmodels].[dbo].[payment]    
     */
    public void findMinAndRoundMinInvoiceAmount() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select(min(PAYMENT.INVOICE_AMOUNT),
                        round(field(select(min(PAYMENT.INVOICE_AMOUNT))
                                .from(PAYMENT)), 0).as("round_min"))
                        .from(PAYMENT)
                        .fetch()
        );
    }

    // EXAMPLE 3
    /*
    select 
      (
        [classicmodels].[dbo].[employee].[salary] + (
          select 
            avg(
              [classicmodels].[dbo].[employee].[salary]
            ) 
          from 
            [classicmodels].[dbo].[employee]
        )
      ) [baseSalary] 
    from 
      [classicmodels].[dbo].[employee]    
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
      [classicmodels].[dbo].[employee].[last_name], 
      [classicmodels].[dbo].[employee].[first_name]
    from 
      [classicmodels].[dbo].[employee] 
    where 
      [classicmodels].[dbo].[employee].[salary] >= (
        select 
          [classicmodels].[dbo].[employee].[salary] 
        from 
          [classicmodels].[dbo].[employee] 
        where 
          [classicmodels].[dbo].[employee].[employee_number] = ?
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
    insert into [classicmodels].[dbo].[product] (
      [product_name], [product_line], [code], 
      [product_scale], [product_description], 
      [product_vendor], [quantity_in_stock], 
      [buy_price], [msrp]
    ) 
    values 
      (
        ?, ?, 
        (
          select 
            min(
              [classicmodels].[dbo].[product].[code]
            ) 
          from 
            [classicmodels].[dbo].[product] 
          where 
            [classicmodels].[dbo].[product].[product_line] = ?
        ), 
        ?, ?, ?, ?, 
        (
          select 
            avg(
              [classicmodels].[dbo].[product].[buy_price]
            ) 
          from 
            [classicmodels].[dbo].[product]
        ), 
        (
          select 
            avg(
              [classicmodels].[dbo].[product].[msrp]
            ) 
          from 
            [classicmodels].[dbo].[product]
        )
      )    
     */
    @Transactional
    public void insertProduct() {

        System.out.println("EXAMPLE 5 (affected rows): "
                + ctx.insertInto(PRODUCT, 
                        PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_LINE,
                        PRODUCT.CODE, PRODUCT.PRODUCT_SCALE, PRODUCT.PRODUCT_DESCRIPTION,
                        PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK,
                        PRODUCT.BUY_PRICE, PRODUCT.MSRP)
                        .values(val("1985s Green Bree Helicopter"), val("Planes"),
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
      [classicmodels].[dbo].[bank_transaction] 
    where 
      [classicmodels].[dbo].[bank_transaction].[customer_number] = (
        select 
          [classicmodels].[dbo].[customer].[customer_number] 
        from 
          [classicmodels].[dbo].[customer] 
        where 
          [classicmodels].[dbo].[customer].[customer_name] = ?
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
      [classicmodels].[dbo].[employee] 
    set 
      [classicmodels].[dbo].[employee].[salary] = (
        [classicmodels].[dbo].[employee].[salary] + (
          select 
            min(
              [classicmodels].[dbo].[employee].[salary]
            ) 
          from 
            [classicmodels].[dbo].[employee] 
          where 
            [classicmodels].[dbo].[employee].[reports_to] = ?
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
