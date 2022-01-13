package com.classicmodels.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;
import jooq.generated.Keys;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.PaymentRecord;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.Table;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.val;
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
    merge into [classicmodels].[dbo].[payment] using (
      select 
        ?, ?, ?, ?, ?, ?, ?
    ) [t] (
      [customer_number], [check_number], 
      [payment_date], [invoice_amount], 
      [caching_date], [version], [modified]
    ) on (
      (
        [classicmodels].[dbo].[payment].[customer_number] = [t].[customer_number] 
        and [classicmodels].[dbo].[payment].[check_number] = [t].[check_number]
      ) 
      or [classicmodels].[dbo].[payment].[check_number] = [t].[check_number]
    ) when not matched then insert (
      [customer_number], [check_number], 
      [payment_date], [invoice_amount], 
      [caching_date], [version], [modified]
    ) 
    values 
      (
        [t].[customer_number], [t].[check_number], 
        [t].[payment_date], [t].[invoice_amount], 
        [t].[caching_date], [t].[version], 
        [t].[modified]
      );    
     */
    public void insertPaymentOnDuplicateKeyIgnore() {

        System.out.println("EXAMPLE 1 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(129L, "ID449593",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21), 0,
                                LocalDateTime.now())
                        .onDuplicateKeyIgnore()
                        .execute()
        );
    }

    // EXAMPLE 2
    /*
    merge into [classicmodels].[dbo].[payment] using (
          select 
            ?, ?, ?, ?, ?, ?, ?
        ) [t] (
          [customer_number], [check_number], 
          [payment_date], [invoice_amount], 
          [caching_date], [version], [modified]
        ) on (
          (
            [classicmodels].[dbo].[payment].[customer_number] = [t].[customer_number] 
            and [classicmodels].[dbo].[payment].[check_number] = [t].[check_number]
          ) 
          or [classicmodels].[dbo].[payment].[check_number] = [t].[check_number]
        ) when not matched then insert (
          [customer_number], [check_number], 
          [payment_date], [invoice_amount], 
          [caching_date], [version], [modified]
        ) 
        values 
          (
            [t].[customer_number], [t].[check_number], 
            [t].[payment_date], [t].[invoice_amount], 
            [t].[caching_date], [t].[version], 
            [t].[modified]
          );        
     */
    public void insertPaymentOnConflictDoNothing() {

        System.out.println("EXAMPLE 2 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(129L, "ID449593",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21), 0,
                                LocalDateTime.now())
                        .onConflictDoNothing()
                        .execute()
        );
    }

    // EXAMPLE 3
    /*
    merge into [classicmodels].[dbo].[payment] using (
      select 
        ?, ?, ?, ?, ?, ?, ?
    ) [t] (
      [customer_number], [check_number], 
      [payment_date], [invoice_amount], 
      [caching_date], [version], [modified]
    ) on [classicmodels].[dbo].[payment].[check_number] = [t].[check_number] when not matched then insert (
      [customer_number], [check_number], 
      [payment_date], [invoice_amount], 
      [caching_date], [version], [modified]
    ) 
    values 
      (
        [t].[customer_number], [t].[check_number], 
        [t].[payment_date], [t].[invoice_amount], 
        [t].[caching_date], [t].[version], 
        [t].[modified]
      );    
     */
    public void insertPaymentOnDuplicateCheckNumberDoNothing() {

        System.out.println("EXAMPLE 3 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(100L, "HQ336336",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21), 0,
                                LocalDateTime.now())
                        .onConflict(PAYMENT.CHECK_NUMBER)
                        .doNothing()
                        .execute()
        );
    }

    // EXAMPLE 4
    /*
    merge into [classicmodels].[dbo].[payment] using (
      select 
        ?, ?, ?, ?, ?, ?, ?
    ) [t] (
      [customer_number], [check_number], 
      [payment_date], [invoice_amount], 
      [caching_date], [version], [modified]
    ) on [classicmodels].[dbo].[payment].[check_number] = [t].[check_number] when not matched then insert (
      [customer_number], [check_number], 
      [payment_date], [invoice_amount], 
      [caching_date], [version], [modified]
    ) 
    values 
      (
        [t].[customer_number], [t].[check_number], 
        [t].[payment_date], [t].[invoice_amount], 
        [t].[caching_date], [t].[version], 
        [t].[modified]
      );    
     */
    public void insertPaymentOnConflictOnConstraintDoNothing() {

        System.out.println("EXAMPLE 4 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(103L, "HQ336336",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21), 0,
                                LocalDateTime.now())
                        .onConflictOnConstraint(Keys.PAYMENT__CHECK_NUMBER_UK)
                        .doNothing()
                        .execute()
        );
    }

    // EXAMPLE 5
    /*
    // 5.1, 5.2
    merge into [classicmodels].[dbo].[payment] using (
      select 
        ?, ?, ?, ?, ?, ?, ?
    ) [t] (
      [customer_number], [check_number], 
      [payment_date], [invoice_amount], 
      [caching_date], [version], [modified]
    ) on (
      (
        [classicmodels].[dbo].[payment].[customer_number] = [t].[customer_number] 
        and [classicmodels].[dbo].[payment].[check_number] = [t].[check_number]
      ) 
      or [classicmodels].[dbo].[payment].[check_number] = [t].[check_number]
    ) when matched then 
    update 
    set 
      [classicmodels].[dbo].[payment].[invoice_amount] = ?, 
      [classicmodels].[dbo].[payment].[caching_date] = ? when not matched then insert (
        [customer_number], [check_number], 
        [payment_date], [invoice_amount], 
        [caching_date], [version], [modified]
      ) 
    values 
      (
        [t].[customer_number], [t].[check_number], 
        [t].[payment_date], [t].[invoice_amount], 
        [t].[caching_date], [t].[version], 
        [t].[modified]
      );
    
     // 5.3
     SET IDENTITY_INSERT [order] ON

    merge into [classicmodels].[dbo].[order] using (
      select 
        10101, 
        cast('2003-02-12' as date), 
        cast('2003-03-01' as date), 
        cast('2003-02-27' as date), 
        'Shipped', 
        'New order inserted ...', 
        363, 
        414.44
    ) [t] (
      [order_id], [order_date], [required_date], 
      [shipped_date], [status], [comments], 
      [customer_number], [amount]
    ) on [classicmodels].[dbo].[order].[order_id] = [t].[order_id] when matched then 
    update 
    set 
      [classicmodels].[dbo].[order].[order_date] = cast('2003-02-12' as date), 
      [classicmodels].[dbo].[order].[required_date] = cast('2003-03-01' as date), 
      [classicmodels].[dbo].[order].[shipped_date] = cast('2003-02-27' as date), 
      [classicmodels].[dbo].[order].[status] = 'Shipped' when not matched then insert (
        [order_id], [order_date], [required_date], 
        [shipped_date], [status], [comments], 
        [customer_number], [amount]
      ) 
    values 
      (
        [t].[order_id], [t].[order_date], 
        [t].[required_date], [t].[shipped_date], 
        [t].[status], [t].[comments], [t].[customer_number], 
        [t].[amount]
      );
    
      SET IDENTITY_INSERT [order] OFF
     */
    public void insertPaymentOnDuplicateKeyUpdateIt() {

        System.out.println("EXAMPLE 5.1 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(103L, "HQ336336",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21), 0,
                                LocalDateTime.now())
                        .onDuplicateKeyUpdate()
                        .set(PAYMENT.INVOICE_AMOUNT, BigDecimal.valueOf(123.32))
                        .set(PAYMENT.CACHING_DATE, LocalDateTime.now())
                        .execute()
        );

        System.out.println("EXAMPLE 5.2 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .set(PAYMENT.CUSTOMER_NUMBER, 103L)
                        .set(PAYMENT.CHECK_NUMBER, "HQ336336")
                        .set(PAYMENT.PAYMENT_DATE, LocalDateTime.of(2005, 11, 9, 12, 10, 11))
                        .set(PAYMENT.INVOICE_AMOUNT, BigDecimal.valueOf(123.32))
                        .set(PAYMENT.CACHING_DATE, LocalDateTime.of(2005, 11, 11, 14, 25, 21))
                        .set(PAYMENT.VERSION, 0)
                        .set(PAYMENT.MODIFIED, LocalDateTime.now())
                        .onDuplicateKeyUpdate()
                        .set(PAYMENT.INVOICE_AMOUNT, BigDecimal.valueOf(123.32))
                        .set(PAYMENT.CACHING_DATE, LocalDateTime.now())
                        .execute()
        );

        // check this: https://github.com/jOOQ/jOOQ/issues/1818
        Query q1 = ctx.query("SET IDENTITY_INSERT [order] ON");
        Query q2 = ctx.insertInto(ORDER)
                .values(10101L, LocalDate.of(2003, 2, 12), LocalDate.of(2003, 3, 1),
                        LocalDate.of(2003, 2, 27), "Shipped", "New order inserted ...", 363L, 414.44)
                .onDuplicateKeyUpdate()
                .set(ORDER.ORDER_DATE, LocalDate.of(2003, 2, 12))
                .set(ORDER.REQUIRED_DATE, LocalDate.of(2003, 3, 1))
                .set(ORDER.SHIPPED_DATE, LocalDate.of(2003, 2, 27))
                .set(ORDER.STATUS, "Shipped");
        Query q3 = ctx.query("SET IDENTITY_INSERT [order] OFF");

        System.out.println("EXAMPLE 5.3 (affected rows): "
                + Arrays.toString(
                        ctx.batch(q1, q2, q3).execute()
                )
        );
    }

    // EXAMPLE 6
    /*
    merge into [classicmodels].[dbo].[payment] using (
      select 
        ?, ?, ?, ?, ?, ?, ?
    ) [t] (
      [customer_number], [check_number], 
      [payment_date], [invoice_amount], 
      [caching_date], [version], [modified]
    ) on [classicmodels].[dbo].[payment].[check_number] = [t].[check_number] when matched then 
    update 
    set 
      [classicmodels].[dbo].[payment].[invoice_amount] = ?, 
      [classicmodels].[dbo].[payment].[caching_date] = ? when not matched then insert (
        [customer_number], [check_number], 
        [payment_date], [invoice_amount], 
        [caching_date], [version], [modified]
      ) 
    values 
      (
        [t].[customer_number], [t].[check_number], 
        [t].[payment_date], [t].[invoice_amount], 
        [t].[caching_date], [t].[version], 
        [t].[modified]
      );    
     */
    public void insertPaymentOnConflictUpdateIt() {

        System.out.println("EXAMPLE 6 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(103L, "HQ336336",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21), 0,
                                LocalDateTime.now())
                        .onConflict(PAYMENT.CHECK_NUMBER)
                        .doUpdate()
                        .set(PAYMENT.INVOICE_AMOUNT, BigDecimal.ZERO)
                        .setNull(PAYMENT.CACHING_DATE)
                        .execute()
        );
    }

    // EXAMPLE 7
    /*
    merge into [classicmodels].[dbo].[payment] using (
      select 
        ?, ?, ?, ?, ?, ?, ?
    ) [t] (
      [customer_number], [check_number], 
      [payment_date], [invoice_amount], 
      [caching_date], [version], [modified]
    ) on (
      (
        [classicmodels].[dbo].[payment].[customer_number] = [t].[customer_number] 
        and [classicmodels].[dbo].[payment].[check_number] = [t].[check_number]
      ) 
      or [classicmodels].[dbo].[payment].[check_number] = [t].[check_number]
    ) when matched then 
    update 
    set 
      [classicmodels].[dbo].[payment].[customer_number] = ?, 
      [classicmodels].[dbo].[payment].[check_number] = ?, 
      [classicmodels].[dbo].[payment].[payment_date] = ?, 
      [classicmodels].[dbo].[payment].[invoice_amount] = ?, 
      [classicmodels].[dbo].[payment].[caching_date] = ?, 
      [classicmodels].[dbo].[payment].[version] = ?, 
      [classicmodels].[dbo].[payment].[modified] = ? when not matched then insert (
        [customer_number], [check_number], 
        [payment_date], [invoice_amount], 
        [caching_date], [version], [modified]
      ) 
    values 
      (
        [t].[customer_number], [t].[check_number], 
        [t].[payment_date], [t].[invoice_amount], 
        [t].[caching_date], [t].[version], 
        [t].[modified]
      );    
     */
    public void insertPaymentRecordOnDuplicateKeyUpdateIt() {

        PaymentRecord pr = new PaymentRecord(129L, "ID449593",
                LocalDateTime.of(2005, 11, 9, 12, 10, 11), BigDecimal.valueOf(123.32),
                LocalDateTime.of(2005, 11, 11, 14, 25, 21), 0, LocalDateTime.now());

        System.out.println("EXAMPLE 7 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .set(pr) // or, values(pr)                
                        .onDuplicateKeyUpdate()
                        .set(pr)
                        .execute()
        );
    }

    // EXAMPLE 8
    /*
    merge into [classicmodels].[dbo].[payment] using (
      select 
        ?, ?, ?, ?, ?, ?, ?
    ) [t] (
      [customer_number], [check_number], 
      [payment_date], [invoice_amount], 
      [caching_date], [version], [modified]
    ) on [classicmodels].[dbo].[payment].[check_number] = [t].[check_number] when matched then 
    update 
    set 
      [classicmodels].[dbo].[payment].[invoice_amount] = ?, 
      [classicmodels].[dbo].[payment].[caching_date] = ? when not matched then insert (
        [customer_number], [check_number], 
        [payment_date], [invoice_amount], 
        [caching_date], [version], [modified]
      ) 
    values 
      (
        [t].[customer_number], [t].[check_number], 
        [t].[payment_date], [t].[invoice_amount], 
        [t].[caching_date], [t].[version], 
        [t].[modified]
      );    
     */
    public void insertPaymentRecordOnConflictUpdateIt() {
        PaymentRecord pr = new PaymentRecord(129L, "ID449593",
                LocalDateTime.of(2005, 11, 9, 12, 10, 11), BigDecimal.valueOf(123.32),
                LocalDateTime.of(2005, 11, 11, 14, 25, 21), 0, LocalDateTime.now());

        System.out.println("EXAMPLE 8 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .set(pr) // or, values(pr)                
                        .onConflict(PAYMENT.CHECK_NUMBER)
                        .doUpdate()
                        .set(PAYMENT.INVOICE_AMOUNT, BigDecimal.ZERO)
                        .setNull(PAYMENT.CACHING_DATE)
                        .execute()
        );
    }

    /***********************/
    /* MERGE INTO EXAMPLES */
    /***********************/
    
    // EXAMPLE 9
    /*    
    merge into [classicmodels].[dbo].[product] using (
      select 
        1 [one]
    ) t on [classicmodels].[dbo].[product].[product_name] = ? when matched then 
    update 
    set 
      [classicmodels].[dbo].[product].[product_name] = ? when not matched then insert ([product_name], [code]) 
    values 
      (?, ?);    
     */
    public void updateProductNameElseInsertProduct() {

        System.out.println("EXAMPLE 9 (affected rows): "
                + ctx.mergeInto(PRODUCT)
                        .usingDual() // or, (ctx.selectOne())
                        .on(PRODUCT.PRODUCT_NAME.eq("1952 Alpine Renault 1300"))
                        .whenMatchedThenUpdate()
                        .set(PRODUCT.PRODUCT_NAME, "1952 Alpine Renault 1600")
                        .whenNotMatchedThenInsert(PRODUCT.PRODUCT_NAME, PRODUCT.CODE)
                        .values("1952 Alpine Renault 1600", 599302L)
                        .execute()
        );
    }

    // EXAMPLE 10
    /*
    merge into [classicmodels].[dbo].[customer] using (
      select 
        1 [one]
    ) t on (
      [classicmodels].[dbo].[customer].[contact_first_name] = ? 
      and [classicmodels].[dbo].[customer].[contact_last_name] = ?
    ) when matched then 
    update 
    set 
      [classicmodels].[dbo].[customer].[contact_first_name] = ? when not matched then insert (
        [customer_name], [contact_first_name], 
        [contact_last_name], [phone], [sales_rep_employee_number], 
        [credit_limit]
      ) 
    values 
      (?, ?, ?, ?, ?, ?);   
     */
    public void updateCustomerFirstNameElseInsertCustomer() {

        System.out.println("EXAMPLE 10 (affected rows): "
                + ctx.mergeInto(CUSTOMER)
                        .usingDual() //or, using(ctx.selectOne())
                        .on(CUSTOMER.CONTACT_FIRST_NAME.eq("Alejandra")
                                .and(CUSTOMER.CONTACT_LAST_NAME.eq("Camino")))
                        .whenMatchedThenUpdate()
                        .set(CUSTOMER.CONTACT_FIRST_NAME, "Alexandra")
                        .whenNotMatchedThenInsert(CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_FIRST_NAME,
                                CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE, CUSTOMER.SALES_REP_EMPLOYEE_NUMBER,
                                CUSTOMER.CREDIT_LIMIT)
                        .values(UUID.randomUUID().toString(), // random customer name
                                "Camino", "Alexandra", "(91) 745 6555", 1401L, BigDecimal.ZERO)
                        .execute()
        );
    }

    // EXAMPLE 11
    /*
    merge into [classicmodels].[dbo].[sale] using [classicmodels].[dbo].[employee] on [classicmodels].[dbo].[employee].[employee_number] = [classicmodels].[dbo].[sale].[employee_number] when matched then 
    update 
    set 
      [classicmodels].[dbo].[sale].[sale] = coalesce(
        (
          [classicmodels].[dbo].[sale].[sale] - [classicmodels].[dbo].[employee].[commission]
        ), 
        [classicmodels].[dbo].[sale].[sale]
      ) when not matched then insert (
        [employee_number], [fiscal_year], 
        [sale], [fiscal_month], [revenue_growth]
      ) 
    values 
      (
        [classicmodels].[dbo].[employee].[employee_number], 
        ?, 
        coalesce(
          (
            ? * [classicmodels].[dbo].[employee].[commission]
          ), 
          ?
        ), 
        ?, 
        ?
      );   
     */
    public void updateSaleElseInsertSale() {

        System.out.println("EXAMPLE 11 (affected rows): "
                + ctx.mergeInto(SALE)
                        .using(EMPLOYEE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                        .whenMatchedThenUpdate()
                        .set(SALE.SALE_, coalesce(SALE.SALE_.minus(EMPLOYEE.COMMISSION), SALE.SALE_))
                        .whenNotMatchedThenInsert(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_,
                                SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(EMPLOYEE.EMPLOYEE_NUMBER, val(2015),
                                coalesce(val(-1.0).mul(EMPLOYEE.COMMISSION), val(0.0)), val(1), val(0.0))
                        .execute()
        );
    }

    // EXAMPLE 12
    /*
    merge into [classicmodels].[dbo].[sale] using [classicmodels].[dbo].[employee] on [classicmodels].[dbo].[employee].[employee_number] = [classicmodels].[dbo].[sale].[employee_number] when matched then delete when not matched then insert (
      [employee_number], [fiscal_year], 
      [sale], [fiscal_month], [revenue_growth]
    ) 
    values 
      (
        [classicmodels].[dbo].[employee].[employee_number], 
        ?, 
        coalesce(
          (
            ? * [classicmodels].[dbo].[employee].[commission]
          ), 
          ?
        ), 
        ?, 
        ?
      );    
     */
    public void deleteSaleElseInsertSale() {

        System.out.println("EXAMPLE 12 (affected rows): "
                + ctx.mergeInto(SALE)
                        .using(EMPLOYEE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                        .whenMatchedThenDelete()
                        .whenNotMatchedThenInsert(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_,
                                SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(EMPLOYEE.EMPLOYEE_NUMBER, val(2015),
                                coalesce(val(-1.0).mul(EMPLOYEE.COMMISSION), val(0.0)), val(1), val(0.0))
                        .execute()
        );
    }

    // EXAMPLE 13
    /*
    merge into [classicmodels].[dbo].[sale] using [classicmodels].[dbo].[employee] on (
      [classicmodels].[dbo].[employee].[employee_number] = [classicmodels].[dbo].[sale].[employee_number] 
      and [classicmodels].[dbo].[sale].[sale] < ?
    ) when matched then delete when not matched then insert (
      [employee_number], [fiscal_year], 
      [sale], [fiscal_month], [revenue_growth]
    ) 
    values 
      (
        [classicmodels].[dbo].[employee].[employee_number], 
        ?, 
        coalesce(
          (
            ? * [classicmodels].[dbo].[employee].[commission]
          ), 
          ?
        ), 
        ?, 
        ?
      );    
     */
    public void deleteNegativeSaleElseInsertSaleAsDoubleCommission() {

        System.out.println("EXAMPLE 13 (affected rows): "
                + ctx.mergeInto(SALE)
                        .using(EMPLOYEE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER)
                                .and(SALE.SALE_.lt(0.0)))
                        .whenMatchedThenDelete()
                        .whenNotMatchedThenInsert(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_,
                                SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(EMPLOYEE.EMPLOYEE_NUMBER, val(2015),
                                coalesce(val(2.0).mul(EMPLOYEE.COMMISSION), val(0.0)), val(1), val(0.0))
                        .execute()
        );
    }

    // EXAMPLE 14
    /*
    merge into [classicmodels].[dbo].[sale] using (
      select 
        1 [one]
    ) t on [classicmodels].[dbo].[sale].[fiscal_year] = ? when matched 
    and [classicmodels].[dbo].[sale].[sale] > ? then 
    update 
    set 
      [classicmodels].[dbo].[sale].[sale] = (
        [classicmodels].[dbo].[sale].[sale] * ?
      ) when matched 
      and (
        not (
          [classicmodels].[dbo].[sale].[sale] > ?
        ) 
        and [classicmodels].[dbo].[sale].[sale] < ?
      ) then delete;    
     */
    public void updateSaleThenDeleteViaWhenMatchedAnd() {

        System.out.println("EXAMPLE 14 (affected rows): "
                + ctx.mergeInto(SALE)
                        .usingDual()
                        .on(SALE.FISCAL_YEAR.eq(2003))
                        .whenMatchedAnd(SALE.SALE_.gt(2000.0))
                        .thenUpdate()
                        .set(SALE.SALE_, SALE.SALE_.mul(0.50))
                        .whenMatchedAnd(SALE.SALE_.lt(2000.0))
                        .thenDelete()
                        .execute()
        );
    }

    // EXAMPLE 15
    /*
    merge into [classicmodels].[dbo].[sale] using [classicmodels].[dbo].[employee] on [classicmodels].[dbo].[employee].[employee_number] = [classicmodels].[dbo].[sale].[employee_number] when matched 
    and [classicmodels].[dbo].[sale].[sale] < ? then delete when matched 
    and (
      not (
        [classicmodels].[dbo].[sale].[sale] < ?
      ) 
      and [classicmodels].[dbo].[sale].[sale] between ? 
      and ?
    ) then 
    update 
    set 
      [classicmodels].[dbo].[sale].[sale] = coalesce(
        (
          [classicmodels].[dbo].[sale].[sale] - [classicmodels].[dbo].[employee].[commission]
        ), 
        [classicmodels].[dbo].[sale].[sale]
      ) when not matched then insert (
        [employee_number], [fiscal_year], 
        [sale], [fiscal_month], [revenue_growth]
      ) 
    values 
      (
        [classicmodels].[dbo].[employee].[employee_number], 
        ?, 
        coalesce(
          [classicmodels].[dbo].[employee].[commission], 
          ?
        ), 
        ?, 
        ?
      );    
     */
    public void deleteSaleElseUpdateElseInsert() {

        System.out.println("EXAMPLE 15 (affected rows): "
                + ctx.mergeInto(SALE)
                        .using(EMPLOYEE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                        .whenMatchedAnd(SALE.SALE_.lt(0.0))
                        .thenDelete()
                        .whenMatchedAnd(SALE.SALE_.between(0.0, 1000.0))
                        .thenUpdate()
                        .set(SALE.SALE_, coalesce(SALE.SALE_.minus(EMPLOYEE.COMMISSION), SALE.SALE_))
                        .whenNotMatchedThenInsert(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_,
                                SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(EMPLOYEE.EMPLOYEE_NUMBER, val(2015),
                                coalesce(EMPLOYEE.COMMISSION.coerce(Double.class), val(0.0)), val(1), val(0.0))
                        .execute()
        );
    }

    // EXAMPLE 16
    /*
    merge into [classicmodels].[dbo].[sale] using (
      select 
        [classicmodels].[dbo].[employee].[employee_number], 
        [classicmodels].[dbo].[employee].[last_name], 
        [classicmodels].[dbo].[employee].[first_name], 
        [classicmodels].[dbo].[employee].[extension], 
        [classicmodels].[dbo].[employee].[email], 
        [classicmodels].[dbo].[employee].[office_code], 
        [classicmodels].[dbo].[employee].[salary], 
        [classicmodels].[dbo].[employee].[commission], 
        [classicmodels].[dbo].[employee].[reports_to], 
        [classicmodels].[dbo].[employee].[job_title], 
        [classicmodels].[dbo].[employee].[employee_of_year], 
        [classicmodels].[dbo].[employee].[monthly_bonus] 
      from 
        [classicmodels].[dbo].[employee] 
      where 
        [classicmodels].[dbo].[employee].[salary] < ?
    ) [t] on [classicmodels].[dbo].[sale].[employee_number] = [t].[employee_number] when matched then delete when not matched then insert (
      [employee_number], [fiscal_year], 
      [sale], [fiscal_month], [revenue_growth]
    ) 
    values 
      (
        [t].[employee_number], 
        ?, 
        coalesce([t].[commission], ?), 
        ?, 
        ?
      );    
     */
    public void deleteSalesOfEmpoyeeSalaryLt65000ElseInsert() {

        Table<?> table = selectFrom(EMPLOYEE)
                .where(EMPLOYEE.SALARY.lt(65000)).asTable("t");

        System.out.println("EXAMPLE 16 (affected rows): "
                + ctx.mergeInto(SALE)
                        .using(table)
                        .on(SALE.EMPLOYEE_NUMBER.eq(table.field(EMPLOYEE.EMPLOYEE_NUMBER)))
                        .whenMatchedThenDelete()
                        .whenNotMatchedThenInsert(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_,
                                SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(table.field(EMPLOYEE.EMPLOYEE_NUMBER), val(2015),
                                coalesce(table.field(EMPLOYEE.COMMISSION).coerce(Double.class), val(0.0)), 
                                val(1), val(0.0))
                        .execute()
        );
    }

    // EXAMPLE 17
    /*
    merge into [classicmodels].[dbo].[sale] using (
      select 
        1 [one]
    ) t on [classicmodels].[dbo].[sale].[fiscal_year] = ? when matched 
    and [classicmodels].[dbo].[sale].[sale] < ? then delete when matched 
    and not (
      [classicmodels].[dbo].[sale].[sale] < ?
    ) then 
    update 
    set 
      [classicmodels].[dbo].[sale].[sale] = (
        [classicmodels].[dbo].[sale].[sale] * ?
      );    
     */
    public void updateSaleThenDeleteViaDeleteWhere() {

        System.out.println("EXAMPLE 17 (affected rows): "
                + ctx.mergeInto(SALE)
                        .usingDual()
                        .on(SALE.FISCAL_YEAR.eq(2003))
                        .whenMatchedThenUpdate()
                        .set(SALE.SALE_, SALE.SALE_.mul(0.50))
                        .deleteWhere(SALE.SALE_.lt(2000.0))
                        .execute()
        );
    }

    // EXAMPLE 18
    /*
    merge into [classicmodels].[dbo].[sale] using [classicmodels].[dbo].[employee] on [classicmodels].[dbo].[sale].[employee_number] = [classicmodels].[dbo].[employee].[employee_number] when matched 
    and [classicmodels].[dbo].[sale].[sale] < ? then delete when matched 
    and not (
      [classicmodels].[dbo].[sale].[sale] < ?
    ) then 
    update 
    set 
      [classicmodels].[dbo].[sale].[sale] = coalesce(
        (
          [classicmodels].[dbo].[sale].[sale] - [classicmodels].[dbo].[employee].[commission]
        ), 
        [classicmodels].[dbo].[sale].[sale]
      );    
     */
    public void updateSaleBeforeDelete() {

        System.out.println("EXAMPLE 18 (affected rows): "
                + ctx.mergeInto(SALE)
                        .using(EMPLOYEE)
                        .on(SALE.EMPLOYEE_NUMBER.eq(EMPLOYEE.EMPLOYEE_NUMBER))
                        .whenMatchedThenUpdate()
                        .set(SALE.SALE_, coalesce(SALE.SALE_.minus(EMPLOYEE.COMMISSION), SALE.SALE_))
                        .deleteWhere(SALE.SALE_.lt(1000.0))
                        .execute()
        );
    }

    // EXAMPLE 19
    /*
    merge into [classicmodels].[dbo].[sale] using [classicmodels].[dbo].[employee] on [classicmodels].[dbo].[sale].[employee_number] = [classicmodels].[dbo].[employee].[employee_number] when matched 
    and [classicmodels].[dbo].[employee].[commission] < ? then delete when matched 
    and not (
      [classicmodels].[dbo].[employee].[commission] < ?
    ) then 
    update 
    set 
      [classicmodels].[dbo].[sale].[sale] = coalesce(
        (
          [classicmodels].[dbo].[sale].[sale] - [classicmodels].[dbo].[employee].[commission]
        ), 
        [classicmodels].[dbo].[sale].[sale]
      );    
     */
    public void deleteSaleBeforeUpdate() {

        System.out.println("EXAMPLE 19 (affected rows): "
                + ctx.mergeInto(SALE)
                        .using(EMPLOYEE)
                        .on(SALE.EMPLOYEE_NUMBER.eq(EMPLOYEE.EMPLOYEE_NUMBER))
                        .whenMatchedThenUpdate()
                        .set(SALE.SALE_, coalesce(SALE.SALE_.minus(EMPLOYEE.COMMISSION), SALE.SALE_))
                        .deleteWhere(EMPLOYEE.COMMISSION.lt(1000))
                        .execute()
        );
    }
}
