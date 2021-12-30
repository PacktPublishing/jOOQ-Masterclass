package com.classicmodels.repository;

import com.classicmodels.pojo.EmployeeName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Product.PRODUCT;
import jooq.generated.tables.pojos.Employee;
import jooq.generated.tables.records.EmployeeRecord;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.conf.Settings;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.sql;
import org.jooq.impl.SQLDataType;
import org.springframework.stereotype.Repository;

@Repository
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

    // coercing ResultQuery
    public void coerceResultQueryToAnotherResultQuery() {

        // fill up only the 'first_name' and 'last_name' fields of the generated POJO
        List<Employee> result1
                = ctx.resultQuery("SELECT first_name, last_name FROM employee")
                        .fetchInto(Employee.class);

        // define a custom POJO having only the 'first_name' and 'last_name' fields
        List<EmployeeName> result2
                = ctx.resultQuery("SELECT first_name, last_name FROM employee")
                        .fetchInto(EmployeeName.class);

        // without using coerce() this is Result<Record>
        Result<Record2<String, String>> result3
                = ctx.resultQuery("SELECT first_name, last_name FROM employee")
                        .coerce(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .fetch();

        // jOOQ 3.12+, using ResultQuery.coerce(Table<X> table)
        List<EmployeeRecord> result4 = ctx.resultQuery("select * from employee")
                .coerce(EMPLOYEE)
                .fetch();

        // jOOOQ until 3.12 can use this workaround
        List<EmployeeRecord> result5 = ctx.configuration().derive(new Settings()
                .withRenderSchema(Boolean.FALSE))
                .dsl()
                .select(EMPLOYEE.fields())
                .from("({0}) {1}",
                        sql("select * from employee"),
                        name(EMPLOYEE.getName()))
                .fetchInto(EMPLOYEE);
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
