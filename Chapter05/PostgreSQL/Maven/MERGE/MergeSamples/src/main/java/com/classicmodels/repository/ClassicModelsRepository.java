package com.classicmodels.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jooq.generated.Keys;
import jooq.generated.enums.RateType;
import jooq.generated.enums.VatType;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.PaymentRecord;
import jooq.generated.tables.records.SaleRecord;
import org.jooq.DSLContext;
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
    insert into "public"."payment" (
      "customer_number", "check_number", 
      "payment_date", "invoice_amount", 
      "caching_date", "version", "modified"
    ) 
    values 
      (
        ?, 
        ?, 
        cast(
          ? as timestamp(6)
        ), 
        ?, 
        cast(
          ? as timestamp(6)
        ), 
        ?, 
        cast(
          ? as timestamp(6)
        )
      ) on conflict do nothing   
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
    insert into "public"."payment" (
      "customer_number", "check_number", 
      "payment_date", "invoice_amount", 
      "caching_date", "version", "modified"
    ) 
    values 
      (
        ?, 
        ?, 
        cast(
          ? as timestamp(6)
        ), 
        ?, 
        cast(
          ? as timestamp(6)
        ), 
        ?, 
        cast(
          ? as timestamp(6)
        )
      ) on conflict do nothing    
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
    // 3.1
    insert into "public"."payment" (
      "customer_number", "check_number", 
      "payment_date", "invoice_amount", 
      "caching_date", "version", "modified"
    ) 
    values 
      (
        ?, 
        ?, 
        cast(
          ? as timestamp(6)
        ), 
        ?, 
        cast(
          ? as timestamp(6)
        ), 
        ?, 
        cast(
          ? as timestamp(6)
        )
      ) on conflict (
        "customer_number", "check_number"
      ) do 
    update 
    set 
      "invoice_amount" = ?, 
      "caching_date" = cast(
        ? as timestamp(6)
      )
    
    // 3.2
    insert into "public"."payment" (
      "customer_number", "check_number", 
      "payment_date", "invoice_amount", 
      "caching_date", "version", "modified"
    ) 
    values 
      (
        ?, 
        ?, 
        cast(
          ? as timestamp(6)
        ), 
        ?, 
        cast(
          ? as timestamp(6)
        ), 
        ?, 
        cast(
          ? as timestamp(6)
        )
      ) on conflict on constraint "payment_pk" do 
    update 
    set 
      "invoice_amount" = ?, 
      "caching_date" = cast(
        ? as timestamp(6)
      )
    
    // 3.3
    insert into "public"."payment" (
      "customer_number", "check_number", 
      "payment_date", "invoice_amount", 
      "caching_date", "version", "modified"
    ) 
    values 
      (
        ?, 
        ?, 
        cast(
          ? as timestamp(6)
        ), 
        ?, 
        cast(
          ? as timestamp(6)
        ), 
        ?, 
        cast(
          ? as timestamp(6)
        )
      ) on conflict on constraint "check_number_uk" do 
    update 
    set 
      "invoice_amount" = ?, 
      "caching_date" = cast(
        ? as timestamp(6)
      )    
     */
    public void insertPaymentOnConflictUpdateIt() {

        System.out.println("EXAMPLE 3.1 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(129L, "ID449593",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21), 0, LocalDateTime.now())
                        .onConflict()
                        // .onConflict(PAYMENT.CUSTOMER_NUMBER, PAYMENT.CHECK_NUMBER)
                        .doUpdate()
                        .set(PAYMENT.INVOICE_AMOUNT, BigDecimal.ZERO)
                        .setNull(PAYMENT.CACHING_DATE)
                        .execute()
        );
        
        System.out.println("EXAMPLE 3.2 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(129L, "ID449593",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21), 0, LocalDateTime.now())
                        .onConflictOnConstraint(Keys.PAYMENT_PK)
                        .doUpdate()
                        .set(PAYMENT.INVOICE_AMOUNT, BigDecimal.ZERO)
                        .setNull(PAYMENT.CACHING_DATE)
                        .execute()
        );
        
        System.out.println("EXAMPLE 3.3 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(129L, "ID449593",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21), 0, LocalDateTime.now())
                        .onConflictOnConstraint(Keys.CHECK_NUMBER_UK)
                        .doUpdate() 
                        .set(PAYMENT.INVOICE_AMOUNT, BigDecimal.ZERO)
                        .setNull(PAYMENT.CACHING_DATE)
                        .execute()
        );
    }   

    // EXAMPLE 4
    /*
    insert into "public"."order" (
      "order_id", "order_date", "required_date", 
      "shipped_date", "status", "comments", 
      "customer_number", "amount"
    ) 
    values 
      (
        ?, 
        cast(? as date), 
        cast(? as date), 
        cast(? as date), 
        ?, 
        ?, 
        ?, 
        ?
      ) on conflict ("order_id") do 
    update 
    set 
      "order_date" = cast(? as date), 
      "required_date" = cast(? as date), 
      "shipped_date" = cast(? as date), 
      "status" = ?    
     */
    public void insertOrderOtherwiseUpdateIt() {

        System.out.println("EXAMPLE 4.1 (affected rows): "
                + ctx.insertInto(ORDER)
                        .values(10101L, LocalDate.of(2003, 2, 12), LocalDate.of(2003, 3, 1),
                                LocalDate.of(2003, 2, 27), "Shipped", "New order inserted ...", 363L, 414.44)
                        .onDuplicateKeyUpdate()
                        .set(ORDER.ORDER_DATE, LocalDate.of(2003, 2, 12))
                        .set(ORDER.REQUIRED_DATE, LocalDate.of(2003, 3, 1))
                        .set(ORDER.SHIPPED_DATE, LocalDate.of(2003, 2, 27))
                        .set(ORDER.STATUS, "Shipped")
                        .execute()
        );

        System.out.println("EXAMPLE 4.2 (affected rows): "
                + ctx.insertInto(ORDER)
                        .set(ORDER.ORDER_ID, 10101L)
                        .set(ORDER.ORDER_DATE, LocalDate.of(2003, 2, 12))
                        .set(ORDER.REQUIRED_DATE, LocalDate.of(2003, 3, 1))
                        .set(ORDER.SHIPPED_DATE, LocalDate.of(2003, 2, 27))
                        .set(ORDER.STATUS, "Shipped")
                        .set(ORDER.COMMENTS, "New order inserted ...")
                        .set(ORDER.CUSTOMER_NUMBER, 128L)
                        .set(ORDER.AMOUNT, BigDecimal.valueOf(414.44))
                        .onDuplicateKeyUpdate()
                        .set(ORDER.ORDER_DATE, LocalDate.of(2003, 2, 12))
                        .set(ORDER.REQUIRED_DATE, LocalDate.of(2003, 3, 1))
                        .set(ORDER.SHIPPED_DATE, LocalDate.of(2003, 2, 27))
                        .set(ORDER.STATUS, "Shipped")
                        .execute()
        );
    }

    // EXAMPLE 5
    /*
    insert into "public"."sale" (
      "sale_id", "fiscal_year", "sale", 
      "employee_number", "hot", "rate", 
      "vat", "fiscal_month", "revenue_growth", 
      "trend"
    ) 
    values 
      (
        ?, ?, ?, ?, ?, ? :: "public"."rate_type", 
        ? :: "public"."vat_type", ?, ?, ?
      ) on conflict ("sale_id") do 
    update 
    set 
      "sale_id" = ?, 
      "fiscal_year" = ?, 
      "sale" = ?, 
      "employee_number" = ?, 
      "hot" = ?, 
      "rate" = ? :: "public"."rate_type", 
      "vat" = ? :: "public"."vat_type", 
      "fiscal_month" = ?, 
      "revenue_growth" = ?, 
      "trend" = ?    
     */
    public void insertSaleRecordOnDuplicateKeyUpdateIt() {

        SaleRecord sr = new SaleRecord(1L, 2003, 3443.22, 1370L,
                null, RateType.SILVER, VatType.MAX, 3, 14.55, null);

        System.out.println("EXAMPLE 5 (affected rows): "
                + ctx.insertInto(SALE)
                        .set(sr) // or, values(sr)                
                        .onDuplicateKeyUpdate()
                        .set(sr)
                        .execute()
        );
    }

    // EXAMPLE 6
    /*
    insert into "public"."payment" (
      "customer_number", "check_number", 
      "payment_date", "invoice_amount", 
      "caching_date", "version", "modified"
    ) 
    values 
      (
        ?, 
        ?, 
        cast(
          ? as timestamp(6)
        ), 
        ?, 
        cast(
          ? as timestamp(6)
        ), 
        ?, 
        cast(
          ? as timestamp(6)
        )
      ) on conflict ("check_number") do 
    update 
    set 
      "invoice_amount" = ?, 
      "caching_date" = cast(
        ? as timestamp(6)
      )    
    */
    public void insertPaymentRecordOnConflictUpdateIt() {
        PaymentRecord pr = new PaymentRecord(129L, "ID449593",
                LocalDateTime.of(2005, 11, 9, 12, 10, 11), BigDecimal.valueOf(123.32),
                LocalDateTime.of(2005, 11, 11, 14, 25, 21), 0, LocalDateTime.now());
        
        System.out.println("EXAMPLE 6 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .set(pr) // or, values(pr)                
                        .onConflict(PAYMENT.CHECK_NUMBER)
                        // .onConflict(PAYMENT.CUSTOMER_NUMBER, PAYMENT.CHECK_NUMBER)
                        // .onConflictOnConstraint(Keys.PAYMENT_PK)
                        // .onConflictOnConstraint(Keys.CHECK_NUMBER_UK)
                        .doUpdate()
                        .set(PAYMENT.INVOICE_AMOUNT, BigDecimal.ZERO)
                        .setNull(PAYMENT.CACHING_DATE)
                        .execute()
        );
    }
}