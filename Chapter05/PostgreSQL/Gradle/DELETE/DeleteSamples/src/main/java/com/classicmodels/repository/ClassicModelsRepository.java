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
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import static jooq.generated.tables.Productlinedetail.PRODUCTLINEDETAIL;
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

        // delete from "public"."sale" where "public"."sale"."fiscal_year" = ?
        System.out.println("EXAMPLE 1.1 (affected rows): "
                + ctx.delete(SALE)
                        .where(SALE.FISCAL_YEAR.eq(2003))
                        .execute()
        );

        // delete from "public"."sale" where "public"."sale"."fiscal_year" = ?
        System.out.println("EXAMPLE 1.2 (affected rows): "
                + ctx.deleteFrom(SALE)
                        .where(SALE.FISCAL_YEAR.eq(2004))
                        .execute()
        );

        // delete from "public"."sale" where "public"."sale"."fiscal_year" = ?
        DeleteQuery dq = ctx.deleteQuery(SALE);
        dq.addConditions(SALE.FISCAL_YEAR.eq(2005));
        // dq.execute();
        System.out.println("EXAMPLE 1.3 (query): " + dq.getSQL());

        // delete from "public"."bank_transaction"
        System.out.println("EXAMPLE 1.4 (affected rows): "
                + ctx.deleteFrom(BANK_TRANSACTION).execute()
        );        
    }

    // EXAMPLE 2
    /*
    delete from
      "public"."payment"
    where
    (
      "public"."payment"."customer_number",
      "public"."payment"."check_number"
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
          "public"."customerdetail" 
        where 
          (
            "public"."customerdetail"."postal_code", 
            "public"."customerdetail"."state"
          ) in (
            select 
              "public"."office"."postal_code", 
              "public"."office"."state" 
            from 
              "public"."office" 
            where 
              "public"."office"."country" = ?
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
          "public"."customerdetail" 
        where 
          (
            "public"."customerdetail"."city", 
            "public"."customerdetail"."country"
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
      "public"."payment"
    where
    (
      "public"."payment"."customer_number",
      "public"."payment"."check_number"
    ) in (
      select
        "public"."payment"."customer_number",
        "public"."payment"."check_number"
      from
        "public"."payment"
      where
        "public"."payment"."customer_number" = ?
      order by
        "public"."payment"."invoice_amount" desc
      limit
        ?
    )
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
          "public"."orderdetail"
        where
          "public"."orderdetail"."order_id" in (
            select
              "public"."order"."order_id"
            from
              "public"."order"
            where
              "public"."order"."customer_number" = ?
          )
         */
        int e1 = ctx.deleteFrom(ORDERDETAIL)
                .where(ORDERDETAIL.ORDER_ID.in(
                        select(ORDER.ORDER_ID).from(ORDER)
                                .where(ORDER.CUSTOMER_NUMBER.eq(103L))))
                .execute();

        /*
        delete from
          "public"."order"
        where
          "public"."order"."customer_number" = ?
         */
        int e2 = ctx.deleteFrom(ORDER)
                .where(ORDER.CUSTOMER_NUMBER.eq(103L))
                .execute();

        /*
        delete from
          "public"."customerdetail"
        where
          "public"."customerdetail"."customer_number" = ?
         */
        int e3 = ctx.deleteFrom(CUSTOMERDETAIL)
                .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(103L))
                .execute();

        /*
        delete from
          "public"."payment"
        where
          "public"."payment"."customer_number" = ?
         */
        int e4 = ctx.deleteFrom(PAYMENT)
                .where(PAYMENT.CUSTOMER_NUMBER.eq(103L))
                .execute();

        /*
        delete from
          "public"."customer"
        where
          "public"."customer"."customer_number" = ?
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
        //        114L, "GG31455", LocalDateTime.of(2003,5,20,8,10,45),
        //        BigDecimal.valueOf(45864.03), LocalDateTime.of(2003,5,20,8,30,9));
        /*
        delete from
          "public"."payment"
        where
          (
            "public"."payment"."customer_number" = ?
               and "public"."payment"."check_number" = ?
          )
         */
        System.out.println("EXAMPLE 6.1 (affected rows): "
                + ctx.executeDelete(pr)
        );

        /*
        delete from 
          "public"."payment" 
        where 
          "public"."payment"."invoice_amount" = ?       
         */
        System.out.println("EXAMPLE 6.2 (affected rows): "
                + ctx.executeDelete(pr, PAYMENT.INVOICE_AMOUNT.eq(BigDecimal.ZERO))
        );

        // user-defined POJO
        /*
        delete from
          "public"."sale"
        where
          "public"."sale"."sale_id" = ?
         */
        SalePart sp = new SalePart(14L, BigDecimal.valueOf(1607.76));
        System.out.println("EXAMPLE 6.3 (affected rows): "
                + ctx.executeDelete(ctx.newRecord(SALE, sp))
        );
    }

    // EXAMPLE 7   
    /*
    delete from
      "public"."payment"
    where
     (
       "public"."payment"."customer_number" = ?
          and "public"."payment"."check_number" = ?
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
                    .withExecuteDeleteWithoutWhere(ExecuteWithoutWhere.THROW)) // check other options beside THROW
                    .dsl()
                    .deleteFrom(SALE)
                    .execute();

            // in production, don't "swallow" the exception as here!
        } catch (org.jooq.exception.DataAccessException e) {
            System.out.println("Execute DELETE without WHERE!");
        }
    }

    // EXAMPLE 9
    /*
    delete from
      "public"."sale"
    where
      "public"."sale"."sale_id" = ? 
    returning 
      "public"."sale"."sale"
     */
    public void deleteSaleReturning() {

        System.out.println("EXAMPLE 9 (deleted sale): \n"
                + ctx.delete(SALE)
                        .where(SALE.SALE_ID.eq(15L))
                        .returningResult(SALE.SALE_)
                        .fetchOne()
        );
    }

    // EXAMPLE 10
    /*
    delete from
      "public"."payment"
    where
      "public"."payment"."invoice_amount" > ? 
    returning 
      "public"."payment"."customer_number",
      "public"."payment"."check_number",
      "public"."payment"."payment_date",
      "public"."payment"."invoice_amount",
      "public"."payment"."caching_date"
     */
    public void deletePaymentReturning() {

        System.out.println("EXAMPLE 10 (deleted payment): \n"
                + ctx.delete(PAYMENT)
                        .where(PAYMENT.INVOICE_AMOUNT.gt(BigDecimal.valueOf(100000)))
                        .returningResult()
                        .fetch() // Result<PaymentRecord>
        );
    }

    // EXAMPLE 11
    /*
    // 1
    delete from 
      "public"."orderdetail" 
    where 
      "public"."orderdetail"."product_id" in (
        select 
          "public"."product"."product_id" 
        from 
          "public"."product" 
        where 
          (
            "public"."product"."product_line" = ? 
            or "public"."product"."product_line" = ?
          )
      ) returning "public"."orderdetail"."product_id"
    
    // 2
    delete from 
      "public"."product" 
    where 
      "public"."product"."product_id" in (?, ?, ?,..., ?) returning "public"."product"."product_line"
   
    // 3
    delete from 
      "public"."productlinedetail" 
    where 
      "public"."productlinedetail"."product_line" in (
        ?, ?, ?, ... , ?
      ) returning "public"."productlinedetail"."product_line"
    
    // 4
    delete from 
      "public"."productline" 
    where 
      "public"."productline"."product_line" in (?, ?)    
     */
    public void deleteCascadeReturningProductLineMotorcyclesAndTrucksAndBuses() {

        // Of course, even if this is possible, use it carefully!
        System.out.println("EXAMPLE 11 (affected rows): "
                + ctx.delete(PRODUCTLINE)
                        .where(PRODUCTLINE.PRODUCT_LINE.in(
                                ctx.delete(PRODUCTLINEDETAIL)
                                        .where(PRODUCTLINEDETAIL.PRODUCT_LINE.in(
                                                ctx.delete(PRODUCT)
                                                        .where(PRODUCT.PRODUCT_ID.in(
                                                                ctx.delete(ORDERDETAIL)
                                                                        .where(ORDERDETAIL.PRODUCT_ID.in(
                                                                                select(PRODUCT.PRODUCT_ID).from(PRODUCT)
                                                                                        .where(PRODUCT.PRODUCT_LINE.eq("Motorcycles")
                                                                                                .or(PRODUCT.PRODUCT_LINE.eq("Trucks and Buses")))))
                                                                        .returningResult(ORDERDETAIL.PRODUCT_ID).fetch()))
                                                        .returningResult(PRODUCT.PRODUCT_LINE).fetch()))
                                        .returningResult(PRODUCTLINEDETAIL.PRODUCT_LINE).fetch()))
                        .execute()
        );
    }
}
