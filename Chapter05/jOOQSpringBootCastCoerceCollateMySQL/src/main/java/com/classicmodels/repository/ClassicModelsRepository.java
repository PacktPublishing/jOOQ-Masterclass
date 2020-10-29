package com.classicmodels.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.coerce;
import org.jooq.impl.SQLDataType;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    /* Casting examples */
    public void printOfficeByCode1(int officeCode) {

        ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .where(OFFICE.OFFICE_CODE.cast(SQLDataType.INTEGER).eq(officeCode))
                .fetch()
                .forEach(System.out::println);

        ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .where(OFFICE.OFFICE_CODE.cast(Integer.class).eq(officeCode))
                .fetch()
                .forEach(System.out::println);

        ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .where(OFFICE.OFFICE_CODE.eq(cast(officeCode, SQLDataType.VARCHAR(10))))
                .fetch()
                .forEach(System.out::println);

        ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .where(OFFICE.OFFICE_CODE.eq(cast(officeCode, String.class)))
                .fetch()
                .forEach(System.out::println);
    }

    public void printPaymentDatesByAmount1(float amount) {

        ctx.select(PAYMENT.PAYMENT_DATE, PAYMENT.CACHING_DATE)
                .from(PAYMENT)
                .where(PAYMENT.INVOICE_AMOUNT.eq(cast(amount, PAYMENT.INVOICE_AMOUNT)))
                .fetch()
                .forEach(System.out::println);
    }

    public void printOrderStatusByOrderDate1(LocalDateTime ldt) {

        ctx.select(ORDER.STATUS)
                .from(ORDER)
                .where(ORDER.ORDER_DATE.gt(cast(ldt, LocalDate.class)))
                .fetch()
                .forEach(System.out::println);

        ctx.select(ORDER.STATUS)
                .from(ORDER)
                .where(ORDER.ORDER_DATE.cast(SQLDataType.LOCALDATETIME).gt(ldt))
                .fetch()
                .forEach(System.out::println);

        ctx.select(ORDER.STATUS)
                .from(ORDER)
                .where(ORDER.ORDER_DATE.cast(LocalDateTime.class).gt(ldt))
                .fetch()
                .forEach(System.out::println);
    }

    public void printProductNameAndPrice1() {
        ctx.select(PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE.cast(Integer.class))
                .from(PRODUCT)
                .where(PRODUCT.PRODUCT_ID.eq(1L))
                .fetch()
                .forEach(System.out::println);
    }

    /* Coercions examples */
    public void printOfficeByCode2(int officeCode) {

        ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .where(OFFICE.OFFICE_CODE.coerce(SQLDataType.INTEGER).eq(officeCode))
                .fetch()
                .forEach(System.out::println);

        ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .where(OFFICE.OFFICE_CODE.coerce(Integer.class).eq(officeCode))
                .fetch()
                .forEach(System.out::println);

        ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .where(OFFICE.OFFICE_CODE.eq(coerce(officeCode, SQLDataType.VARCHAR(10))))
                .fetch()
                .forEach(System.out::println);

        ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .where(OFFICE.OFFICE_CODE.eq(coerce(officeCode, String.class)))
                .fetch()
                .forEach(System.out::println);
    }

    public void printPaymentDatesByAmount2(float amount) {

        ctx.select(PAYMENT.PAYMENT_DATE, PAYMENT.CACHING_DATE)
                .from(PAYMENT)
                .where(PAYMENT.INVOICE_AMOUNT.eq(coerce(amount, PAYMENT.INVOICE_AMOUNT)))
                .fetch()
                .forEach(System.out::println);
    }

    public void printOrderStatusByOrderDate2(LocalDateTime ldt) {

        ctx.select(ORDER.STATUS)
                .from(ORDER)
                .where(ORDER.ORDER_DATE.gt(coerce(ldt, LocalDate.class)))
                .fetch()
                .forEach(System.out::println);

        ctx.select(ORDER.STATUS)
                .from(ORDER)
                .where(ORDER.ORDER_DATE.coerce(SQLDataType.LOCALDATETIME).gt(ldt))
                .fetch()
                .forEach(System.out::println);

        ctx.select(ORDER.STATUS)
                .from(ORDER)
                .where(ORDER.ORDER_DATE.coerce(LocalDateTime.class).gt(ldt))
                .fetch()
                .forEach(System.out::println);
    }

    public void printProductNameAndPrice2() {
        ctx.select(PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE.coerce(Integer.class))
                .from(PRODUCT)
                .where(PRODUCT.PRODUCT_ID.eq(1L))
                .fetch()
                .forEach(System.out::println);
    }

    // cast vs coerce            
    // this doesn't work as expected
    public void printPaymentDatesByAmountCast(float amount) {

        ctx.select(PAYMENT.PAYMENT_DATE, PAYMENT.CACHING_DATE)
                .from(PAYMENT)
                .where(PAYMENT.INVOICE_AMOUNT.cast(Float.class).eq(amount))
                .fetch()
                .forEach(System.out::println);
    }
    
    // this work as expected
    public void printPaymentDatesByAmountCoerce(float amount) {

        ctx.select(PAYMENT.PAYMENT_DATE, PAYMENT.CACHING_DATE)
                .from(PAYMENT)
                .where(PAYMENT.INVOICE_AMOUNT.coerce(Float.class).eq(amount))
                .fetch()
                .forEach(System.out::println);
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
                .orderBy(PRODUCT.PRODUCT_NAME.collate("latin1_spanish_ci"))
                .fetch()
                .forEach(System.out::println);
    }
    
}