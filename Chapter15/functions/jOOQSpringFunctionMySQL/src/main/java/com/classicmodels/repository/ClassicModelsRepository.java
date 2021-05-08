package com.classicmodels.repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.jooq.DatePart;
import org.jooq.Field;
import static org.jooq.impl.DSL.atan2;
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
import static org.jooq.impl.DSL.greatest;
import static org.jooq.impl.DSL.iif;
import static org.jooq.impl.DSL.least;
import static org.jooq.impl.DSL.localDate;
import static org.jooq.impl.DSL.localDateAdd;
import static org.jooq.impl.DSL.lower;
import static org.jooq.impl.DSL.month;
import static org.jooq.impl.DSL.nullif;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.nvl2;
import static org.jooq.impl.DSL.power;
import static org.jooq.impl.DSL.round;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.rpad;
import static org.jooq.impl.DSL.sign;
import static org.jooq.impl.DSL.sin;
import static org.jooq.impl.DSL.space;
import static org.jooq.impl.DSL.sqrt;
import static org.jooq.impl.DSL.substring;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.upper;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.values;
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

    ///////////////////////
    // General Functions //
    ///////////////////////
    public void generalFunctionExamples() {

        // COALESCE          
        ctx.select(DEPARTMENT.NAME, DEPARTMENT.OFFICE_CODE,
                DEPARTMENT.CASH, DEPARTMENT.ACCOUNTS_RECEIVABLE, DEPARTMENT.INVENTORIES,
                DEPARTMENT.ACCRUED_LIABILITIES, DEPARTMENT.ACCOUNTS_PAYABLE, DEPARTMENT.ST_BORROWING,
                round(coalesce(DEPARTMENT.CASH, DEPARTMENT.ACCOUNTS_RECEIVABLE,
                        DEPARTMENT.INVENTORIES, val(0)).mul(0.25), 2).as("income_deduction"),
                round(coalesce(DEPARTMENT.ACCRUED_LIABILITIES, DEPARTMENT.ACCOUNTS_PAYABLE,
                        DEPARTMENT.ST_BORROWING, val(0)).mul(0.25), 2).as("expenses_deduction"))
                .from(DEPARTMENT)
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
        //!!! pay attention that ORDER BY cannot use indexes in this case      
        ctx.select(DEPARTMENT.NAME, DEPARTMENT.LOCAL_BUDGET, DEPARTMENT.OFFICE_CODE)
                .from(DEPARTMENT)
                .orderBy(
                        decode(c,
                                "N", DEPARTMENT.NAME,
                                "B", DEPARTMENT.LOCAL_BUDGET,
                                "C", DEPARTMENT.CODE))
                .fetch();

        // DECODE AND GROUP BY        
        ctx.select(decode(sign(PRODUCT.BUY_PRICE.minus(PRODUCT.MSRP.divide(2))),
                1, "Buy price larger than half of MSRP",
                0, "Buy price equal to half of MSRP",
                -1, "Buy price smaller than half of MSRP"), count())
                .from(PRODUCT)
                .groupBy(decode(sign(PRODUCT.BUY_PRICE.minus(PRODUCT.MSRP.divide(2))),
                        1, "Buy price larger than half of MSRP",
                        0, "Buy price equal to half of MSRP",
                        -1, "Buy price smaller than half of MSRP"))
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
        ctx.select(ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.QUANTITY_ORDERED,
                iif(ORDERDETAIL.QUANTITY_ORDERED.gt(45), "MORE", "LESS").as("45"))
                .from(ORDERDETAIL)
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
        ctx.selectFrom(OFFICE)
                .where(nullif(OFFICE.COUNTRY, "").isNull())
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
                round((power(1.6180339, n).minus(power(-0.6180339, n)))
                        .divide(2.236067977), 0)
        ))).fetch();

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

        ctx.select().from(values(row(val(6371d).mul(val(2d)
                .mul(atan2(sqrt(a), sqrt(val(1d).minus(a))))))))
                .fetch();
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
    }

    ////////////////////////
    // Datetime Functions //
    ////////////////////////
    public void dateTimeFunctionsExample() {

        // get current date
        Date cd = ctx.select(currentDate()).fetchOneInto(Date.class);
        LocalDate ld = ctx.select(currentLocalDate()).fetchOneInto(LocalDate.class);

        System.out.println("Current date (java.sql.Date): " + cd);
        System.out.println("Current date (java.time.LocalDate): " + ld);

        // convert an ISO 8601 DATE string literal into a java.sql.Date
        Date ccd = ctx.select(date("2024-01-29")).fetchOneInto(Date.class);
        System.out.println("Converted date (java.sql.Date): " + ccd);

        // add an interval of 10 days to a date
        var dcd = ctx.select(date("2022-02-03"),
                dateAdd(Date.valueOf("2022-02-03"), 10).as("after_10_days")).fetch();
        System.out.println("After adding 10 days (java.sql.Date):\n" + dcd);

        // add an interval of months to a date
        var mcd = ctx.select(date("2022-02-03"),
                dateAdd(Date.valueOf("2022-02-03"), new YearToMonth(0, 3)).as("after_3_month")).fetch();
        System.out.println("After adding 3 months (java.sql.Date):\n" + mcd);

        // extract parts of a date
        int day11 = ctx.select(dayOfWeek(Date.valueOf("2021-05-06"))).fetchOneInto(Integer.class);
        int day12 = ctx.select(extract(Date.valueOf("2021-05-06"), DatePart.DAY_OF_WEEK)).fetchOneInto(Integer.class);
        System.out.println("Day of week (1 = Sunday, 2 = Monday, ..., 7 = Saturday): " + day11);
        System.out.println("Day of week (1 = Sunday, 2 = Monday, ..., 7 = Saturday): " + day12);

        int day21 = ctx.select(dayOfYear(Date.valueOf("2021-05-06"))).fetchOneInto(Integer.class);
        int day22 = ctx.select(extract(Date.valueOf("2021-05-06"), DatePart.DAY_OF_YEAR)).fetchOneInto(Integer.class);
        System.out.println("Day of year (corresponds to ChronoField.DAY_OF_YEAR): " + day21);
        System.out.println("Day of year (corresponds to ChronoField.DAY_OF_YEAR): " + day22);

        int month1 = ctx.select(month(Date.valueOf("2021-05-06"))).fetchOneInto(Integer.class);
        int month2 = ctx.select(extract(Date.valueOf("2021-05-06"), DatePart.MONTH)).fetchOneInto(Integer.class);
        System.out.println("Month (corresponds  to ChronoField.MONTH_OF_YEAR): " + month1);
        System.out.println("Month (corresponds  to ChronoField.MONTH_OF_YEAR): " + month2);

        int day31 = ctx.select(day(Date.valueOf("2021-05-06"))).fetchOneInto(Integer.class);
        int day32 = ctx.select(extract(Date.valueOf("2021-05-06"), DatePart.DAY)).fetchOneInto(Integer.class);
        System.out.println("Day (corresponds  to ChronoField.DAY_OF_MONTH): " + day31);
        System.out.println("Day (corresponds  to ChronoField.DAY_OF_MONTH): " + day32);

        // convert an ISO 8601 DATE string literal into java.time.LocalDate
        LocalDate cld = ctx.select(localDate("2021-05-06")).fetchOneInto(LocalDate.class);
        System.out.println("String to LocalDate: " + cld);

        // add 3 days to a LocalDate       
        var ldcd = ctx.select(localDateAdd(LocalDate.parse("2023-05-08"), 3)
                .as("after_3_days")).fetch();
        System.out.println("After adding 3 days (java.sql.Date):\n" + ldcd);
    }
}
