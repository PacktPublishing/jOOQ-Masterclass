package com.classicmodels.repository;

import java.time.LocalDate;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.all;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.avgDistinct;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.countDistinct;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.maxDistinct;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.minDistinct;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.partitionBy;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.rowNumber;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectDistinct;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.sumDistinct;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // EXAMPLE 1
    /*
    select 
      distinct "CLASSICMODELS"."OFFICE"."CITY", 
      "CLASSICMODELS"."OFFICE"."COUNTRY" 
    from 
      "CLASSICMODELS"."OFFICE" 
    where 
      (
        "CLASSICMODELS"."OFFICE"."CITY" is not null 
        and "CLASSICMODELS"."OFFICE"."COUNTRY" is not null
      )    
    */
    public void findDistinctOfficesCityCountry() {

        System.out.println("EXAMPLE 1\n"
                + ctx.selectDistinct(OFFICE.CITY, OFFICE.COUNTRY)
                        .from(OFFICE)
                        .where(row(OFFICE.CITY, OFFICE.COUNTRY).isNotNull())
                        .fetch()
        );
    }

    // EXAMPLE 2
    /*
    select 
      "CLASSICMODELS"."OFFICE"."OFFICE_CODE", 
      "CLASSICMODELS"."OFFICE"."CITY", 
      "CLASSICMODELS"."OFFICE"."PHONE", 
      "CLASSICMODELS"."OFFICE"."ADDRESS_LINE_FIRST", 
      "CLASSICMODELS"."OFFICE"."ADDRESS_LINE_SECOND", 
      "CLASSICMODELS"."OFFICE"."STATE", 
      "CLASSICMODELS"."OFFICE"."COUNTRY", 
      "CLASSICMODELS"."OFFICE"."POSTAL_CODE", 
      "CLASSICMODELS"."OFFICE"."TERRITORY" 
    from 
      "CLASSICMODELS"."OFFICE" 
    where 
      decode(
        "CLASSICMODELS"."OFFICE"."ADDRESS_LINE_SECOND", 
        ?, 1, 0
      ) = 0    
    */
    public void findOfficeDistinctFromAddress() {

        System.out.println("EXAMPLE 2\n"
                + ctx.selectFrom(OFFICE)
                        .where(OFFICE.ADDRESS_LINE_SECOND.isDistinctFrom("Level 22"))
                        .fetch()
        );
    }

    // EXAMPLE 3
    /*
    select 
      "CLASSICMODELS"."PAYMENT"."INVOICE_AMOUNT", 
      "CLASSICMODELS"."PAYMENT"."PAYMENT_DATE" 
    from 
      "CLASSICMODELS"."PAYMENT" 
    where 
      decode(
        cast(
          "CLASSICMODELS"."PAYMENT"."PAYMENT_DATE" as date
        ), 
        cast(
          "CLASSICMODELS"."PAYMENT"."CACHING_DATE" as date
        ), 
        1, 
        0
      ) = 0    
    */
    public void findDistinctAndNotDistinctPaymentDates() {

        System.out.println("EXAMPLE 3.1\n" +
                ctx.select(PAYMENT.INVOICE_AMOUNT, PAYMENT.PAYMENT_DATE)
                        .from(PAYMENT)
                        .where(PAYMENT.PAYMENT_DATE.cast(LocalDate.class).isDistinctFrom(
                                PAYMENT.CACHING_DATE.cast(LocalDate.class)))
                        .fetch()
        );
        
        System.out.println("EXAMPLE 3.2\n" +
                ctx.select(PAYMENT.INVOICE_AMOUNT, PAYMENT.PAYMENT_DATE)
                        .from(PAYMENT)
                        .where(PAYMENT.PAYMENT_DATE.cast(LocalDate.class).isNotDistinctFrom(
                                PAYMENT.CACHING_DATE.cast(LocalDate.class)))
                        .fetch()
        );
    }
    
    // EXAMPLE 4
    /*
    select 
      "CLASSICMODELS"."OFFICE"."OFFICE_CODE", 
      "CLASSICMODELS"."OFFICE"."CITY", 
      "CLASSICMODELS"."OFFICE"."PHONE", 
      "CLASSICMODELS"."OFFICE"."ADDRESS_LINE_FIRST", 
      "CLASSICMODELS"."OFFICE"."ADDRESS_LINE_SECOND", 
      "CLASSICMODELS"."OFFICE"."STATE", 
      "CLASSICMODELS"."OFFICE"."COUNTRY", 
      "CLASSICMODELS"."OFFICE"."POSTAL_CODE", 
      "CLASSICMODELS"."OFFICE"."TERRITORY", 
      "CLASSICMODELS"."CUSTOMERDETAIL"."CUSTOMER_NUMBER", 
      "CLASSICMODELS"."CUSTOMERDETAIL"."ADDRESS_LINE_FIRST", 
      "CLASSICMODELS"."CUSTOMERDETAIL"."ADDRESS_LINE_SECOND", 
      "CLASSICMODELS"."CUSTOMERDETAIL"."CITY", 
      "CLASSICMODELS"."CUSTOMERDETAIL"."STATE", 
      "CLASSICMODELS"."CUSTOMERDETAIL"."POSTAL_CODE", 
      "CLASSICMODELS"."CUSTOMERDETAIL"."COUNTRY" 
    from 
      "CLASSICMODELS"."OFFICE" 
      join "CLASSICMODELS"."CUSTOMERDETAIL" on "CLASSICMODELS"."OFFICE"."POSTAL_CODE" 
         = "CLASSICMODELS"."CUSTOMERDETAIL"."POSTAL_CODE" 
    where 
      not (
        exists (
          select 
            "CLASSICMODELS"."OFFICE"."CITY", 
            "CLASSICMODELS"."OFFICE"."COUNTRY" 
          from 
            dual 
          intersect 
          select 
            "CLASSICMODELS"."CUSTOMERDETAIL"."CITY", 
            "CLASSICMODELS"."CUSTOMERDETAIL"."COUNTRY" 
          from 
            dual
        )
      )    
    */
    public void findOfficeAndCustomerOfficePostalCodeDistinctCityCountry() {

        System.out.println("EXAMPLE 4\n" +
                ctx.select()
                        .from(OFFICE)
                        .innerJoin(CUSTOMERDETAIL)
                        .on(OFFICE.POSTAL_CODE.eq(CUSTOMERDETAIL.POSTAL_CODE))
                        .where(row(OFFICE.CITY, OFFICE.COUNTRY).isDistinctFrom(
                                row(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)))
                        .fetch()
        );
    }

    // EXAMPLE 5
    /*
    select 
      count(*) "all", 
      count(
        "CLASSICMODELS"."PAYMENT"."CACHING_DATE"
      ) "all_caching_date", 
      count(
        distinct "CLASSICMODELS"."PAYMENT"."CACHING_DATE"
      ) "distinct_cachcing_date" 
    from 
      "CLASSICMODELS"."PAYMENT"    
    */
    public void countPaymentCachingDate() {

        System.out.println("EXAMPLE 5\n" +
                ctx.select(
                        count().as("all"),
                        count(PAYMENT.CACHING_DATE).as("all_caching_date"),
                        countDistinct(PAYMENT.CACHING_DATE).as("distinct_cachcing_date"))
                        .from(PAYMENT)
                        .fetch()
        );
    }

    // EXAMPLE 6
    /*
    select 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_LINE", 
      count(*) 
    from 
      "CLASSICMODELS"."PRODUCT" 
    group by 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_LINE" 
    having 
      (
        count(*) + ?
      ) > all (
        select 
          count(
            distinct "CLASSICMODELS"."PRODUCT"."PRODUCT_ID"
          ) 
        from 
          "CLASSICMODELS"."PRODUCT" 
        group by 
          "CLASSICMODELS"."PRODUCT"."PRODUCT_LINE"
      )  
    */
    public void findProductLineHavingMaxNrOfProducts() {

        System.out.println("EXAMPLE 6\n" +
                ctx.select(PRODUCT.PRODUCT_LINE, count())
                        .from(PRODUCT)
                        .groupBy(PRODUCT.PRODUCT_LINE)
                        .having(count().plus(1)
                                .gt(all(select(countDistinct(PRODUCT))
                                        .from(PRODUCT)
                                        .groupBy(PRODUCT.PRODUCT_LINE))))
                        .fetch()
        );
    }   

    // EXAMPLE 7
    /*
    select 
      avg(
        "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH"
      ), 
      avg(
        distinct "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH"
      ), 
      sum(
        "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH"
      ), 
      sum(
        distinct "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH"
      ), 
      min(
        "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH"
      ), 
      min(
        distinct "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH"
      ), 
      max(
        "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH"
      ), 
      max(
        distinct "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH"
      ) 
    from 
      "CLASSICMODELS"."ORDERDETAIL"    
    */
    public void avgSumMinMaxPriceEach() {

        System.out.println("EXAMPLE 7\n" +
                ctx.select(
                        avg(ORDERDETAIL.PRICE_EACH),
                        avgDistinct(ORDERDETAIL.PRICE_EACH),
                        sum(ORDERDETAIL.PRICE_EACH),
                        sumDistinct(ORDERDETAIL.PRICE_EACH),
                        min(ORDERDETAIL.PRICE_EACH),
                        minDistinct(ORDERDETAIL.PRICE_EACH),
                        max(ORDERDETAIL.PRICE_EACH),
                        maxDistinct(ORDERDETAIL.PRICE_EACH)
                ).from(ORDERDETAIL)
                        .fetch()
        );
    }

    // EXAMPLE 8
    /*
    select 
      distinct count(*) over () "sales" 
    from 
      "CLASSICMODELS"."SALE" 
    group by 
      "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER"    
    */
    public void countDistinctSalesByEmployeeNumber() {

        System.out.println("EXAMPLE 8 (count result): "
                + ctx.selectDistinct(count().over().as("sales"))
                        .from(SALE)
                        .groupBy(SALE.EMPLOYEE_NUMBER)
                        .fetchOneInto(int.class)
        );
    }
    
    /* PostgreSQL DISTINCT ON */
    // EXAMPLE 9
    /* The following statement sorts the result set by the product's vendor and scale, 
       and then for each group of duplicates, it keeps the first row in the returned result set */    
    /*
    select 
      "t"."PRODUCT_VENDOR", 
      "t"."PRODUCT_SCALE" 
    from 
      (
        select 
          distinct "CLASSICMODELS"."PRODUCT"."PRODUCT_VENDOR", 
          "CLASSICMODELS"."PRODUCT"."PRODUCT_SCALE", 
          row_number() over (
            partition by "CLASSICMODELS"."PRODUCT"."PRODUCT_VENDOR", 
            "CLASSICMODELS"."PRODUCT"."PRODUCT_SCALE" 
            order by 
              "CLASSICMODELS"."PRODUCT"."PRODUCT_VENDOR", 
              "CLASSICMODELS"."PRODUCT"."PRODUCT_SCALE"
          ) "rn" 
        from 
          "CLASSICMODELS"."PRODUCT"
      ) "t" 
    where 
      "rn" = 1 
    order by 
      "PRODUCT_VENDOR", 
      "PRODUCT_SCALE"    
    */
    public void findProductsByVendorScale() {

        System.out.println("EXAMPLE 9\n" +
                ctx.selectDistinct(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE)
                        .on(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE)
                        .from(PRODUCT)         
                        .orderBy(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE)
                        .fetch()
        );

        /* or, like this */        
        /*
        System.out.println("EXAMPLE 9\n" +
                ctx.select(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE)
                        .distinctOn(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE)
                        .from(PRODUCT) 
                        .orderBy(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE)
                        .fetch()
        );
        */        
    }
    
    // EXAMPLE 10
    /* What is the employee numbers of the max sales per fiscal years */        
    public void findEmployeeNumberOfMaxSalePerFiscalYear() {

        /*
        select 
          "t"."EMPLOYEE_NUMBER", 
          "t"."FISCAL_YEAR", 
          "t"."SALE" 
        from 
          (
            select 
              "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER", 
              "CLASSICMODELS"."SALE"."FISCAL_YEAR", 
              "CLASSICMODELS"."SALE"."SALE", 
              row_number() over (
                partition by "CLASSICMODELS"."SALE"."FISCAL_YEAR" 
                order by 
                  "CLASSICMODELS"."SALE"."FISCAL_YEAR", 
                  "CLASSICMODELS"."SALE"."SALE" desc
              ) "rn" 
            from 
              "CLASSICMODELS"."SALE"
          ) "t" 
        where 
          "rn" = 1 
        order by 
          "FISCAL_YEAR", 
          "SALE" desc        
        */
        System.out.println("EXAMPLE 10.1\n" +
                ctx.select(
                        SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_)
                        .distinctOn(SALE.FISCAL_YEAR)
                        .from(SALE)
                        .orderBy(SALE.FISCAL_YEAR, SALE.SALE_.desc())
                        .fetch()
        );

        /* SQL alternative based on JOIN */
        /*
        select 
          "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER", 
          "CLASSICMODELS"."SALE"."FISCAL_YEAR", 
          "CLASSICMODELS"."SALE"."SALE" 
        from 
          "CLASSICMODELS"."SALE" 
          join (
            select 
              "CLASSICMODELS"."SALE"."FISCAL_YEAR" "fy", 
              max("CLASSICMODELS"."SALE"."SALE") "ms" 
            from 
              "CLASSICMODELS"."SALE" 
            group by 
              "CLASSICMODELS"."SALE"."FISCAL_YEAR"
          ) "alias_127759043" on (
            "CLASSICMODELS"."SALE"."FISCAL_YEAR" = "fy" 
            and "CLASSICMODELS"."SALE"."SALE" = "ms"
          ) 
        order by 
          "CLASSICMODELS"."SALE"."FISCAL_YEAR", 
          "CLASSICMODELS"."SALE"."SALE" desc        
        */  
        System.out.println("EXAMPLE 10.2\n" + 
                ctx.select(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_)
                        .from(SALE)
                        .innerJoin(select(SALE.FISCAL_YEAR.as(name("fy")), max(SALE.SALE_).as(name("ms")))
                                .from(SALE)
                                .groupBy(SALE.FISCAL_YEAR))
                        .on(SALE.FISCAL_YEAR.eq(field(name("fy"), Integer.class))
                                .and(SALE.SALE_.eq(field(name("ms"), Double.class))))
                        .orderBy(SALE.FISCAL_YEAR, SALE.SALE_.desc())
                        .fetch()
        );        
        
        /* SQL alternative based on row_number() */        
        /*
        select 
          "t"."employee_number", 
          "t"."fiscal_year", 
          "t"."sale" 
        from 
          (
            select 
              distinct "CLASSICMODELS"."SALE"."FISCAL_YEAR" "fiscal_year", 
              "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER" "employee_number", 
              "CLASSICMODELS"."SALE"."SALE" "sale", 
              row_number() over (
                partition by "CLASSICMODELS"."SALE"."FISCAL_YEAR" 
                order by 
                  "CLASSICMODELS"."SALE"."FISCAL_YEAR", 
                  "CLASSICMODELS"."SALE"."SALE" desc
              ) "rn" 
            from 
              "CLASSICMODELS"."SALE"
          ) "t" 
        where 
          "t"."rn" = ? 
        order by 
          "t"."fiscal_year"        
        */
        // Table<?>
        var t = selectDistinct(SALE.FISCAL_YEAR.as(name("fiscal_year")), 
                SALE.EMPLOYEE_NUMBER.as(name("employee_number")),
                SALE.SALE_.as(name("sale")),
                                rowNumber().over(partitionBy(SALE.FISCAL_YEAR)
                                        .orderBy(SALE.FISCAL_YEAR, SALE.SALE_.desc())).as(name("rn")))
                                        .from(SALE).asTable("t");
        
        System.out.println("EXAMPLE 10.3\n" + 
                ctx.select(t.field(name("employee_number")),
                         t.field(name("fiscal_year")),                         
                         t.field(name("sale")))
                        .from(t)
                        .where(t.field(name("rn"), Integer.class).eq(1))
                        .orderBy(t.field(name("fiscal_year")))
                        .fetch()                                                                
        );
        
        /* SQL alternative based on row_number() and QUALIFY */
        /*
        select 
          distinct "t"."EMPLOYEE_NUMBER", 
          "t"."FISCAL_YEAR", 
          "t"."SALE" 
        from 
          (
            select 
              "CLASSICMODELS"."SALE"."SALE_ID", 
              "CLASSICMODELS"."SALE"."FISCAL_YEAR", 
              "CLASSICMODELS"."SALE"."SALE", 
              "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER", 
              "CLASSICMODELS"."SALE"."HOT", 
              "CLASSICMODELS"."SALE"."RATE", 
              "CLASSICMODELS"."SALE"."VAT", 
              "CLASSICMODELS"."SALE"."FISCAL_MONTH", 
              "CLASSICMODELS"."SALE"."REVENUE_GROWTH", 
              "CLASSICMODELS"."SALE"."TREND", 
              case when row_number() over (
                partition by "CLASSICMODELS"."SALE"."FISCAL_YEAR" 
                order by 
                  "CLASSICMODELS"."SALE"."FISCAL_YEAR", 
                  "CLASSICMODELS"."SALE"."SALE" desc
              ) = ? then 1 when not (
                row_number() over (
                  partition by "CLASSICMODELS"."SALE"."FISCAL_YEAR" 
                  order by 
                    "CLASSICMODELS"."SALE"."FISCAL_YEAR", 
                    "CLASSICMODELS"."SALE"."SALE" desc
                ) = ?
              ) then 0 end "w0" 
            from 
              "CLASSICMODELS"."SALE"
          ) "t" 
        where 
          "w0" = 1 
        order by 
          "t"."FISCAL_YEAR"        
        */
        System.out.println("EXAMPLE 10.4\n"
                + ctx.selectDistinct(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_)
                        .from(SALE)
                        .qualify(rowNumber().over(partitionBy(SALE.FISCAL_YEAR)
                                .orderBy(SALE.FISCAL_YEAR, SALE.SALE_.desc())).eq(1))
                        .orderBy(SALE.FISCAL_YEAR)
                        .fetch()
        );
    }        
    
     // EXAMPLE 11    
    /* What is the distinct employee numbers ordered by min sales */
    /*
    select 
      "T"."EMPLOYEE_NUMBER" 
    from 
      (
        select 
          "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER", 
          min("CLASSICMODELS"."SALE"."SALE") "SALE" 
        from 
          "CLASSICMODELS"."SALE" 
        group by 
          "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER"
      ) "T" 
    order by 
      "T"."SALE"    
    */
    public void findDistinctEmployeeNumberOrderByMinSale() {
        
        System.out.println("EXAMPLE 11\n"
                + ctx.select(field(name("T", "EMPLOYEE_NUMBER")))
                        .from(select(SALE.EMPLOYEE_NUMBER, min(SALE.SALE_).as("SALE"))
                                .from(SALE)
                                .groupBy(SALE.EMPLOYEE_NUMBER).asTable("T"))
                        .orderBy(field(name("T", "SALE")))
                        .fetch()
        );
    }
}