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

        // delete from "CLASSICMODELS"."SALE" where "CLASSICMODELS"."SALE"."FISCAL_YEAR" = ?
        System.out.println("EXAMPLE 1.1 (affected rows): "
                + ctx.delete(SALE)
                        .where(SALE.FISCAL_YEAR.eq(2003))
                        .execute()
        );

        // delete from "CLASSICMODELS"."SALE" where "CLASSICMODELS"."SALE"."FISCAL_YEAR" = ?
        System.out.println("EXAMPLE 1.2 (affected rows): "
                + ctx.deleteFrom(SALE)
                        .where(SALE.FISCAL_YEAR.eq(2004))
                        .execute()
        );

        // delete from "CLASSICMODELS"."SALE" where "CLASSICMODELS"."SALE"."FISCAL_YEAR" = ?
        DeleteQuery dq = ctx.deleteQuery(SALE);
        dq.addConditions(SALE.FISCAL_YEAR.eq(2005));
        // dq.execute();
        System.out.println("EXAMPLE 1.3 (query): " + dq.getSQL());
                
        // delete from "CLASSICMODELS"."BANK_TRANSACTION"
        System.out.println("EXAMPLE 1.4 (affected rows): "
                + ctx.deleteFrom(BANK_TRANSACTION).execute()
        );        
    }

    // EXAMPLE 2
    /*
    delete from 
      "CLASSICMODELS"."PAYMENT" 
    where 
      (
        "CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER", 
        "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER"
      ) = (
        (?, ?)
      )    
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
          "CLASSICMODELS"."CUSTOMERDETAIL" 
        where 
          (
            "CLASSICMODELS"."CUSTOMERDETAIL"."POSTAL_CODE", 
            "CLASSICMODELS"."CUSTOMERDETAIL"."STATE"
          ) in (
            (
              select 
                "CLASSICMODELS"."OFFICE"."POSTAL_CODE", 
                "CLASSICMODELS"."OFFICE"."STATE" 
              from 
                "CLASSICMODELS"."OFFICE" 
              where 
                "CLASSICMODELS"."OFFICE"."COUNTRY" = ?
            )
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
          "CLASSICMODELS"."CUSTOMERDETAIL" 
        where 
          (
            "CLASSICMODELS"."CUSTOMERDETAIL"."CITY", 
            "CLASSICMODELS"."CUSTOMERDETAIL"."COUNTRY"
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
      "CLASSICMODELS"."PAYMENT" 
    where 
      (
        "CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER", 
        "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER"
      ) in (
        (
          select 
            "CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER", 
            "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER" 
          from 
            "CLASSICMODELS"."PAYMENT" 
          where 
            "CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER" = ? 
          order by 
            "CLASSICMODELS"."PAYMENT"."INVOICE_AMOUNT" desc fetch next ? rows only
        )
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
          "CLASSICMODELS"."ORDERDETAIL" 
        where 
          "CLASSICMODELS"."ORDERDETAIL"."ORDER_ID" in (
            select 
              "CLASSICMODELS"."ORDER"."ORDER_ID" 
            from 
              "CLASSICMODELS"."ORDER" 
            where 
              "CLASSICMODELS"."ORDER"."CUSTOMER_NUMBER" = ?
          )        
         */
        int e1 = ctx.deleteFrom(ORDERDETAIL)
                .where(ORDERDETAIL.ORDER_ID.in(
                        select(ORDER.ORDER_ID).from(ORDER)
                                .where(ORDER.CUSTOMER_NUMBER.eq(103L))))
                .execute();

        /*
        delete from 
          "CLASSICMODELS"."ORDER" 
        where 
          "CLASSICMODELS"."ORDER"."CUSTOMER_NUMBER" = ?        
         */
        int e2 = ctx.deleteFrom(ORDER)
                .where(ORDER.CUSTOMER_NUMBER.eq(103L))
                .execute();

        /*
        delete from 
          "CLASSICMODELS"."CUSTOMERDETAIL" 
        where 
          "CLASSICMODELS"."CUSTOMERDETAIL"."CUSTOMER_NUMBER" = ?       
         */
        int e3 = ctx.deleteFrom(CUSTOMERDETAIL)
                .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(103L))
                .execute();

        /*
        delete from 
          "CLASSICMODELS"."PAYMENT" 
        where 
          "CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER" = ?        
         */
        int e4 = ctx.deleteFrom(PAYMENT)
                .where(PAYMENT.CUSTOMER_NUMBER.eq(103L))
                .execute();

        /*
        delete from 
          "CLASSICMODELS"."CUSTOMER" 
        where 
          "CLASSICMODELS"."CUSTOMER"."CUSTOMER_NUMBER" = ?       
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
          "CLASSICMODELS"."PAYMENT" 
        where 
          (
            "CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER" = ? 
            and "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER" = ?
          )        
         */
        System.out.println("EXAMPLE 6.1 (affected rows): "
                + ctx.executeDelete(pr)
        );
        
        /*
        delete from 
          "CLASSICMODELS"."PAYMENT" 
        where 
          "CLASSICMODELS"."PAYMENT"."INVOICE_AMOUNT" = ?       
         */
        System.out.println("EXAMPLE 6.2 (affected rows): "
                + ctx.executeDelete(pr, PAYMENT.INVOICE_AMOUNT.eq(BigDecimal.ZERO))
        );

        // user-defined POJO
        /*
        delete from 
          "CLASSICMODELS"."SALE" 
        where 
          "CLASSICMODELS"."SALE"."SALE_ID" = ?       
         */
        SalePart sp = new SalePart(14L, BigDecimal.valueOf(1607.76));
        System.out.println("EXAMPLE 6.3 (affected rows): "
                + ctx.executeDelete(ctx.newRecord(SALE, sp))
        );
    }

    // EXAMPLE 7   
    /*
    delete from 
      "CLASSICMODELS"."PAYMENT" 
    where 
      (
        "CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER" = ? 
        and "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER" = ?
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
    declare o0 dbms_sql.number_table;
    c0 sys_refcursor;
    begin 
    delete from 
      "CLASSICMODELS"."SALE" 
    where 
      "CLASSICMODELS"."SALE"."SALE_ID" = ? returning "CLASSICMODELS"."SALE"."SALE" bulk collect into o0;
    ? := sql % rowcount;
    open c0 for 
    select 
      * 
    from 
      table(o0);
    ? := c0;
    end;    
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
    declare o0 dbms_sql.number_table;
    o1 dbms_sql.varchar2_table;
    o2 dbms_sql.timestamp_table;
    o3 dbms_sql.number_table;
    o4 dbms_sql.timestamp_table;
    c0 sys_refcursor;
    c1 sys_refcursor;
    c2 sys_refcursor;
    c3 sys_refcursor;
    c4 sys_refcursor;
    begin 
    delete from 
      "CLASSICMODELS"."PAYMENT" 
    where 
      "CLASSICMODELS"."PAYMENT"."INVOICE_AMOUNT" > ? returning "CLASSICMODELS"."PAYMENT"."CUSTOMER_NUMBER", 
      "CLASSICMODELS"."PAYMENT"."CHECK_NUMBER", 
      "CLASSICMODELS"."PAYMENT"."PAYMENT_DATE", 
      "CLASSICMODELS"."PAYMENT"."INVOICE_AMOUNT", 
      "CLASSICMODELS"."PAYMENT"."CACHING_DATE" bulk collect into o0, 
      o1, 
      o2, 
      o3, 
      o4;
    ? := sql % rowcount;
    open c0 for 
    select 
      * 
    from 
      table(o0);
    open c1 for 
    select 
      * 
    from 
      table(o1);
    open c2 for 
    select 
      * 
    from 
      table(o2);
    open c3 for 
    select 
      * 
    from 
      table(o3);
    open c4 for 
    select 
      * 
    from 
      table(o4);
    ? := c0;
    ? := c1;
    ? := c2;
    ? := c3;
    ? := c4;
    end;    
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
    declare o0 DBMS_SQL.NUMBER_TABLE;
    c0 sys_refcursor;
    begin 
    delete from 
      "CLASSICMODELS"."ORDERDETAIL" 
    where 
      "CLASSICMODELS"."ORDERDETAIL"."PRODUCT_ID" in (
        select 
          "CLASSICMODELS"."PRODUCT"."PRODUCT_ID" 
        from 
          "CLASSICMODELS"."PRODUCT" 
        where 
          (
            "CLASSICMODELS"."PRODUCT"."PRODUCT_LINE" = ? 
            or "CLASSICMODELS"."PRODUCT"."PRODUCT_LINE" = ?
          )
      ) returning "CLASSICMODELS"."ORDERDETAIL"."PRODUCT_ID" bulk collect into o0;
    ? := sql % rowcount;
    open c0 for 
    select 
      * 
    from 
      table(o0);
    ? := c0;
    end;
    
    // 2
    declare o0 DBMS_SQL.VARCHAR2_TABLE;
    c0 sys_refcursor;
    begin 
    delete from 
      "CLASSICMODELS"."PRODUCT" 
    where 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_ID" in (?, ?, ?,..., ?) returning "CLASSICMODELS"."PRODUCT"."PRODUCT_LINE" bulk collect into o0;
    ? := sql % rowcount;
    open c0 for 
    select 
      * 
    from 
      table(o0);
    ? := c0;
    end;
    
    // 3
    declare o0 DBMS_SQL.VARCHAR2_TABLE;
    c0 sys_refcursor;
    begin 
    delete from 
      "CLASSICMODELS"."PRODUCTLINEDETAIL" 
    where 
      "CLASSICMODELS"."PRODUCTLINEDETAIL"."PRODUCT_LINE" in (?, ?, ?,..., ?) returning "CLASSICMODELS"."PRODUCTLINEDETAIL"."PRODUCT_LINE" bulk collect into o0;
    ? := sql % rowcount;
    open c0 for 
    select 
      * 
    from 
      table(o0);
    ? := c0;
    end;
    
    // 4
    delete from 
      "CLASSICMODELS"."PRODUCTLINE" 
    where 
      "CLASSICMODELS"."PRODUCTLINE"."PRODUCT_LINE" in (?, ?)        
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