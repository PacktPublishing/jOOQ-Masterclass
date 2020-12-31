package com.classicmodels.repository;

import static com.classicmodels.converter.YearMonthConverter.INTEGER_YEARMONTH_ARR_CONVERTER;
import static com.classicmodels.converter.YearMonthConverter.INTEGER_YEARMONTH_CONVERTER;
import static com.classicmodels.converter.YearMonthConverter.YEARMONTH;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
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
                .values(null, "Atelier One", "Markus", "Alop", "0892 339 423",
                        1370L, 50000, 202010)
                .execute();

        // non-type-safe,
        // use converter explicitly by passing to it a valid YearMonth value
        ctx.insertInto(CUSTOMER)
                .values(null, "Atelier One", "Markus", "Alop", "0892 339 423",
                        1370L, 50000, INTEGER_YEARMONTH_CONVERTER.to(YearMonth.of(2020, 10)))
                .execute();        

        // non-type-safe,
        // convert via data type       
        ctx.insertInto(CUSTOMER)
                .values(null, "Atelier One", "Markus", "Alop", "0892 339 423",
                        1370L, 50000, val(YearMonth.of(2020, 10), YEARMONTH))
                .execute();        
        
        // type-safe,
        // use converter explicitly by passing to it a valid YearMonth value
        ctx.insertInto(CUSTOMER, CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_FIRST_NAME,
                CUSTOMER.CONTACT_LAST_NAME, CUSTOMER.PHONE, CUSTOMER.SALES_REP_EMPLOYEE_NUMBER,
                CUSTOMER.CREDIT_LIMIT, CUSTOMER.FIRST_BUY_DATE)
                .values("Atelier One", "Markus", "Alop", "0892 339 423",
                        1370L, BigDecimal.valueOf(50000), 
                        INTEGER_YEARMONTH_CONVERTER.to(YearMonth.of(2020, 10)))
                .execute();
        
        /* DECLARE CONVERTER LOCALLY, IN THIS CLASS */
        Converter<Integer, YearMonth> converter = Converter.ofNullable(Integer.class, YearMonth.class,
                (Integer t) -> {
                    return YearMonth.of(t / 100, t % 100);
                },
                (YearMonth u) -> {
                    return (u.getYear() * 100) + u.getMonth().getValue();
                });

        // use the above local converter, 'converter'
        ctx.insertInto(CUSTOMER)
                .values(null, "Atelier One", "Markus", "Alop", "0892 339 423",
                        1370L, 50000, converter.to(YearMonth.of(2020, 10)))
                .execute();                

        /* DECLARE CUSTOM DATA TYPE LOCALLY */
        DataType<YearMonth> yearmonthFromConv = INTEGER.asConvertedDataType(converter);
        DataType<YearMonth> yearmonth = INTEGER.asConvertedDataType(YearMonth.class,
                (Integer t) -> {
                    return YearMonth.of(t / 100, t % 100);
                },
                (YearMonth u) -> {
                    return (u.getYear() * 100) + u.getMonth().getValue();
                });
        
        // convert via the above local data type 'yearmonth'      
        ctx.insertInto(CUSTOMER)
                .values(null, "Atelier One", "Markus", "Alop", "0892 339 423",
                        1370L, 50000, val(YearMonth.of(2020, 10), yearmonth))
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
        System.out.println("Convert in fetchArray(): " + resultListYM);

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