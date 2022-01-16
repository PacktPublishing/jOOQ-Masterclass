package com.classicmodels.repository;

import com.classicmodels.pojo.SalePart;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.PaymentRecord;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.jooq.conf.ExecuteWithoutWhere;
import org.jooq.conf.Settings;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // EXAMPLE 1   
    public void simpleDeletes() {

        // delete from `classicmodels`.`sale` where `classicmodels`.`sale`.`fiscal_year` = ?
        System.out.println("EXAMPLE 1.1 (affected rows): "
                + ctx.delete(SALE)
                        .where(SALE.FISCAL_YEAR.eq(2003))
                        .execute()
        );

        // delete from `classicmodels`.`sale` where `classicmodels`.`sale`.`fiscal_year` = ?
        System.out.println("EXAMPLE 1.2 (affected rows): "
                + ctx.deleteFrom(SALE)
                        .where(SALE.FISCAL_YEAR.eq(2004))
                        .execute()
        );

        // delete from `classicmodels`.`sale` where `classicmodels`.`sale`.`fiscal_year` = ?
        DeleteQuery dq = ctx.deleteQuery(SALE);
        dq.addConditions(SALE.FISCAL_YEAR.eq(2005));
        // dq.execute();
        System.out.println("EXAMPLE 1.3 (query): " + dq.getSQL());
        
        // delete from `classicmodels`.`bank_transaction`
        System.out.println("EXAMPLE 1.4 (affected rows): "
                + ctx.deleteFrom(BANK_TRANSACTION).execute()
        );        
    }

    // EXAMPLE 2
    /*
    delete from
      `classicmodels`.`payment`
    where
    (
      `classicmodels`.`payment`.`customer_number`,
      `classicmodels`.`payment`.`check_number`
    ) = (?, ?)
     */
    public void deletePayment() {

        System.out.println("EXAMPLE 2 (affected rows): "
                + ctx.delete(PAYMENT)
                        .where(row(PAYMENT.CUSTOMER_NUMBER, PAYMENT.CHECK_NUMBER).eq(
                                row(103L, "HQ336336")))
                        .execute()
        );
    }

    // EXAMPLE 3    
    public void deleteCustomerDetailViaNotIn() {

        /*
        delete from 
          `classicmodels`.`customerdetail` 
        where 
          (
            `classicmodels`.`customerdetail`.`postal_code`, 
            `classicmodels`.`customerdetail`.`state`
          ) in (
            select 
              `classicmodels`.`office`.`postal_code`, 
              `classicmodels`.`office`.`state` 
            from 
              `classicmodels`.`office` 
            where 
              `classicmodels`.`office`.`country` = ?
          )        
         */
        System.out.println("EXAMPLE 3.1 (affected rows): "
                + ctx.deleteFrom(CUSTOMERDETAIL)
                        .where(row(CUSTOMERDETAIL.POSTAL_CODE, CUSTOMERDETAIL.STATE).in(
                                select(OFFICE.POSTAL_CODE, OFFICE.STATE)
                                        .from(OFFICE).where(OFFICE.COUNTRY.eq("USA"))
                        )).execute()
        );

        /*
        delete from 
          `classicmodels`.`customerdetail` 
        where 
          (
            `classicmodels`.`customerdetail`.`city`, 
            `classicmodels`.`customerdetail`.`country`
          ) not in (
            (?, ?), 
            (?, ?)
          )        
         */
        System.out.println("EXAMPLE 3.2 (affected rows): "
                + ctx.deleteFrom(CUSTOMERDETAIL)
                        .where(row(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY).notIn(
                                row("Paris", "France"),
                                row("Las Vegas", "USA")
                        )).execute()
        );
    }

    // EXAMPLE 4
    /*
    delete from
      `classicmodels`.`payment`
    where
      `classicmodels`.`payment`.`customer_number` = ?
    order by
      `classicmodels`.`payment`.`invoice_amount` desc
    limit
      ?
     */
    public void deleteOrderByAndLimit() {

        System.out.println("EXAMPLE 4 (affected rows): "
                + ctx.deleteFrom(PAYMENT)
                        .where(PAYMENT.CUSTOMER_NUMBER.eq(114L))
                        .orderBy(PAYMENT.INVOICE_AMOUNT.desc())
                        .limit(1)
                        .execute()
        );
    }

    // EXAMPLE 5   
    public void deleteCascade() {

        /*
        delete from
          `classicmodels`.`orderdetail`
        where
          `classicmodels`.`orderdetail`.`order_id` in (
            select
             `classicmodels`.`order`.`order_id`
            from
             `classicmodels`.`order`
            where
             `classicmodels`.`order`.`customer_number` = ?
          )
         */
        int e1 = ctx.deleteFrom(ORDERDETAIL)
                .where(ORDERDETAIL.ORDER_ID.in(
                        select(ORDER.ORDER_ID).from(ORDER)
                                .where(ORDER.CUSTOMER_NUMBER.eq(103L))))
                .execute();

        /*
        delete from
          `classicmodels`.`order`
        where
          `classicmodels`.`order`.`customer_number` = ?
         */
        int e2 = ctx.deleteFrom(ORDER)
                .where(ORDER.CUSTOMER_NUMBER.eq(103L))
                .execute();

        /*
        delete from
          `classicmodels`.`customerdetail`
        where
          `classicmodels`.`customerdetail`.`customer_number` = ?
         */
        int e3 = ctx.deleteFrom(CUSTOMERDETAIL)
                .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(103L))
                .execute();

        /*
        delete from
          `classicmodels`.`payment`
        where
          `classicmodels`.`payment`.`customer_number` = ?
         */
        int e4 = ctx.deleteFrom(PAYMENT)
                .where(PAYMENT.CUSTOMER_NUMBER.eq(103L))
                .execute();

        /*
        delete from
          `classicmodels`.`customer`
        where
          `classicmodels`.`customer`.`customer_number` = ?
         */
        int e5 = ctx.deleteFrom(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NUMBER.eq(103L))
                .execute();

        System.out.println("EXAMPLE 5 (affected rows): " + (e1 + e2 + e3 + e4 + e5));
    }

    // EXAMPLE 6
    public void deleteRecordImplicitWhere() {

        PaymentRecord pr = new PaymentRecord();
        pr.setCustomerNumber(114L);    // being part of PK, it occurs in the generated WHERE clause
        pr.setCheckNumber("GG31455"); // being part of PK, it occurs in the generated WHERE clause
        pr.setPaymentDate(LocalDateTime.of(2003, 5, 20, 8, 10, 45)); // doesn't occur in the generated WHERE clause
        pr.setCachingDate(LocalDateTime.of(2003, 5, 20, 8, 30, 9)); // doesn't occur in the generated WHERE clause
        pr.setInvoiceAmount(BigDecimal.valueOf(45864.03)); // doesn't occur in the generated WHERE clause

        // or
        // PaymentRecord pr = new PaymentRecord(
        //       114L, "GG31455", LocalDateTime.of(2003,5,20,8,10,45),
        //       BigDecimal.valueOf(45864.03), LocalDateTime.of(2003,5,20,8,30,9), 0, LocalDateTime.now());
        /*
        delete from
          `classicmodels`.`payment`
        where
         (
           `classicmodels`.`payment`.`customer_number` = ?
             and `classicmodels`.`payment`.`check_number` = ?
         )
         */
        System.out.println("EXAMPLE 6.1 (affected rows): "
                + ctx.executeDelete(pr)
        );

        /*
        delete from 
          `classicmodels`.`payment` 
        where 
          `classicmodels`.`payment`.`invoice_amount` = ?       
         */
        System.out.println("EXAMPLE 6.2 (affected rows): "
                + ctx.executeDelete(pr, PAYMENT.INVOICE_AMOUNT.eq(BigDecimal.ZERO))
        );
        
        // user-defined POJO
        /*
        delete from
          `classicmodels`.`sale`
        where
          `classicmodels`.`sale`.`sale_id` = ?
         */
        SalePart sp = new SalePart(14L, BigDecimal.valueOf(1607.76));
        System.out.println("EXAMPLE 6.3 (affected rows): "
                + ctx.executeDelete(ctx.newRecord(SALE, sp))
        );
        
        pr.attach(ctx.configuration()); // attach the record to the current configuration
        System.out.println("EXAMPLE 6.4 (affected rows): "
                +pr.delete()
        );
    }

    // EXAMPLE 7   
    /*
        delete from
          `classicmodels`.`payment`
        where
         (
           `classicmodels`.`payment`.`customer_number` = ?
             and `classicmodels`.`payment`.`check_number` = ?
         )
     */
    public void moreDeleteRecordExamples() {

        System.out.println("EXAMPLE 7.1 (affected rows): "
                + ctx.delete(PAYMENT.from(ctx.newRecord(PAYMENT.CUSTOMER_NUMBER, PAYMENT.CHECK_NUMBER)
                        .values(119L, "DB933704")).getTable())
                        .where(PAYMENT.CUSTOMER_NUMBER.eq(119L)
                                .and(PAYMENT.CHECK_NUMBER.eq("DB933704")))
                        .execute()
        );

        System.out.println("EXAMPLE 7.2 (affected rows): "
                + ctx.executeDelete(PAYMENT.from(ctx.newRecord(PAYMENT)),
                        PAYMENT.CUSTOMER_NUMBER.eq(119L).and(PAYMENT.CHECK_NUMBER.eq("DB933704")))
        );

        System.out.println("EXAMPLE 7.3 (affected rows): "
                + ctx.executeDelete(PAYMENT.from(
                        ctx.newRecord(PAYMENT.CUSTOMER_NUMBER, PAYMENT.CHECK_NUMBER)
                        .values(119L, "DB933704")))
        );
    }

    // EXAMPLE 8
    public void throwExceptionForDeleteWithoutWhereClause() {

        try {
            ctx.configuration().derive(new Settings()
                    .withExecuteDeleteWithoutWhere(ExecuteWithoutWhere.THROW))
                    .dsl()
                    .deleteFrom(SALE)
                    .execute();

            // in production, don't "swallow" the exception as here!
        } catch (org.jooq.exception.DataAccessException e) {
            System.out.println("Execute DELETE without WHERE!");
        }
    }
}