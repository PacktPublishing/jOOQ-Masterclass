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
      distinct [classicmodels].[dbo].[office].[city], 
      [classicmodels].[dbo].[office].[country] 
    from 
      [classicmodels].[dbo].[office] 
    where 
      (
        [classicmodels].[dbo].[office].[city] is not null 
        and [classicmodels].[dbo].[office].[country] is not null
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
      [classicmodels].[dbo].[office].[office_code], 
      [classicmodels].[dbo].[office].[city], 
      [classicmodels].[dbo].[office].[phone], 
      [classicmodels].[dbo].[office].[address_line_first], 
      [classicmodels].[dbo].[office].[address_line_second], 
      [classicmodels].[dbo].[office].[state], 
      [classicmodels].[dbo].[office].[country], 
      [classicmodels].[dbo].[office].[postal_code], 
      [classicmodels].[dbo].[office].[territory] 
    from 
      [classicmodels].[dbo].[office] 
    where 
      not (
        exists (
          select 
            [classicmodels].[dbo].[office].[address_line_second] [x] 
          intersect 
          select 
            ? [x]
        )
      )    
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
      [classicmodels].[dbo].[payment].[invoice_amount], 
      [classicmodels].[dbo].[payment].[payment_date] 
    from 
      [classicmodels].[dbo].[payment] 
    where 
      not (
        exists (
          select 
            cast(
              [classicmodels].[dbo].[payment].[payment_date] as date
            ) [x] 
          intersect 
          select 
            cast(
              [classicmodels].[dbo].[payment].[caching_date] as date
            ) [x]
        )
      )    
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
      [classicmodels].[dbo].[office].[office_code], 
      [classicmodels].[dbo].[office].[city], 
      [classicmodels].[dbo].[office].[phone], 
      [classicmodels].[dbo].[office].[address_line_first], 
      [classicmodels].[dbo].[office].[address_line_second], 
      [classicmodels].[dbo].[office].[state], 
      [classicmodels].[dbo].[office].[country], 
      [classicmodels].[dbo].[office].[postal_code], 
      [classicmodels].[dbo].[office].[territory], 
      [classicmodels].[dbo].[customerdetail].[customer_number], 
      [classicmodels].[dbo].[customerdetail].[address_line_first], 
      [classicmodels].[dbo].[customerdetail].[address_line_second], 
      [classicmodels].[dbo].[customerdetail].[city], 
      [classicmodels].[dbo].[customerdetail].[state], 
      [classicmodels].[dbo].[customerdetail].[postal_code], 
      [classicmodels].[dbo].[customerdetail].[country] 
    from 
      [classicmodels].[dbo].[office] 
      join [classicmodels].[dbo].[customerdetail] on [classicmodels].[dbo].[office].[postal_code] = [classicmodels].[dbo].[customerdetail].[postal_code] 
    where 
      not (
        exists (
          select 
            [classicmodels].[dbo].[office].[city], 
            [classicmodels].[dbo].[office].[country] 
          intersect 
          select 
            [classicmodels].[dbo].[customerdetail].[city], 
            [classicmodels].[dbo].[customerdetail].[country]
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
      count(*) [all], 
      count(
        [classicmodels].[dbo].[payment].[caching_date]
      ) [all_caching_date], 
      count(
        distinct [classicmodels].[dbo].[payment].[caching_date]
      ) [distinct_cachcing_date] 
    from 
      [classicmodels].[dbo].[payment]    
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
      [classicmodels].[dbo].[product].[product_line], 
      count(*) 
    from 
      [classicmodels].[dbo].[product] 
    group by 
      [classicmodels].[dbo].[product].[product_line] 
    having 
      (
        count(*) + ?
      ) > all (
        select 
          count(
            distinct [classicmodels].[dbo].[product].[product_id]
          ) 
        from 
          [classicmodels].[dbo].[product] 
        group by 
          [classicmodels].[dbo].[product].[product_line]
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
        [classicmodels].[dbo].[orderdetail].[price_each]
      ), 
      avg(
        distinct [classicmodels].[dbo].[orderdetail].[price_each]
      ), 
      sum(
        [classicmodels].[dbo].[orderdetail].[price_each]
      ), 
      sum(
        distinct [classicmodels].[dbo].[orderdetail].[price_each]
      ), 
      min(
        [classicmodels].[dbo].[orderdetail].[price_each]
      ), 
      min(
        distinct [classicmodels].[dbo].[orderdetail].[price_each]
      ), 
      max(
        [classicmodels].[dbo].[orderdetail].[price_each]
      ), 
      max(
        distinct [classicmodels].[dbo].[orderdetail].[price_each]
      ) 
    from 
      [classicmodels].[dbo].[orderdetail]    
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
    /* Emulating PostgreSQL DISTINCT ON */    
    /* Sample: What is the employee numbers of the max sales per fiscal years 
    ctx.select(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR)
         .distinctOn(SALE.FISCAL_YEAR)
         .from(SALE)
         .orderBy(SALE.FISCAL_YEAR, SALE.SALE_.desc())
         .fetch()
    */            
    public void findEmployeeNumberOfMaxSalePerFiscalYear() {
    
        /* SQL alternative based on JOIN */
        /*
        select 
          [classicmodels].[dbo].[sale].[fiscal_year], 
          [classicmodels].[dbo].[sale].[employee_number] 
        from 
          [classicmodels].[dbo].[sale] 
          join (
            select 
              [classicmodels].[dbo].[sale].[fiscal_year] [fy], 
              max(
                [classicmodels].[dbo].[sale].[sale]
              ) [ms] 
            from 
              [classicmodels].[dbo].[sale] 
            group by 
              [classicmodels].[dbo].[sale].[fiscal_year]
          ) [alias_39737657] on (
            [classicmodels].[dbo].[sale].[fiscal_year] = fy 
            and [classicmodels].[dbo].[sale].[sale] = ms
          ) 
        order by 
          [classicmodels].[dbo].[sale].[fiscal_year], 
          [classicmodels].[dbo].[sale].[sale] desc        
        */  
        System.out.println("EXAMPLE 8.1\n" + 
                ctx.select(SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER)
                        .from(SALE)
                        .innerJoin(select(SALE.FISCAL_YEAR.as("fy"), max(SALE.SALE_).as("ms"))
                                .from(SALE)
                                .groupBy(SALE.FISCAL_YEAR))
                        .on(SALE.FISCAL_YEAR.eq(field("fy", Integer.class))
                                .and(SALE.SALE_.eq(field("ms", Double.class))))
                        .orderBy(SALE.FISCAL_YEAR, SALE.SALE_.desc())
                        .fetch()
        );        
        
        /* SQL alternative based on row_number() */        
        /*
        select 
          [alias_37493236].[fiscal_year], 
          [alias_37493236].[employee_number] 
        from 
          (
            select 
              distinct [classicmodels].[dbo].[sale].[fiscal_year], 
              [classicmodels].[dbo].[sale].[employee_number], 
              row_number() over (
                partition by [classicmodels].[dbo].[sale].[fiscal_year] 
                order by 
                  [classicmodels].[dbo].[sale].[fiscal_year], 
                  [classicmodels].[dbo].[sale].[sale] desc
              ) [rn] 
            from 
              [classicmodels].[dbo].[sale]
          ) [alias_37493236] 
        where 
          [alias_37493236].[rn] = ? 
        order by 
          [alias_37493236].[fiscal_year]        
        */
        // Table<?>
        var t = selectDistinct(SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER,
                                rowNumber().over(partitionBy(SALE.FISCAL_YEAR)
                                        .orderBy(SALE.FISCAL_YEAR, SALE.SALE_.desc())).as("rn"))
                                        .from(SALE).asTable();
        
        System.out.println("EXAMPLE 8.2\n" + 
                ctx.select(t.field("fiscal_year"), t.field("employee_number"))
                        .from(t)
                        .where(t.field("rn", Integer.class).eq(1))
                        .orderBy(t.field("fiscal_year"))
                        .fetch()                                                                
        );
    }    
}