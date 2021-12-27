package com.classicmodels.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.impl.SQLDataType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    /* Casting and coercing examples */
    public void printPaymentAndCachingDateCast() {

        // no casting
        Result<Record2<BigDecimal, LocalDateTime>> result1
                = ctx.select(PAYMENT.INVOICE_AMOUNT.as("invoice_amount"),
                        PAYMENT.CACHING_DATE.as("caching_date"))
                        .from(PAYMENT)
                        .where(PAYMENT.CUSTOMER_NUMBER.eq(103L))
                        .fetch();

        result1.forEach(System.out::println);

        // casting
        Result<Record2<String, LocalDate>> result2
                = ctx.select(PAYMENT.INVOICE_AMOUNT.cast(String.class).as("invoice_amount"),
                        PAYMENT.CACHING_DATE.cast(LocalDate.class).as("caching_date"))
                        .from(PAYMENT)
                        .where(PAYMENT.CUSTOMER_NUMBER.eq(103L))
                        .fetch();

        result2.forEach(System.out::println);
    }

    // coercing
    public void printPaymentAndCachingDateCoerce() {

        Result<Record2<String, LocalDate>> result
                = ctx.select(PAYMENT.INVOICE_AMOUNT.coerce(String.class).as("invoice_amount"),
                        PAYMENT.CACHING_DATE.coerce(LocalDate.class).as("caching_date"))
                        .from(PAYMENT)
                        .where(PAYMENT.CUSTOMER_NUMBER.eq(103L))
                        .fetch();

        result.forEach(System.out::println);
    }

    // coercing - // this doesn't work as expected
    public void printProductPriceAndDescCoerce() {

        Result<Record2<BigDecimal, String>> result
                = ctx.select(PRODUCT.BUY_PRICE.coerce(SQLDataType.DECIMAL(10, 5)).as("buy_price"),
                        PRODUCT.PRODUCT_DESCRIPTION.coerce(SQLDataType.VARCHAR(10)).as("prod_desc"))
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_ID.eq(1L))
                        .fetch();

        result.forEach(System.out::println);
    }

    // casting - this work as expected
    public void printProductPriceAndDescCast() {

        Result<Record2<BigDecimal, String>> result
                = ctx.select(PRODUCT.BUY_PRICE.cast(SQLDataType.DECIMAL(10, 5)).as("buy_price"),
                        PRODUCT.PRODUCT_DESCRIPTION.cast(SQLDataType.VARCHAR(10)).as("prod_desc"))
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_ID.eq(1L))
                        .fetch();

        result.forEach(System.out::println);
    }

    // this doesn't work as expected
    public void printInvoicesPerDayCoerce(LocalDate day) {

        ctx.select(PAYMENT.INVOICE_AMOUNT)
                .from(PAYMENT)
                .where(PAYMENT.PAYMENT_DATE.coerce(LocalDate.class).eq(day))
                .fetch()
                .forEach(System.out::println);
    }

    // this work as expected
    public void printInvoicesPerDayCast(LocalDate day) {

        ctx.select(PAYMENT.INVOICE_AMOUNT)
                .from(PAYMENT)
                .where(PAYMENT.PAYMENT_DATE.cast(LocalDate.class).eq(day))
                .fetch()
                .forEach(System.out::println);
    }

    // set a collation
    public void printProductsName() {

        ctx.select(PRODUCT.PRODUCT_NAME)
                .from(PRODUCT)
                .orderBy(PRODUCT.PRODUCT_NAME.collate("BINARY_AI"))
                .fetch()
                .forEach(System.out::println);
    }

}
