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
      `classicmodels`.`employee`.`last_name`,
      `classicmodels`.`employee`.`first_name`      
    from
      `classicmodels`.`employee`
    where
      `classicmodels`.`employee`.`salary` >= (
    select
      (avg(`classicmodels`.`employee`.`salary`) + ?)
    from
      `classicmodels`.`employee`
     */
    public void findSalaryGeAvgPlus25000() {

        System.out.println("EXAMPLE 1\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.SALARY.coerce(BigDecimal.class).ge(
                                select(avg(EMPLOYEE.SALARY).plus(25000)).from(EMPLOYEE)))
                        .fetch()
        );
    }

    // EXAMPLE 2
    /*
    select
       min(`classicmodels`.`payment`.`invoice_amount`),
       round(
        (
          select
             min(`classicmodels`.`payment`.`invoice_amount`)
          from
             `classicmodels`.`payment`
        ), ?
      ) as `round_min`
    from
      `classicmodels`.`payment`
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
        `classicmodels`.`employee`.`salary` + (
          select
            avg(`classicmodels`.`employee`.`salary`)
          from
            `classicmodels`.`employee`
        )
      ) as `baseSalary`
    from
      `classicmodels`.`employee`
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
      `classicmodels`.`employee`.`last_name`,
      `classicmodels`.`employee`.`first_name`      
    from
      `classicmodels`.`employee`
    where
      `classicmodels`.`employee`.`salary` >= (
        select
          `classicmodels`.`employee`.`salary`
        from
          `classicmodels`.`employee`
        where
          `classicmodels`.`employee`.`employee_number` = ?
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
    insert into `classicmodels`.`product` (
      `product_id`, `product_name`, `product_line`, 
      `code`, `product_scale`, `product_description`, 
      `product_vendor`, `quantity_in_stock`, 
      `buy_price`, `msrp`
    ) 
    values 
      (
        (
          select 
            * 
          from 
            (
              select 
                max(
                  (
                    `classicmodels`.`product`.`product_id` + ?
                  )
                ) 
              from 
                `classicmodels`.`product`
            ) as `t`
        ), 
        ?, ?, 
        (
          select 
            * 
          from 
            (
              select 
                min(
                  `classicmodels`.`product`.`code`
                ) 
              from 
                `classicmodels`.`product` 
              where 
                `classicmodels`.`product`.`product_line` = ?
            ) as `t`
        ), 
        ?, ?, ?, ?, 
        (
          select 
            * 
          from 
            (
              select 
                avg(
                  `classicmodels`.`product`.`buy_price`
                ) 
              from 
                `classicmodels`.`product`
            ) as `t`
        ), 
        (
          select 
            * 
          from 
            (
              select 
                avg(
                  `classicmodels`.`product`.`msrp`
                ) 
              from 
                `classicmodels`.`product`
            ) as `t`
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
      `classicmodels`.`bank_transaction` 
    where 
      `classicmodels`.`bank_transaction`.`customer_number` = (
        select 
          `classicmodels`.`customer`.`customer_number` 
        from 
          `classicmodels`.`customer` 
        where 
          `classicmodels`.`customer`.`customer_name` = ?
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
      `classicmodels`.`employee`
    set
      `classicmodels`.`employee`.`salary` = (
        `classicmodels`.`employee`.`salary` + (
          select *
          from
            (
              select
                min(`classicmodels`.`employee`.`salary`)
              from
                `classicmodels`.`employee`
              where
                `classicmodels`.`employee`.`reports_to` = ?
            ) as `t`
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
