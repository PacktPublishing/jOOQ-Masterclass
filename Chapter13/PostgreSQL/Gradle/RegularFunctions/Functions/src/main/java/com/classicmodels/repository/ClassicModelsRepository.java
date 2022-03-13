package com.classicmodels.repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.jooq.DatePart;
import org.jooq.Field;
import static org.jooq.impl.DSL.aggregate;
import static org.jooq.impl.DSL.atan2;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.castNull;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.cos;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.currentDate;
import static org.jooq.impl.DSL.currentLocalDate;
import static org.jooq.impl.DSL.date;
import static org.jooq.impl.DSL.dateAdd;
import static org.jooq.impl.DSL.day;
import static org.jooq.impl.DSL.dayOfWeek;
import static org.jooq.impl.DSL.dayOfYear;
import static org.jooq.impl.DSL.decode;
import static org.jooq.impl.DSL.extract;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.greatest;
import static org.jooq.impl.DSL.ifnull;
import static org.jooq.impl.DSL.iif;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.isnull;
import static org.jooq.impl.DSL.least;
import static org.jooq.impl.DSL.localDate;
import static org.jooq.impl.DSL.localDateAdd;
import static org.jooq.impl.DSL.lower;
import static org.jooq.impl.DSL.month;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.nullif;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.nvl2;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.position;
import static org.jooq.impl.DSL.power;
import static org.jooq.impl.DSL.round;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.rpad;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sign;
import static org.jooq.impl.DSL.sin;
import static org.jooq.impl.DSL.space;
import static org.jooq.impl.DSL.sqrt;
import static org.jooq.impl.DSL.substring;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.toLocalDateTime;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.DSL.upper;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.values;
import static org.jooq.impl.SQLDataType.NUMERIC;
import org.jooq.types.YearToMonth;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }
    
    public void someNullsStuffGoodToKnow() {

        /*
        Statement	        Result	Comment
        SELECT NULL = NULL;	NULL	NULL cannot be compared to NULL
        SELECT NULL > 0;	NULL	NULL can't be compared
        SELECT NULL < 0;	NULL	NULL can't be compared
        SELECT NULL = 0;	NULL	NULL can't be equated
        SELECT NULL / 0;	NULL	So, no Division by Zero error ?!
        SELECT NULL OR FALSE;	NULL	NULL can't be used in boolean logic
        SELECT NULL OR TRUE;	TRUE	Surprising and database dependent!
        */

        // all these return NULL
        ctx.select(field(castNull(Integer.class).eq(castNull(Integer.class))).as("r")).fetch();
        ctx.select(field(castNull(Integer.class).gt(0)).as("r")).fetch();
        ctx.select(field(castNull(Integer.class).lt(0)).as("r")).fetch();
        ctx.select(field(castNull(Integer.class).eq(0)).as("r")).fetch();
        ctx.select(field(castNull(Integer.class).divide(0)).as("r")).fetch();                
        ctx.select(field(trueCondition().or(castNull(Integer.class).eq(0))).as("r")).fetch();        
        ctx.select(field(falseCondition().or(castNull(Integer.class).eq(0))).as("r")).fetch();        

        // IS DISTINCT FROM and IS NOT DISTINCT FROM that specially 
        // treats NULL values as if it were a known value
        
        /*
        X	        Y	        IS DISTINCT FROM
        not NULL	not NULL	rely on the regular not equal '<>' operator
        not NULL	NULL	        return TRUE because they are different
        NULL	        not NULL	return TRUE because they are different
        NULL	        NULL	        return FALSE because they are the same
        */
        
        ctx.select(field(castNull(Integer.class).isDistinctFrom(castNull(Integer.class))).as("r")).fetch();
        ctx.select(field(castNull(Integer.class).isNotDistinctFrom(castNull(Integer.class))).as("r")).fetch();
        ctx.select(field(castNull(Integer.class).isDistinctFrom(0)).as("r")).fetch();
        ctx.select(field(castNull(Integer.class).isNotDistinctFrom(0)).as("r")).fetch();

        // IS NULL and IS NOT NULL
        ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY)
                .from(OFFICE)
                .where(OFFICE.CITY.isNull())
                .fetch();

        ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY)
                .from(OFFICE)
                .where(OFFICE.CITY.isNotNull())
                .fetch();
        
        // sorting NULLs
        ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY)
                .from(OFFICE)
                .orderBy(OFFICE.CITY.desc().nullsFirst())
                .fetch();

        ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY)
                .from(OFFICE)
                .orderBy(OFFICE.CITY.desc().nullsLast())
                .fetch();
    }

    ///////////////////////
    // General Functions //
    ///////////////////////
    public void generalFunctionExamples() {

        // COALESCE          
        ctx.select(DEPARTMENT.NAME, DEPARTMENT.OFFICE_CODE,
                DEPARTMENT.CASH, DEPARTMENT.ACCOUNTS_RECEIVABLE, DEPARTMENT.INVENTORIES,
                DEPARTMENT.ACCRUED_LIABILITIES, DEPARTMENT.ACCOUNTS_PAYABLE, DEPARTMENT.ST_BORROWING,
                round(coalesce(DEPARTMENT.CASH, DEPARTMENT.ACCOUNTS_RECEIVABLE,
                        DEPARTMENT.INVENTORIES, inline(0)).mul(0.25), 2).as("income_deduction"),
                round(coalesce(DEPARTMENT.ACCRUED_LIABILITIES, DEPARTMENT.ACCOUNTS_PAYABLE,
                        DEPARTMENT.ST_BORROWING, inline(0)).mul(0.25), 2).as("expenses_deduction"))
                .from(DEPARTMENT)
                .fetch();

        // Fill gaps in forecast profit        
        ctx.select(DEPARTMENT.NAME, DEPARTMENT.OFFICE_CODE, DEPARTMENT.FORECAST_PROFIT,
                DEPARTMENT.PROFIT,
                coalesce(DEPARTMENT.FORECAST_PROFIT,
                        select(avg(field(name("t", "forecast_profit"), Double.class))).from(DEPARTMENT.as("t"))
                                .where(coalesce(field(name("t", "profit")), 0)
                                        .gt(coalesce(DEPARTMENT.PROFIT, 0))
                                        .and(field(name("t", "forecast_profit")).isNotNull())))
                        .as("fill_forecast_profit"))
                .from(DEPARTMENT)
                .orderBy(DEPARTMENT.DEPARTMENT_ID)
                .fetch();

        // DECODE        
        ctx.select(DEPARTMENT.NAME, DEPARTMENT.OFFICE_CODE, DEPARTMENT.LOCAL_BUDGET,
                decode(DEPARTMENT.LOCAL_BUDGET,
                        castNull(Double.class), 0, DEPARTMENT.LOCAL_BUDGET.mul(0.25))
                        .mul(2).divide(100).as("financial_index"))
                .from(DEPARTMENT)
                .fetch();

        // DECODE AND MULTIPLE VALUES        
        ctx.select(DEPARTMENT.NAME, DEPARTMENT.OFFICE_CODE, DEPARTMENT.LOCAL_BUDGET,
                decode(DEPARTMENT.NAME,
                        "Advertising", "Publicity and promotion",
                        "Accounting", "Monetary and business",
                        "Logistics", "Facilities and supplies",
                        DEPARTMENT.NAME).concat(" department").as("description"))
                .from(DEPARTMENT)
                .fetch();

        // DECODE AND ORDER BY
        String c = "N"; // input parameter (it may come from the database), 
        // !!! pay attention that ORDER BY cannot use indexes in this case      
        ctx.select(DEPARTMENT.NAME, DEPARTMENT.LOCAL_BUDGET, DEPARTMENT.OFFICE_CODE)
                .from(DEPARTMENT)
                .orderBy(
                        decode(c,
                                "N", DEPARTMENT.NAME,
                                "B", DEPARTMENT.LOCAL_BUDGET.cast(String.class),
                                "C", DEPARTMENT.CODE.cast(String.class)))
                .fetch();

        // DECODE AND GROUP BY        
        ctx.select(field(name("t", "d")), count()).from(
                select(decode(sign(PRODUCT.BUY_PRICE.minus(PRODUCT.MSRP.divide(2))),
                        1, "Buy price larger than half of MSRP",
                        0, "Buy price equal to half of MSRP",
                        -1, "Buy price smaller than half of MSRP").as("d"))
                        .from(PRODUCT)
                        .groupBy(PRODUCT.BUY_PRICE, PRODUCT.MSRP).asTable("t"))
                .groupBy(field(name("t", "d")))
                .fetch();

        // DECODE AND SUM        
        ctx.select(PRODUCT.PRODUCT_LINE,
                sum(decode(greatest(PRODUCT.BUY_PRICE, 0), least(PRODUCT.BUY_PRICE, 35), 1, 0)).as("< 35"),
                sum(decode(greatest(PRODUCT.BUY_PRICE, 36), least(PRODUCT.BUY_PRICE, 55), 1, 0)).as("36-55"),
                sum(decode(greatest(PRODUCT.BUY_PRICE, 56), least(PRODUCT.BUY_PRICE, 75), 1, 0)).as("56-75"),
                sum(decode(greatest(PRODUCT.BUY_PRICE, 76), least(PRODUCT.BUY_PRICE, 150), 1, 0)).as("76-150"))
                .from(PRODUCT)
                .groupBy(PRODUCT.PRODUCT_LINE)
                .fetch();

        // of course, you can write the same thing as here        
        ctx.select(PRODUCT.PRODUCT_LINE,
                count().filterWhere(PRODUCT.BUY_PRICE.gt(BigDecimal.ZERO).and(PRODUCT.BUY_PRICE.lt(BigDecimal.valueOf(35)))).as("< 35"),
                count().filterWhere(PRODUCT.BUY_PRICE.gt(BigDecimal.valueOf(36)).and(PRODUCT.BUY_PRICE.lt(BigDecimal.valueOf(55)))).as("36-55"),
                count().filterWhere(PRODUCT.BUY_PRICE.gt(BigDecimal.valueOf(56)).and(PRODUCT.BUY_PRICE.lt(BigDecimal.valueOf(75)))).as("56-75"),
                count().filterWhere(PRODUCT.BUY_PRICE.gt(BigDecimal.valueOf(76)).and(PRODUCT.BUY_PRICE.lt(BigDecimal.valueOf(150)))).as("76-150"))
                .from(PRODUCT)
                .groupBy(PRODUCT.PRODUCT_LINE)
                .fetch();

        // DECODE AND DECODE        
        ctx.select(DEPARTMENT.NAME, DEPARTMENT.OFFICE_CODE,
                DEPARTMENT.LOCAL_BUDGET, DEPARTMENT.PROFIT,
                decode(DEPARTMENT.LOCAL_BUDGET, castNull(Double.class), DEPARTMENT.PROFIT,
                        decode(sign(DEPARTMENT.PROFIT.minus(DEPARTMENT.LOCAL_BUDGET)),
                                1, DEPARTMENT.PROFIT.minus(DEPARTMENT.LOCAL_BUDGET),
                                0, DEPARTMENT.LOCAL_BUDGET.divide(2).mul(-1),
                                -1, DEPARTMENT.LOCAL_BUDGET.mul(-1))).as("profit_balance"))
                .from(DEPARTMENT)
                .fetch();

        // IIF
        ctx.select(DEPARTMENT.DEPARTMENT_ID, DEPARTMENT.NAME,
                iif(DEPARTMENT.LOCAL_BUDGET.isNull(), "NO BUDGET", "HAS BUDGET").as("budget"))
                .from(DEPARTMENT)
                .fetch();

        ctx.select(ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.QUANTITY_ORDERED,
                iif(ORDERDETAIL.QUANTITY_ORDERED.gt(45), "MORE", "LESS").as("45"))
                .from(ORDERDETAIL)
                .fetch();

        // check this example simplified via ifnull() in the IFNULL() section
        ctx.select(DEPARTMENT.NAME, DEPARTMENT.OFFICE_CODE,
                iif(DEPARTMENT.LOCAL_BUDGET.isNull(),
                        ((iif(DEPARTMENT.CASH.isNull(), 0, DEPARTMENT.CASH)
                                .plus(iif(DEPARTMENT.ACCOUNTS_RECEIVABLE.isNull(), 0, DEPARTMENT.ACCOUNTS_RECEIVABLE))
                                .plus(iif(DEPARTMENT.INVENTORIES.isNull(), 0, DEPARTMENT.INVENTORIES)))
                                .minus(iif(DEPARTMENT.ACCOUNTS_PAYABLE.isNull(), 0, DEPARTMENT.ACCOUNTS_PAYABLE)
                                        .plus(iif(DEPARTMENT.ACCRUED_LIABILITIES.isNull(), 0, DEPARTMENT.ACCRUED_LIABILITIES))
                                        .plus(iif(DEPARTMENT.ST_BORROWING.isNull(), 0, DEPARTMENT.ST_BORROWING)))),
                        DEPARTMENT.LOCAL_BUDGET.minus(iif(DEPARTMENT.ACCOUNTS_PAYABLE.isNull(), 0, DEPARTMENT.ACCOUNTS_PAYABLE)
                                .plus(iif(DEPARTMENT.ACCRUED_LIABILITIES.isNull(), 0, DEPARTMENT.ACCRUED_LIABILITIES)
                                        .plus(iif(DEPARTMENT.ST_BORROWING.isNull(), 0, DEPARTMENT.ST_BORROWING))))).as("budget"))
                .from(DEPARTMENT)
                .fetch();

        ctx.select(
                iif(PRODUCT.PRODUCT_SCALE.eq("1:10"), "A",
                        iif(PRODUCT.PRODUCT_SCALE.eq("1:12"), "B",
                                iif(PRODUCT.PRODUCT_SCALE.eq("1:18"), "C",
                                        iif(PRODUCT.PRODUCT_SCALE.eq("1:24"), "D",
                                                iif(PRODUCT.PRODUCT_SCALE.eq("1:32"), "E",
                                                        iif(PRODUCT.PRODUCT_SCALE.eq("1:50"), "F",
                                                                iif(PRODUCT.PRODUCT_SCALE.eq("1:72"), "G",
                                                                        iif(PRODUCT.PRODUCT_SCALE.eq("1:700"), "H", "N/A")
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                ).as("class_scale"), count())
                .from(PRODUCT)
                .groupBy(PRODUCT.PRODUCT_SCALE)
                .fetch();

        // NULLIF
        ctx.select(OFFICE.OFFICE_CODE, nullif(OFFICE.COUNTRY, ""))
                .from(OFFICE)
                .fetch();

        ctx.selectFrom(OFFICE)
                .where(nullif(OFFICE.COUNTRY, "").isNull())
                .fetch();

        // IFNULL and ISNULL()        
        ctx.select(DEPARTMENT.DEPARTMENT_ID, DEPARTMENT.NAME,
                ifnull(DEPARTMENT.LOCAL_BUDGET, 0).as("budget_if"),
                isnull(DEPARTMENT.LOCAL_BUDGET, 0).as("budget_is"))
                .from(DEPARTMENT)
                .fetch();

        ctx.select(ifnull(CUSTOMERDETAIL.POSTAL_CODE, CUSTOMERDETAIL.ADDRESS_LINE_FIRST).as("address_if"),
                isnull(CUSTOMERDETAIL.POSTAL_CODE, CUSTOMERDETAIL.ADDRESS_LINE_FIRST).as("address_is"))
                .from(CUSTOMERDETAIL)
                .fetch();

        // re-written version of the IIF() approach (using isnull() is trivial)
        ctx.select(DEPARTMENT.NAME, DEPARTMENT.OFFICE_CODE,
                iif(DEPARTMENT.LOCAL_BUDGET.isNull(),
                        ((ifnull(DEPARTMENT.CASH, 0)
                                .plus(ifnull(DEPARTMENT.ACCOUNTS_RECEIVABLE, 0))
                                .plus(ifnull(DEPARTMENT.INVENTORIES, 0)))
                                .minus(ifnull(DEPARTMENT.ACCOUNTS_PAYABLE, 0)
                                        .plus(ifnull(DEPARTMENT.ACCRUED_LIABILITIES, 0))
                                        .plus(ifnull(DEPARTMENT.ST_BORROWING, 0)))),
                        DEPARTMENT.LOCAL_BUDGET.minus(ifnull(DEPARTMENT.ACCOUNTS_PAYABLE, 0)
                                .plus(ifnull(DEPARTMENT.ACCRUED_LIABILITIES, 0)
                                        .plus(ifnull(DEPARTMENT.ST_BORROWING, 0))))).as("budget"))
                .from(DEPARTMENT)
                .fetch();

        // NVL        
        ctx.select(OFFICE.OFFICE_CODE, nvl(OFFICE.CITY, "N/A"), nvl(OFFICE.COUNTRY, "N/A"))
                .from(OFFICE)
                .fetch();

        // ((ACTUAL PROFIT ÷ FORECAST PROFIT) - 1) * 100, variance formula        
        ctx.select(DEPARTMENT.NAME, DEPARTMENT.OFFICE_CODE,
                DEPARTMENT.LOCAL_BUDGET, DEPARTMENT.FORECAST_PROFIT, DEPARTMENT.PROFIT,
                round((DEPARTMENT.PROFIT.divide(DEPARTMENT.FORECAST_PROFIT)).minus(1d).mul(100), 2)
                        .concat("%").as("no_nvl"),
                round((nvl(DEPARTMENT.PROFIT, 0d).divide(
                        nvl(DEPARTMENT.FORECAST_PROFIT, 10000d))).minus(1d).mul(100), 2)
                        .concat("%").as("nvl"))
                .from(DEPARTMENT)
                .fetch();

        // NVL2        
        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME,
                iif(EMPLOYEE.COMMISSION.isNull(),
                        EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(EMPLOYEE.COMMISSION)).as("iif1"),
                iif(EMPLOYEE.COMMISSION.isNotNull(),
                        EMPLOYEE.SALARY.plus(EMPLOYEE.COMMISSION), EMPLOYEE.SALARY).as("iif2"),
                nvl2(EMPLOYEE.COMMISSION,
                        EMPLOYEE.SALARY.plus(EMPLOYEE.COMMISSION), EMPLOYEE.SALARY).as("nvl2"))
                .from(EMPLOYEE)
                .fetch();
    }

    ///////////////////////
    // Numeric Functions //
    ///////////////////////
    public void numericFunctionsExamples() {

        // Fibonacci
        int n = 15;

        ctx.select().from(values(row(
                round(cast((power(1.6180339, n).minus(power(-0.6180339, n)))
                        .divide(2.236067977), NUMERIC), 0)
        ))).fetch();
        
        ctx.select(round(cast((power(1.6180339, n).minus(power(-0.6180339, n)))
                        .divide(2.236067977), NUMERIC), 0))
                .fetch();
        
        ctx.fetchValue(round(cast((power(1.6180339, n).minus(power(-0.6180339, n)))
                        .divide(2.236067977), NUMERIC), 0));

        // Distance between two points
        // Banesti, Romania  
        double latitude1 = 45.10057933230524;
        double longitude1 = 25.76015481483892;

        // Tabriz, Azarbaidjan
        double latitude2 = 38.09271544696884;
        double longitude2 = 46.278607862213306;

        // Math behind the SQL
        /* 
        a = POWER(SIN((latitude2 − latitude1) / 2.0)), 2)
             + COS(latitude1) * COS(latitude2) * POWER (SIN((longitude2 − longitude1) / 2.0), 2);                 
        RETURN (6371.0 * (2.0 * ATN2(SQRT(a),SQRT(1.0 − a))));
         */
        double pi180 = Math.PI / 180;

        Field<BigDecimal> a = (power(sin(val((latitude2 - latitude1) * pi180).divide(2d)), 2d)
                .plus(cos(latitude1 * pi180).mul(cos(latitude2 * pi180))
                        .mul(power(sin(val((longitude2 - longitude1) * pi180).divide(2d)), 2d))));

        ctx.select().from(values(row(inline(6371d).mul(inline(2d)
                .mul(atan2(sqrt(a), sqrt(inline(1d).minus(a))))))))
                .fetch();
        
        ctx.select(inline(6371d).mul(inline(2d)
                .mul(atan2(sqrt(a), sqrt(inline(1d).minus(a))))))
                .fetch();
        
        ctx.fetchValue(inline(6371d).mul(inline(2d)
                .mul(atan2(sqrt(a), sqrt(inline(1d).minus(a))))));
    }

    //////////////////////
    // String Functions //
    //////////////////////
    public void stringFunctionsExample() {

        ctx.select(concat(upper(EMPLOYEE.FIRST_NAME), space(1),
                substring(EMPLOYEE.LAST_NAME, 1, 1).concat(". ("),
                lower(EMPLOYEE.JOB_TITLE),
                rpad(val(")"), 4, '.')).as("employee"))
                .from(EMPLOYEE)
                .fetch();

        // the next two queries does the same thing - split an email as "name@domain" into "name" and "domain"
        ctx.select(EMPLOYEE.EMAIL,
                substring(EMPLOYEE.EMAIL, one(),
                        position(EMPLOYEE.EMAIL, "@").minus(1)).as("name"),
                substring(EMPLOYEE.EMAIL,
                        position(EMPLOYEE.EMAIL, "@").plus(1)).as("domain"))
                .from(EMPLOYEE)
                .fetch();

        ctx.select(EMPLOYEE.EMAIL,
                aggregate("split_part", String.class,
                        EMPLOYEE.EMAIL, inline("@"), one()).as("name"),
                aggregate("split_part", String.class,
                        EMPLOYEE.EMAIL, inline("@"), inline(2)).as("domain"))
                .from(EMPLOYEE)
                .fetch();        
    }

    ////////////////////////
    // Datetime Functions //
    ////////////////////////
    public void dateTimeFunctionsExample() {
        
        // get current date
        Date cd1 = ctx.select(currentDate()).fetchOneInto(Date.class);
        LocalDate ld1 = ctx.select(currentLocalDate()).fetchOneInto(LocalDate.class);
        Date cd2 = ctx.fetchValue(currentDate());
        LocalDate ld2 = ctx.fetchValue(currentLocalDate());

        System.out.println("Current date (java.sql.Date): \n" + cd1 + "\n" + cd2);
        System.out.println("Current date (java.time.LocalDate): \n" + ld1 + "\n" + ld2);

        // convert an ISO 8601 DATE string literal into a java.sql.Date
        Date ccd1 = ctx.select(date("2024-01-29")).fetchOneInto(Date.class);
        Date ccd2 = ctx.fetchValue(date("2024-01-29"));
        System.out.println("Converted date (java.sql.Date): \n" + ccd1 + "\n" + ccd2);

        // add an interval of 10 days to a date
        var dcd1 = ctx.select(date("2022-02-03"),
                dateAdd(Date.valueOf("2022-02-03"), 10).as("after_10_days")).fetch();
        var dcd2 = ctx.fetchValue(dateAdd(Date.valueOf("2022-02-03"), 10).as("after_10_days"));
        System.out.println("After adding 10 days (java.sql.Date):\n" + dcd1 + "\n" + dcd2);

        // add an interval of months to a date
        var mcd1 = ctx.select(date("2022-02-03"),
                dateAdd(Date.valueOf("2022-02-03"), new YearToMonth(0, 3)).as("after_3_month")).fetch();
        var mcd2 = ctx.fetchValue(dateAdd(Date.valueOf("2022-02-03"), new YearToMonth(0, 3)).as("after_3_month"));
        System.out.println("After adding 3 months (java.sql.Date):\n" + mcd1 + "\n" + mcd2);

        // extract parts of a date
        int day111 = ctx.select(dayOfWeek(Date.valueOf("2021-05-06"))).fetchOneInto(Integer.class);
        int day112 = ctx.fetchValue(dayOfWeek(Date.valueOf("2021-05-06")));
        int day121 = ctx.select(extract(Date.valueOf("2021-05-06"), DatePart.DAY_OF_WEEK)).fetchOneInto(Integer.class);
        int day122 = ctx.fetchValue(extract(Date.valueOf("2021-05-06"), DatePart.DAY_OF_WEEK));
        System.out.println("Day of week (1 = Sunday, 2 = Monday, ..., 7 = Saturday): \n" + day111 + "\n" + day112);
        System.out.println("Day of week (1 = Sunday, 2 = Monday, ..., 7 = Saturday): \n" + day121 + "\n" + day122);

        int day211 = ctx.select(dayOfYear(Date.valueOf("2021-05-06"))).fetchOneInto(Integer.class);
        int day212 = ctx.fetchValue(dayOfYear(Date.valueOf("2021-05-06")));
        int day221 = ctx.select(extract(Date.valueOf("2021-05-06"), DatePart.DAY_OF_YEAR)).fetchOneInto(Integer.class);
        int day222 = ctx.fetchValue(extract(Date.valueOf("2021-05-06"), DatePart.DAY_OF_YEAR));
        System.out.println("Day of year (corresponds to ChronoField.DAY_OF_YEAR): \n" + day211 + "\n" + day212);
        System.out.println("Day of year (corresponds to ChronoField.DAY_OF_YEAR): \n" + day221 + "\n" + day222);

        int month11 = ctx.select(month(Date.valueOf("2021-05-06"))).fetchOneInto(Integer.class);
        int month12 = ctx.fetchValue(month(Date.valueOf("2021-05-06")));
        int month21 = ctx.select(extract(Date.valueOf("2021-05-06"), DatePart.MONTH)).fetchOneInto(Integer.class);
        int month22 = ctx.fetchValue(extract(Date.valueOf("2021-05-06"), DatePart.MONTH));
        System.out.println("Month (corresponds  to ChronoField.MONTH_OF_YEAR): \n" + month11 + "\n" + month12);
        System.out.println("Month (corresponds  to ChronoField.MONTH_OF_YEAR): \n" + month21 + "\n" + month22);

        int day311 = ctx.select(day(Date.valueOf("2021-05-06"))).fetchOneInto(Integer.class);
        int day312 = ctx.fetchValue(day(Date.valueOf("2021-05-06")));
        int day321 = ctx.select(extract(Date.valueOf("2021-05-06"), DatePart.DAY)).fetchOneInto(Integer.class);
        int day322 = ctx.fetchValue(extract(Date.valueOf("2021-05-06"), DatePart.DAY));
        System.out.println("Day (corresponds  to ChronoField.DAY_OF_MONTH): \n" + day311 + "\n" + day312);
        System.out.println("Day (corresponds  to ChronoField.DAY_OF_MONTH): \n" + day321 + "\n" + day322);

        // convert an ISO 8601 DATE string literal into java.time.LocalDate
        LocalDate cld1 = ctx.select(localDate("2021-05-06")).fetchOneInto(LocalDate.class);
        LocalDate cld2 = ctx.fetchValue(localDate("2021-05-06"));
        System.out.println("String to LocalDate: \n" + cld1 + "\n" + cld2);

        // add 3 days to a LocalDate       
        var ldcd1 = ctx.select(localDateAdd(LocalDate.parse("2023-05-08"), 3)
                .as("after_3_days")).fetch();
        var ldcd2 = ctx.fetchValue(localDateAdd(LocalDate.parse("2023-05-08"), 3)
                .as("after_3_days"));
        System.out.println("After adding 3 days (java.sql.Date):\n" + ldcd1 + "\n" + ldcd2);

        // Parse a string value to a java.time.LocalDateTime
        LocalDateTime fd1 = ctx.select(toLocalDateTime("20210501170000",
                "YYYYMMDDHH24MISS")).fetchOneInto(LocalDateTime.class);
        LocalDateTime fd2 = ctx.fetchValue(toLocalDateTime("20210501170000", "YYYYMMDDHH24MISS"));
        System.out.println("Format date: \n" + fd1 + "\n" + fd2);
    }
}
