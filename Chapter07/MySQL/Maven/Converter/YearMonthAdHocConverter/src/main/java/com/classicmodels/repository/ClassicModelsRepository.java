package com.classicmodels.repository;

import static com.classicmodels.converter.YearMonthConverter.INTEGER_YEARMONTH_CONVERTER;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.temporal.ChronoField;
import java.util.List;
import static jooq.generated.tables.Customer.CUSTOMER;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Transactional
    public void insertCustomer() {

        ctx.insertInto(CUSTOMER, CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_FIRST_NAME,
                CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE, CUSTOMER.SALES_REP_EMPLOYEE_NUMBER,
                CUSTOMER.CREDIT_LIMIT, CUSTOMER.FIRST_BUY_DATE.convert(INTEGER_YEARMONTH_CONVERTER))
                .values("Atelier One", "Markus", "Alop", "0892 339 423",
                        1370L, BigDecimal.valueOf(50000), YearMonth.of(2020, 10))
                .onDuplicateKeyIgnore()
                .execute();

        ctx.insertInto(CUSTOMER, CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_FIRST_NAME,
                CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE, 
                CUSTOMER.SALES_REP_EMPLOYEE_NUMBER, CUSTOMER.CREDIT_LIMIT,
                CUSTOMER.FIRST_BUY_DATE.convert(YearMonth.class,
                        t -> INTEGER_YEARMONTH_CONVERTER.from(t), // useless part of the converter
                        u -> INTEGER_YEARMONTH_CONVERTER.to(u)))
                .values("Atelier One", "Markus", "Alop", "0892 339 423",
                        1370L, BigDecimal.valueOf(50000), YearMonth.of(2020, 10))
                .onDuplicateKeyIgnore()
                .execute();
        
        ctx.insertInto(CUSTOMER, CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_FIRST_NAME,
                CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE, 
                CUSTOMER.SALES_REP_EMPLOYEE_NUMBER, CUSTOMER.CREDIT_LIMIT,
                CUSTOMER.FIRST_BUY_DATE.convert(YearMonth.class,
                        t -> YearMonth.of(1970, 1).with(ChronoField.PROLEPTIC_MONTH, t), // useless part of the converter
                        u -> (int) u.getLong(ChronoField.PROLEPTIC_MONTH)))
                .values("Atelier One", "Markus", "Alop", "0892 339 423",
                        1370L, BigDecimal.valueOf(50000), YearMonth.of(2020, 10))
                .onDuplicateKeyIgnore()
                .execute();

        ctx.insertInto(CUSTOMER, CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_FIRST_NAME,
                CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE, CUSTOMER.SALES_REP_EMPLOYEE_NUMBER, CUSTOMER.CREDIT_LIMIT,
                CUSTOMER.FIRST_BUY_DATE.convertTo(YearMonth.class, u -> INTEGER_YEARMONTH_CONVERTER.to(u)))
                .values("Atelier One", "Markus", "Alop", "0892 339 423",
                        1370L, BigDecimal.valueOf(50000), YearMonth.of(2020, 10))
                .onDuplicateKeyIgnore()
                .execute();
        
        ctx.insertInto(CUSTOMER, CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_FIRST_NAME,
                CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE, CUSTOMER.SALES_REP_EMPLOYEE_NUMBER, CUSTOMER.CREDIT_LIMIT,
                CUSTOMER.FIRST_BUY_DATE.convertTo(YearMonth.class, u -> (int) u.getLong(ChronoField.PROLEPTIC_MONTH)))
                .values("Atelier One", "Markus", "Alop", "0892 339 423",
                        1370L, BigDecimal.valueOf(50000), YearMonth.of(2020, 10))
                .onDuplicateKeyIgnore()
                .execute();
    }

    public void findAtelierOneCustomer() {

        List<YearMonth> resultListYM1 = ctx.select(
                CUSTOMER.FIRST_BUY_DATE.convert(INTEGER_YEARMONTH_CONVERTER))
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NAME.eq("Atelier One"))
                .fetchInto(YearMonth.class);

        System.out.println("Result 1: " + resultListYM1);

        List<YearMonth> resultListYM2 = ctx.select(
                CUSTOMER.FIRST_BUY_DATE.convertFrom(
                        t -> YearMonth.of(1970, 1).with(ChronoField.PROLEPTIC_MONTH, t)))
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NAME.eq("Atelier One"))
                .fetchInto(YearMonth.class);

        System.out.println("Result 2: " + resultListYM2);
        
        List<YearMonth> resultListYM3 = ctx.select(
                CUSTOMER.FIRST_BUY_DATE.convertFrom(
                        t -> INTEGER_YEARMONTH_CONVERTER.from(t)))
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NAME.eq("Atelier One"))
                .fetchInto(YearMonth.class);

        System.out.println("Result 3: " + resultListYM3);
    }
}
