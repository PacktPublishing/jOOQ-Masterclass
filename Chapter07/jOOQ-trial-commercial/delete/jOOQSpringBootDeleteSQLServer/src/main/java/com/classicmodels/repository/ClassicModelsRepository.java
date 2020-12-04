package com.classicmodels.repository;

import com.classicmodels.pojo.SalePart;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
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
    /*
    delete from 
      [classicmodels].[dbo].[sale] 
    where 
      [classicmodels].[dbo].[sale].[fiscal_year] = ?    
     */
    public void deleteSale() {

        System.out.println("EXAMPLE 1.1 (affected rows): "
                + ctx.delete(SALE)
                        .where(SALE.FISCAL_YEAR.eq(2003))
                        .execute()
        );

        System.out.println("EXAMPLE 1.2 (affected rows): "
                + ctx.deleteFrom(SALE)
                        .where(SALE.FISCAL_YEAR.eq(2004))
                        .execute()
        );

        DeleteQuery dq = ctx.deleteQuery(SALE);
        dq.addConditions(SALE.FISCAL_YEAR.eq(2005));
        // dq.execute();
        System.out.println("EXAMPLE 1.3 (query): " + dq.getSQL());
    }

    // EXAMPLE 2
    /*
    delete from 
      [classicmodels].[dbo].[payment] 
    where 
      (
        [classicmodels].[dbo].[payment].[customer_number] = ? 
        and [classicmodels].[dbo].[payment].[check_number] = ?
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
          [classicmodels].[dbo].[customerdetail] 
        where 
          exists (
            select 
              [alias_1].[v0], 
              [alias_1].[v1] 
            from 
              (
                select 
                  [classicmodels].[dbo].[office].[postal_code] [v0], 
                  [classicmodels].[dbo].[office].[state] [v1] 
                from 
                  [classicmodels].[dbo].[office] 
                where 
                  [classicmodels].[dbo].[office].[country] = ?
              ) [alias_1] 
            where 
              (
                [classicmodels].[dbo].[customerdetail].[postal_code] = [alias_1].[v0] 
                and [classicmodels].[dbo].[customerdetail].[state] = [alias_1].[v1]
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
          [classicmodels].[dbo].[customerdetail] 
        where 
          not (
            (
              (
                [classicmodels].[dbo].[customerdetail].[city] = ? 
                and [classicmodels].[dbo].[customerdetail].[country] = ?
              ) 
              or (
                [classicmodels].[dbo].[customerdetail].[city] = ? 
                and [classicmodels].[dbo].[customerdetail].[country] = ?
              )
            )
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
      [classicmodels].[dbo].[payment] 
    where 
      exists (
        select 
          [alias_1].[v0], 
          [alias_1].[v1] 
        from 
          (
            select 
              top 1 [classicmodels].[dbo].[payment].[customer_number], 
              [classicmodels].[dbo].[payment].[check_number] 
            from 
              [classicmodels].[dbo].[payment] 
            where 
              [classicmodels].[dbo].[payment].[customer_number] = ? 
            order by 
              [classicmodels].[dbo].[payment].[invoice_amount] desc
          ) [alias_1] ([v0], [v1]) 
        where 
          (
            [classicmodels].[dbo].[payment].[customer_number] = [alias_1].[v0] 
            and [classicmodels].[dbo].[payment].[check_number] = [alias_1].[v1]
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
          [classicmodels].[dbo].[orderdetail] 
        where 
          [classicmodels].[dbo].[orderdetail].[order_id] in (
            select 
              [classicmodels].[dbo].[order].[order_id] 
            from 
              [classicmodels].[dbo].[order] 
            where 
              [classicmodels].[dbo].[order].[customer_number] = ?
          )        
         */
        int e1 = ctx.deleteFrom(ORDERDETAIL)
                .where(ORDERDETAIL.ORDER_ID.in(
                        select(ORDER.ORDER_ID).from(ORDER)
                                .where(ORDER.CUSTOMER_NUMBER.eq(103L))))
                .execute();

        /*
        delete from 
          [classicmodels].[dbo].[order] 
        where 
          [classicmodels].[dbo].[order].[customer_number] = ?        
         */
        int e2 = ctx.deleteFrom(ORDER)
                .where(ORDER.CUSTOMER_NUMBER.eq(103L))
                .execute();

        /*
        delete from 
          [classicmodels].[dbo].[customerdetail] 
        where 
          [classicmodels].[dbo].[customerdetail].[customer_number] = ?        
         */
        int e3 = ctx.deleteFrom(CUSTOMERDETAIL)
                .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(103L))
                .execute();

        /*
        delete from 
          [classicmodels].[dbo].[payment] 
        where 
          [classicmodels].[dbo].[payment].[customer_number] = ?        
         */
        int e4 = ctx.deleteFrom(PAYMENT)
                .where(PAYMENT.CUSTOMER_NUMBER.eq(103L))
                .execute();

        /*
        delete from 
          [classicmodels].[dbo].[customer] 
        where 
          [classicmodels].[dbo].[customer].[customer_number] = ?        
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
          [classicmodels].[dbo].[payment] 
        where 
          (
            [classicmodels].[dbo].[payment].[customer_number] = ? 
            and [classicmodels].[dbo].[payment].[check_number] = ?
          )        
         */
        System.out.println("EXAMPLE 6.1 (affected rows): "
                + ctx.executeDelete(pr)
        );
        
        /*
        delete from 
          [classicmodels].[dbo].[payment] 
        where 
          [classicmodels].[dbo].[payment].[invoice_amount] = ?        
        */
        System.out.println("EXAMPLE 6.2 (affected rows): "
                + ctx.executeDelete(pr, PAYMENT.INVOICE_AMOUNT.eq(BigDecimal.ZERO))
        );

        // user-defined POJO
        /*
        delete from 
          [classicmodels].[dbo].[sale] 
        where 
          [classicmodels].[dbo].[sale].[sale_id] = ?        
         */
        SalePart sp = new SalePart(14L, BigDecimal.valueOf(1607.76));
        System.out.println("EXAMPLE 6.3 (affected rows): "
                + ctx.executeDelete(ctx.newRecord(SALE, sp))
        );
    }

    // EXAMPLE 7   
    /*
    delete from 
      [classicmodels].[dbo].[payment] 
    where 
      (
        [classicmodels].[dbo].[payment].[customer_number] = ? 
        and [classicmodels].[dbo].[payment].[check_number] = ?
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
            ctx.configuration().set(new Settings()
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
    declare @result table ([sale] float);
    
    delete from 
      [classicmodels].[dbo].[sale] output [deleted].[sale] into @result 
    where 
      [classicmodels].[dbo].[sale].[sale_id] = ?;
    
    select 
      [r].[sale] 
    from 
      @result [r];
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
    declare @result table (
      [customer_number] bigint, 
      [check_number] varchar(50), 
      [payment_date] datetime2(3), 
      [invoice_amount] numeric(10, 2), 
      [caching_date] datetime2(3)
    );
    
    delete from 
      [classicmodels].[dbo].[payment] output [deleted].[customer_number], 
      [deleted].[check_number], 
      [deleted].[payment_date], 
      [deleted].[invoice_amount], 
      [deleted].[caching_date] into @result 
    where 
      [classicmodels].[dbo].[payment].[invoice_amount] > ?;
    
    select 
      [r].[customer_number], 
      [r].[check_number], 
      [r].[payment_date], 
      [r].[invoice_amount], 
      [r].[caching_date] 
    from 
      @result [r];   
     */
    public void deletePaymentReturning() {

        System.out.println("EXAMPLE 10 (deleted payment): \n"
                + ctx.delete(PAYMENT)
                        .where(PAYMENT.INVOICE_AMOUNT.gt(BigDecimal.valueOf(100000)))
                        .returning()
                        .fetch() // Result<PaymentRecord>
        );
    }

    // EXAMPLE 11
    /*
    declare @result table ([product_id] bigint);
    delete from 
      [classicmodels].[dbo].[orderdetail] output [deleted].[product_id] into @result 
    where 
      [classicmodels].[dbo].[orderdetail].[product_id] in (
        select 
          [classicmodels].[dbo].[product].[product_id] 
        from 
          [classicmodels].[dbo].[product] 
        where 
          [classicmodels].[dbo].[product].[product_line] = ?
      );
    select 
      [r].[product_id] 
    from 
      @result [r];
    
    declare @result table (
      [product_line] varchar(50)
    );
    delete from 
      [classicmodels].[dbo].[product] output [deleted].[product_line] into @result 
    where 
      [classicmodels].[dbo].[product].[product_id] in (?, ?, ?, ..., ?);
    select 
      [r].[product_line] 
    from 
      @result [r];
    
    delete from 
      [classicmodels].[dbo].[productline] 
    where 
      [classicmodels].[dbo].[productline].[product_line] = ?    
     */
    public void deleteCascadeReturningProductLineMotorcycles() {

        System.out.println("EXAMPLE 11 (affected rows): "
                + ctx.delete(PRODUCTLINE)
                        .where(PRODUCTLINE.PRODUCT_LINE.eq(
                                ctx.delete(PRODUCT)
                                        .where(PRODUCT.PRODUCT_ID.in(
                                                ctx.delete(ORDERDETAIL)
                                                        .where(ORDERDETAIL.PRODUCT_ID.in(
                                                                select(PRODUCT.PRODUCT_ID).from(PRODUCT)
                                                                        .where(PRODUCT.PRODUCT_LINE.eq("Motorcycles"))))
                                                        .returningResult(ORDERDETAIL.PRODUCT_ID).fetch()))
                                        .returningResult(PRODUCT.PRODUCT_LINE).fetch().get(0).value1()))
                        .execute()
        );
    }

    // EXAMPLE 12
    /*
    declare @result table ([order_id] bigint);
    delete from 
      [classicmodels].[dbo].[orderdetail] output [deleted].[order_id] into @result 
    where 
      [classicmodels].[dbo].[orderdetail].[order_id] in (
        select 
          [classicmodels].[dbo].[order].[order_id] 
        from 
          [classicmodels].[dbo].[order] 
        where 
          [classicmodels].[dbo].[order].[customer_number] = ?
      );
    select 
      [r].[order_id] 
    from 
      @result [r];
   
    declare @result table ([customer_number] bigint);
    delete from 
      [classicmodels].[dbo].[order] output [deleted].[customer_number] into @result 
    where 
      [classicmodels].[dbo].[order].[order_id] in (?, ?, ?, ..., ?);
    select 
      [r].[customer_number] 
    from 
      @result [r];
    
    declare @result table ([customer_number] bigint);
    delete from 
      [classicmodels].[dbo].[payment] output [deleted].[customer_number] into @result 
    where 
      [classicmodels].[dbo].[payment].[customer_number] = ?;
    select 
      [r].[customer_number] 
    from 
      @result [r];    
    
    declare @result table ([customer_number] bigint);
    delete from 
      [classicmodels].[dbo].[customerdetail] output [deleted].[customer_number] into @result 
    where 
      [classicmodels].[dbo].[customerdetail].[customer_number] = ?;
    select 
      [r].[customer_number] 
    from 
      @result [r];

    delete from 
      [classicmodels].[dbo].[customer] 
    where 
      [classicmodels].[dbo].[customer].[customer_number] = ?
     */
    public void deleteCascadeReturningCustomer112() {

        System.out.println("EXAMPLE 12 (affected rows): "
                + ctx.delete(CUSTOMER)
                        .where(CUSTOMER.CUSTOMER_NUMBER.eq(
                                ctx.delete(CUSTOMERDETAIL)
                                        .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(
                                                ctx.delete(PAYMENT)
                                                        .where(PAYMENT.CUSTOMER_NUMBER.eq(
                                                                ctx.delete(ORDER)
                                                                        .where(ORDER.ORDER_ID.in(
                                                                                ctx.delete(ORDERDETAIL)
                                                                                        .where(ORDERDETAIL.ORDER_ID.in(
                                                                                                select(ORDER.ORDER_ID).from(ORDER)
                                                                                                        .where(ORDER.CUSTOMER_NUMBER.eq(112L))))
                                                                                        .returningResult(ORDERDETAIL.ORDER_ID).fetch()))
                                                                        .returningResult(ORDER.CUSTOMER_NUMBER).fetch().get(0).value1()))
                                                        .returningResult(PAYMENT.CUSTOMER_NUMBER).fetch().get(0).value1()))
                                        .returningResult(CUSTOMERDETAIL.CUSTOMER_NUMBER).fetchOne().value1()))
                        .execute()
        );
    }
}