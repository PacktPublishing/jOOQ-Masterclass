package com.classicmodels.repository;

import static com.classicmodels.converter.DateToYearMonthConverter.YEARMONTH_DATE_CONVERTER;
import static com.classicmodels.converter.YearMonthConverter.INTEGER_YEARMONTH_ARR_CONVERTER;
import static com.classicmodels.converter.YearMonthConverter.INTEGER_YEARMONTH_CONVERTER;
import static com.classicmodels.converter.YearMonthConverter.YEARMONTH;
import static com.classicmodels.converter.YearMonthToDateConverter.DATE_YEARMONTH_CONVERTER;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static jooq.generated.Sequences.CUSTOMER_SEQ;
import static jooq.generated.tables.Customer.CUSTOMER;
import org.jooq.Converter;
import org.jooq.DSLContext;
import org.jooq.DataType;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.SQLDataType.INTEGER;
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

        // non-type-safe,
        // converter is not used (we insert '202010' directly)
        ctx.insertInto(CUSTOMER)
                .values(CUSTOMER_SEQ.nextval(), "Atelier One", "Markus", "Alop", "0892 339 423",
                        1370, 50000, 24249)
                .onDuplicateKeyIgnore()
                .execute();

        // non-type-safe,
        // use converter explicitly by passing to it a valid YearMonth value
        ctx.insertInto(CUSTOMER)
                .values(CUSTOMER_SEQ.nextval(), "Atelier One", "Markus", "Alop", "0892 339 423",
                        1370, 50000, INTEGER_YEARMONTH_CONVERTER.to(YearMonth.of(2020, 10)))
                .onDuplicateKeyIgnore()
                .execute();

        // non-type-safe,
        // convert via data type       
        ctx.insertInto(CUSTOMER)
                .values(CUSTOMER_SEQ.nextval(), "Atelier One", "Markus", "Alop", "0892 339 423",
                        1370, 50000, val(YearMonth.of(2020, 10), YEARMONTH))
                .onDuplicateKeyIgnore()
                .execute();

        // type-safe,
        // use converter explicitly by passing to it a valid YearMonth value
        ctx.insertInto(CUSTOMER, CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_FIRST_NAME,
                CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE, CUSTOMER.SALES_REP_EMPLOYEE_NUMBER,
                CUSTOMER.CREDIT_LIMIT, CUSTOMER.FIRST_BUY_DATE)
                .values("Atelier One", "Markus", "Alop", "0892 339 423",
                        1370L, BigDecimal.valueOf(50000),
                        INTEGER_YEARMONTH_CONVERTER.to(YearMonth.of(2020, 10)))
                .onDuplicateKeyIgnore()
                .execute();

        // type-safe,
        // nesting converters via to()
        ctx.insertInto(CUSTOMER, CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_FIRST_NAME,
                CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE, CUSTOMER.SALES_REP_EMPLOYEE_NUMBER,
                CUSTOMER.CREDIT_LIMIT, CUSTOMER.FIRST_BUY_DATE)
                .values("Atelier One", "Markus", "Alop", "0892 339 423",
                        1370L, BigDecimal.valueOf(50000),
                        INTEGER_YEARMONTH_CONVERTER.to(YEARMONTH_DATE_CONVERTER.to(new Date())))
                .onDuplicateKeyIgnore()
                .execute();

        // type-safe,
        // nesting converters via to() and inverse()
        ctx.insertInto(CUSTOMER, CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_FIRST_NAME,
                CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE, CUSTOMER.SALES_REP_EMPLOYEE_NUMBER,
                CUSTOMER.CREDIT_LIMIT, CUSTOMER.FIRST_BUY_DATE)
                .values("Atelier One", "Markus", "Alop", "0892 339 423",
                        1370L, BigDecimal.valueOf(50000),
                        INTEGER_YEARMONTH_CONVERTER.to(DATE_YEARMONTH_CONVERTER.inverse().to(new Date())))
                .onDuplicateKeyIgnore()
                .execute();

        /* CONVERTER WHICH CANCELS ITSELF OUT (CIRCULAR CONVERTER) */
        INTEGER_YEARMONTH_CONVERTER.andThen(INTEGER_YEARMONTH_CONVERTER.inverse());

        /* DECLARE CONVERTER LOCALLY, IN THIS CLASS */
        Converter<Integer, YearMonth> converter = Converter.ofNullable(Integer.class, YearMonth.class,
                (Integer t) -> {
                    return YearMonth.of(1970, 1).with(ChronoField.PROLEPTIC_MONTH, t);
                },
                (YearMonth u) -> {
                    return (int) u.getLong(ChronoField.PROLEPTIC_MONTH);
                });

        // use the above local converter, 'converter'
        ctx.insertInto(CUSTOMER)
                .values(CUSTOMER_SEQ.nextval(), "Atelier One", "Markus", "Alop", "0892 339 423",
                        1370, 50000, converter.to(YearMonth.of(2020, 10)))
                .onDuplicateKeyIgnore()
                .execute();

        /* DECLARE CUSTOM DATA TYPE LOCALLY */
        // via Converter
        DataType<YearMonth> yearmonthFromConv = INTEGER.asConvertedDataType(converter);

        // without Converter
        DataType<YearMonth> yearmonth = INTEGER.asConvertedDataType(YearMonth.class,
                (Integer t) -> {
                    return YearMonth.of(1970, 1).with(ChronoField.PROLEPTIC_MONTH, t);
                },
                (YearMonth u) -> {
                    return (int) u.getLong(ChronoField.PROLEPTIC_MONTH);
                });

        // convert via the above local data type 'yearmonth'      
        ctx.insertInto(CUSTOMER)
                .values(CUSTOMER_SEQ.nextval(), "Atelier One", "Markus", "Alop", "0892 339 423",
                        1370, 50000, val(YearMonth.of(2020, 10), yearmonth))
                .onDuplicateKeyIgnore()
                .execute();
    }

    public void findAtelierOneCustomer() {

        // fetch any CUSTOMER.FIRST_BUY_DATE and convert it via data type's convert()
        Integer resultInt = ctx.select(CUSTOMER.FIRST_BUY_DATE)
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NAME.eq("Atelier One"))
                .fetchAny(CUSTOMER.FIRST_BUY_DATE);

        System.out.println("Converter is not used, so the result is fetched as Integer: " + resultInt);
        YearMonth resultYM = YEARMONTH.convert(resultInt); // there is a convert() for array and one for List as well
        System.out.println("Use data type's convert() method: " + resultYM);

        // fetch an array of CUSTOMER.FIRST_BUY_DATE and convert via INTEGER_YEARMONTH_ARR_CONVERTER
        Integer[] resultArrInt = ctx.select(CUSTOMER.FIRST_BUY_DATE)
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NAME.eq("Atelier One"))
                .fetchArray(CUSTOMER.FIRST_BUY_DATE);

        YearMonth[] resultArrYM1 = INTEGER_YEARMONTH_ARR_CONVERTER.from(resultArrInt);
        System.out.println("Use converter for arrays: " + Arrays.toString(resultArrYM1));

        // fetch an array of CUSTOMER.FIRST_BUY_DATE and convert in fetchArray()
        YearMonth[] resultArrYM2 = ctx.select(CUSTOMER.FIRST_BUY_DATE)
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NAME.eq("Atelier One"))
                .fetchArray(CUSTOMER.FIRST_BUY_DATE, INTEGER_YEARMONTH_CONVERTER);
        System.out.println("Convert in fetchArray(): " + Arrays.toString(resultArrYM2));

        // fetch a List of CUSTOMER.FIRST_BUY_DATE and convert in fetch()
        List<YearMonth> resultListYM = ctx.select(CUSTOMER.FIRST_BUY_DATE)
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NAME.eq("Atelier One"))
                .fetch(CUSTOMER.FIRST_BUY_DATE, INTEGER_YEARMONTH_CONVERTER);
        System.out.println("Convert in fetch(): " + resultListYM);

        // fetch a List of CUSTOMER.FIRST_BUY_DATE and convert in fetch() via chained converters
        // - first, convert from INTEGER (database) to YearMonth
        // - second, convert from YearMonth to Date
        List<Date> resultDateListYM1 = ctx.select(CUSTOMER.FIRST_BUY_DATE)
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NAME.eq("Atelier One"))
                .fetch(CUSTOMER.FIRST_BUY_DATE,
                        INTEGER_YEARMONTH_CONVERTER.andThen(YEARMONTH_DATE_CONVERTER));
        System.out.println("Convert in fetch() via chained converters: " + resultDateListYM1);

        // fetch a List of CUSTOMER.FIRST_BUY_DATE and convert in fetch() via chained converters and inverse()
        // - first, convert from INTEGER (database) to YearMonth
        // - second, convert from YearMonth to Date
        List<Date> resultDateListYM2 = ctx.select(CUSTOMER.FIRST_BUY_DATE)
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NAME.eq("Atelier One"))
                .fetch(CUSTOMER.FIRST_BUY_DATE,
                        INTEGER_YEARMONTH_CONVERTER.andThen(DATE_YEARMONTH_CONVERTER.inverse()));
        System.out.println("Convert in fetch() via chained converters: " + resultDateListYM2);

        // coercing
        ctx.select(CUSTOMER.FIRST_BUY_DATE.coerce(YEARMONTH))
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NAME.eq("Atelier One"))
                .fetch();

        // casting
        ctx.select(CUSTOMER.FIRST_BUY_DATE.cast(YEARMONTH))
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NAME.eq("Atelier One"))
                .fetch();

        // new field
        ctx.select(field(CUSTOMER.FIRST_BUY_DATE.getName(), YEARMONTH))
                .from(CUSTOMER)
                .where(CUSTOMER.CUSTOMER_NAME.eq("Atelier One"))
                .fetch();
    }
}
