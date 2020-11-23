package com.classicmodels.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Payment.PAYMENT;
import jooq.generated.tables.records.PaymentRecord;
import org.jooq.DSLContext;
import org.jooq.Query;
import static org.jooq.impl.DSL.name;
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
    
     */
    public void insertPaymentOnDuplicateKeyIgnore() {

        System.out.println("EXAMPLE 1 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(103L, "HQ336336",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21))
                        .onDuplicateKeyIgnore()
                        .execute()
        );
    }

    // EXAMPLE 2
    /*
    insert into "public"."payment" (
      "customer_number", "check_number", "payment_date", "invoice_amount", "caching_date"
    )
    values
    (
      ?, ?, cast(? as timestamp), ?, cast(? as timestamp)
    ) on conflict do nothing
     */
    public void insertPaymentOnConflictDoNothing() {

        System.out.println("EXAMPLE 2 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(103L, "HQ336336",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21))
                        .onConflictDoNothing()
                        .execute()
        );
    }

    // EXAMPLE 3
    /*
    insert into "public"."payment" (
      "customer_number", "check_number", "payment_date", "invoice_amount", "caching_date"
    )
    values
    ( 
      ?, ?, cast(? as timestamp), ?, cast(? as timestamp)
    ) on conflict ("check_number") do nothing
     */
    public void insertPaymentOnDuplicateCheckNumberDoNothing() {

        System.out.println("EXAMPLE 3 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(103L, "HQ336336",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21))
                        .onConflict(PAYMENT.CHECK_NUMBER)
                        .doNothing()
                        .execute()
        );
    }

    // EXAMPLE 4
    /*
    
     */
    public void insertPaymentOnConflictOnConstraintDoNothing() {

        System.out.println("EXAMPLE 4 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(103L, "HQ336336",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21))
                        .onConflictOnConstraint(name("unique_check_number")) // the auto UNIQUE constraint on CHECK_NUMBER
                        .doNothing() // with the name specified by us as [unique_check_number]
                        .execute()
        );
    }

    // EXAMPLE 5
    /*
    
     */
    public void insertPaymentOtherwiseUpdateIt() {

        System.out.println("EXAMPLE 5.1 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(103L, "HQ336336",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21))
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
                        .onDuplicateKeyUpdate()
                        .set(PAYMENT.INVOICE_AMOUNT, BigDecimal.valueOf(123.32))
                        .set(PAYMENT.CACHING_DATE, LocalDateTime.now())
                        .execute()
        );

        // check this: https://github.com/jOOQ/jOOQ/issues/1818
        Query q1 = ctx.query("SET IDENTITY_INSERT [order] ON");
        Query q2 = ctx.insertInto(ORDER)
                .values(10101L, LocalDate.of(2003, 2, 12), LocalDate.of(2003, 3, 1),
                        LocalDate.of(2003, 2, 27), "Shipped", "New order inserted ...", 363L)
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
    
     */
    public void insertPaymentOnConflictUpdateIt() {

        System.out.println("EXAMPLE 6 (affected rows): "
                + ctx.insertInto(PAYMENT)
                        .values(103L, "HQ336336",
                                LocalDateTime.of(2005, 11, 9, 12, 10, 11), 123.32,
                                LocalDateTime.of(2005, 11, 11, 14, 25, 21))
                        .onConflict(PAYMENT.CHECK_NUMBER)
                        .doUpdate()
                        .set(PAYMENT.INVOICE_AMOUNT, BigDecimal.ZERO)
                        .setNull(PAYMENT.CACHING_DATE)
                        .execute()
        );
    }

    // EXAMPLE 7
    /*
    
     */
    public void insertPaymentRecordOnDuplicateKeyUpdateIt() {

        PaymentRecord pr = new PaymentRecord(103L, "HQ336336",
                LocalDateTime.of(2005, 11, 9, 12, 10, 11), BigDecimal.valueOf(123.32),
                LocalDateTime.of(2005, 11, 11, 14, 25, 21));

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
    
     */
    public void insertPaymentRecordOnConflictUpdateIt() {
        PaymentRecord pr = new PaymentRecord(103L, "HQ336336",
                LocalDateTime.of(2005, 11, 9, 12, 10, 11), BigDecimal.valueOf(123.32),
                LocalDateTime.of(2005, 11, 11, 14, 25, 21));

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
    
    
}
