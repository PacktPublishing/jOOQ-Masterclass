package com.classicmodels.repository;

import java.math.BigInteger;
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
      distinct "SYSTEM"."OFFICE"."CITY", 
      "SYSTEM"."OFFICE"."COUNTRY" 
    from 
      "SYSTEM"."OFFICE" 
    where 
      (
        "SYSTEM"."OFFICE"."CITY" is not null 
        and "SYSTEM"."OFFICE"."COUNTRY" is not null
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
      "SYSTEM"."OFFICE"."OFFICE_CODE", 
      "SYSTEM"."OFFICE"."CITY", 
      "SYSTEM"."OFFICE"."PHONE", 
      "SYSTEM"."OFFICE"."ADDRESS_LINE_FIRST", 
      "SYSTEM"."OFFICE"."ADDRESS_LINE_SECOND", 
      "SYSTEM"."OFFICE"."STATE", 
      "SYSTEM"."OFFICE"."COUNTRY", 
      "SYSTEM"."OFFICE"."POSTAL_CODE", 
      "SYSTEM"."OFFICE"."TERRITORY" 
    from 
      "SYSTEM"."OFFICE" 
    where 
      decode(
        "SYSTEM"."OFFICE"."ADDRESS_LINE_SECOND", 
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
      "SYSTEM"."PAYMENT"."INVOICE_AMOUNT", 
      "SYSTEM"."PAYMENT"."PAYMENT_DATE" 
    from 
      "SYSTEM"."PAYMENT" 
    where 
      decode(
        cast(
          "SYSTEM"."PAYMENT"."PAYMENT_DATE" as date
        ), 
        cast(
          "SYSTEM"."PAYMENT"."CACHING_DATE" as date
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
      "SYSTEM"."OFFICE"."OFFICE_CODE", 
      "SYSTEM"."OFFICE"."CITY", 
      "SYSTEM"."OFFICE"."PHONE", 
      "SYSTEM"."OFFICE"."ADDRESS_LINE_FIRST", 
      "SYSTEM"."OFFICE"."ADDRESS_LINE_SECOND", 
      "SYSTEM"."OFFICE"."STATE", 
      "SYSTEM"."OFFICE"."COUNTRY", 
      "SYSTEM"."OFFICE"."POSTAL_CODE", 
      "SYSTEM"."OFFICE"."TERRITORY", 
      "SYSTEM"."CUSTOMERDETAIL"."CUSTOMER_NUMBER", 
      "SYSTEM"."CUSTOMERDETAIL"."ADDRESS_LINE_FIRST", 
      "SYSTEM"."CUSTOMERDETAIL"."ADDRESS_LINE_SECOND", 
      "SYSTEM"."CUSTOMERDETAIL"."CITY", 
      "SYSTEM"."CUSTOMERDETAIL"."STATE", 
      "SYSTEM"."CUSTOMERDETAIL"."POSTAL_CODE", 
      "SYSTEM"."CUSTOMERDETAIL"."COUNTRY" 
    from 
      "SYSTEM"."OFFICE" 
      join "SYSTEM"."CUSTOMERDETAIL" on "SYSTEM"."OFFICE"."POSTAL_CODE" 
         = "SYSTEM"."CUSTOMERDETAIL"."POSTAL_CODE" 
    where 
      not (
        exists (
          select 
            "SYSTEM"."OFFICE"."CITY", 
            "SYSTEM"."OFFICE"."COUNTRY" 
          from 
            dual 
          intersect 
          select 
            "SYSTEM"."CUSTOMERDETAIL"."CITY", 
            "SYSTEM"."CUSTOMERDETAIL"."COUNTRY" 
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
        "SYSTEM"."PAYMENT"."CACHING_DATE"
      ) "all_caching_date", 
      count(
        distinct "SYSTEM"."PAYMENT"."CACHING_DATE"
      ) "distinct_cachcing_date" 
    from 
      "SYSTEM"."PAYMENT"    
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
      "SYSTEM"."PRODUCT"."PRODUCT_LINE", 
      count(*) 
    from 
      "SYSTEM"."PRODUCT" 
    group by 
      "SYSTEM"."PRODUCT"."PRODUCT_LINE" 
    having 
      (
        count(*) + ?
      ) > all (
        select 
          count(
            distinct "SYSTEM"."PRODUCT"."PRODUCT_ID"
          ) 
        from 
          "SYSTEM"."PRODUCT" 
        group by 
          "SYSTEM"."PRODUCT"."PRODUCT_LINE"
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
        "SYSTEM"."ORDERDETAIL"."PRICE_EACH"
      ), 
      avg(
        distinct "SYSTEM"."ORDERDETAIL"."PRICE_EACH"
      ), 
      sum(
        "SYSTEM"."ORDERDETAIL"."PRICE_EACH"
      ), 
      sum(
        distinct "SYSTEM"."ORDERDETAIL"."PRICE_EACH"
      ), 
      min(
        "SYSTEM"."ORDERDETAIL"."PRICE_EACH"
      ), 
      min(
        distinct "SYSTEM"."ORDERDETAIL"."PRICE_EACH"
      ), 
      max(
        "SYSTEM"."ORDERDETAIL"."PRICE_EACH"
      ), 
      max(
        distinct "SYSTEM"."ORDERDETAIL"."PRICE_EACH"
      ) 
    from 
      "SYSTEM"."ORDERDETAIL"    
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

    /* PostgreSQL DISTINCT ON */
    // EXAMPLE 8
    /* The following statement sorts the result set by the product's vendor and scale, 
       and then for each group of duplicates, it keeps the first row in the returned result set */    
    /*
    select 
      "t"."PRODUCT_VENDOR", 
      "t"."PRODUCT_SCALE" 
    from 
      (
        select 
          distinct "SYSTEM"."PRODUCT"."PRODUCT_VENDOR", 
          "SYSTEM"."PRODUCT"."PRODUCT_SCALE", 
          row_number() over (
            partition by "SYSTEM"."PRODUCT"."PRODUCT_VENDOR", 
            "SYSTEM"."PRODUCT"."PRODUCT_SCALE" 
            order by 
              "SYSTEM"."PRODUCT"."PRODUCT_VENDOR", 
              "SYSTEM"."PRODUCT"."PRODUCT_SCALE"
          ) "rn" 
        from 
          "SYSTEM"."PRODUCT"
      ) "t" 
    where 
      "rn" = 1 
    order by 
      "PRODUCT_VENDOR", 
      "PRODUCT_SCALE"    
    */
    public void findProductsByVendorScale() {

        System.out.println("EXAMPLE 8\n" +
                ctx.selectDistinct(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE)
                        .on(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE)
                        .from(PRODUCT)         
                        .orderBy(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE)
                        .fetch()
        );

        /* or, like this */        
        /*
        System.out.println("EXAMPLE 8\n" +
                ctx.select(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE)
                        .distinctOn(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE)
                        .from(PRODUCT) 
                        .orderBy(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE)
                        .fetch()
        );
        */        
    }
    
    // EXAMPLE 9
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
              "SYSTEM"."SALE"."EMPLOYEE_NUMBER", 
              "SYSTEM"."SALE"."FISCAL_YEAR", 
              "SYSTEM"."SALE"."SALE", 
              row_number() over (
                partition by "SYSTEM"."SALE"."FISCAL_YEAR" 
                order by 
                  "SYSTEM"."SALE"."FISCAL_YEAR", 
                  "SYSTEM"."SALE"."SALE" desc
              ) "rn" 
            from 
              "SYSTEM"."SALE"
          ) "t" 
        where 
          "rn" = 1 
        order by 
          "FISCAL_YEAR", 
          "SALE" desc        
        */
        System.out.println("EXAMPLE 9.1\n" +
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
          "SYSTEM"."SALE"."FISCAL_YEAR", 
          "SYSTEM"."SALE"."EMPLOYEE_NUMBER" 
        from 
          "SYSTEM"."SALE" 
          join (
            select 
              "SYSTEM"."SALE"."FISCAL_YEAR" "fy", 
              max("SYSTEM"."SALE"."SALE") "ms" 
            from 
              "SYSTEM"."SALE" 
            group by 
              "SYSTEM"."SALE"."FISCAL_YEAR"
          ) "alias_127759043" on (
            "SYSTEM"."SALE"."FISCAL_YEAR" = "fy" 
            and "SYSTEM"."SALE"."SALE" = "ms"
          ) 
        order by 
          "SYSTEM"."SALE"."FISCAL_YEAR", 
          "SYSTEM"."SALE"."SALE" desc        
        */  
        System.out.println("EXAMPLE 9.2\n" + 
                ctx.select(SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER)
                        .from(SALE)
                        .innerJoin(select(SALE.FISCAL_YEAR.as(name("fy")), max(SALE.SALE_).as(name("ms")))
                                .from(SALE)
                                .groupBy(SALE.FISCAL_YEAR))
                        .on(SALE.FISCAL_YEAR.eq(field(name("fy"), BigInteger.class))
                                .and(SALE.SALE_.eq(field(name("ms"), Double.class))))
                        .orderBy(SALE.FISCAL_YEAR, SALE.SALE_.desc())
                        .fetch()
        );        
        
        /* SQL alternative based on row_number() */        
        /*
        select 
          "alias_35975566"."fiscal_year", 
          "alias_35975566"."employee_number" 
        from 
          (
            select 
              distinct "SYSTEM"."SALE"."FISCAL_YEAR" "fiscal_year", 
              "SYSTEM"."SALE"."EMPLOYEE_NUMBER" "employee_number", 
              row_number() over (
                partition by "SYSTEM"."SALE"."FISCAL_YEAR" 
                order by 
                  "SYSTEM"."SALE"."FISCAL_YEAR"
              ) "rn" 
            from 
              "SYSTEM"."SALE"
          ) "alias_35975566" 
        where 
          "alias_35975566"."rn" = ? 
        order by 
          "alias_35975566"."fiscal_year"        
        */
        // Table<?>
        var t = selectDistinct(SALE.FISCAL_YEAR.as(name("fiscal_year")), 
                SALE.EMPLOYEE_NUMBER.as(name("employee_number")),
                                rowNumber().over(partitionBy(SALE.FISCAL_YEAR)
                                        .orderBy(SALE.FISCAL_YEAR)).as(name("rn")))
                                        .from(SALE).asTable();
        
        System.out.println("EXAMPLE 9.3\n" + 
                ctx.select(t.field(name("fiscal_year")), t.field(name("employee_number")))
                        .from(t)
                        .where(t.field(name("rn"), Integer.class).eq(1))
                        .orderBy(t.field(name("fiscal_year")))
                        .fetch()                                                                
        );
    }
    
    // EXAMPLE 10
    /*
    select 
      distinct count(*) over () "sales" 
    from 
      "SYSTEM"."SALE" 
    group by 
      "SYSTEM"."SALE"."EMPLOYEE_NUMBER"    
    */
    public void countDistinctSalesByEmployeeNumber() {

        System.out.println("EXAMPLE 10 (count result): "
                + ctx.selectDistinct(count().over().as("sales"))
                        .from(SALE)
                        .groupBy(SALE.EMPLOYEE_NUMBER)
                        .fetchOneInto(int.class)
        );
    }
}