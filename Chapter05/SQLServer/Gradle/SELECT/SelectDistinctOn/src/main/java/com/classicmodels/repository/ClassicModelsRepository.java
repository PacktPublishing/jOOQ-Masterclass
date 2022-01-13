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

        System.out.println("EXAMPLE 3.1\n"
                + ctx.select(PAYMENT.INVOICE_AMOUNT, PAYMENT.PAYMENT_DATE)
                        .from(PAYMENT)
                        .where(PAYMENT.PAYMENT_DATE.cast(LocalDate.class).isDistinctFrom(
                                PAYMENT.CACHING_DATE.cast(LocalDate.class)))
                        .fetch()
        );

        System.out.println("EXAMPLE 3.2\n"
                + ctx.select(PAYMENT.INVOICE_AMOUNT, PAYMENT.PAYMENT_DATE)
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

        System.out.println("EXAMPLE 4\n"
                + ctx.select()
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

        System.out.println("EXAMPLE 5\n"
                + ctx.select(
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

        System.out.println("EXAMPLE 6\n"
                + ctx.select(PRODUCT.PRODUCT_LINE, count())
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

        System.out.println("EXAMPLE 7\n"
                + ctx.select(
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
      distinct count(*) over () [sales] 
    from 
      [classicmodels].[dbo].[sale] 
    group by 
      [classicmodels].[dbo].[sale].[employee_number]    
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
      [t].[product_vendor], 
      [t].[product_scale] 
    from 
      (
        select 
          distinct [classicmodels].[dbo].[product].[product_vendor], 
          [classicmodels].[dbo].[product].[product_scale], 
          row_number() over (
            partition by [classicmodels].[dbo].[product].[product_vendor], 
            [classicmodels].[dbo].[product].[product_scale] 
            order by 
              [classicmodels].[dbo].[product].[product_vendor], 
              [classicmodels].[dbo].[product].[product_scale]
          ) [rn] 
        from 
          [classicmodels].[dbo].[product]
      ) [t] 
    where 
      [rn] = 1 
    order by 
      [product_vendor], 
      [product_scale]    
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
          [t].[employee_number], 
          [t].[fiscal_year], 
          [t].[sale] 
        from 
          (
            select 
              [classicmodels].[dbo].[sale].[employee_number], 
              [classicmodels].[dbo].[sale].[fiscal_year], 
              [classicmodels].[dbo].[sale].[sale], 
              row_number() over (
                partition by [classicmodels].[dbo].[sale].[fiscal_year] 
                order by 
                  [classicmodels].[dbo].[sale].[fiscal_year], 
                  [classicmodels].[dbo].[sale].[sale] desc
              ) [rn] 
            from 
              [classicmodels].[dbo].[sale]
          ) [t] 
        where 
          [rn] = 1 
        order by 
          [fiscal_year], 
          [sale] desc     
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
          [classicmodels].[dbo].[sale].[employee_number], 
          [classicmodels].[dbo].[sale].[fiscal_year], 
          [classicmodels].[dbo].[sale].[sale] 
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
        System.out.println("EXAMPLE 10.2\n"
                + ctx.select(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_)
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
          [t].[fiscal_year], 
          [t].[employee_number], 
          [t].[sale] 
        from 
          (
            select 
              distinct [classicmodels].[dbo].[sale].[employee_number], 
              [classicmodels].[dbo].[sale].[fiscal_year], 
              [classicmodels].[dbo].[sale].[sale], 
              row_number() over (
                partition by [classicmodels].[dbo].[sale].[fiscal_year] 
                order by 
                  [classicmodels].[dbo].[sale].[fiscal_year], 
                  [classicmodels].[dbo].[sale].[sale] desc
              ) [rn] 
            from 
              [classicmodels].[dbo].[sale]
          ) [t] 
        where 
          [t].[rn] = ? 
        order by 
          [t].[fiscal_year]              
         */
        // Table<?>
        var t = selectDistinct(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_,
                rowNumber().over(partitionBy(SALE.FISCAL_YEAR)
                        .orderBy(SALE.FISCAL_YEAR, SALE.SALE_.desc())).as("rn"))
                .from(SALE).asTable("t");

        System.out.println("EXAMPLE 10.3\n"
                + ctx.select(t.field("fiscal_year"), t.field("employee_number"), t.field("sale"))
                        .from(t)
                        .where(t.field("rn", Integer.class).eq(1))
                        .orderBy(t.field("fiscal_year"))
                        .fetch()
        );
        
        /* SQL alternative based on row_number() and QUALIFY */
        /*
        select 
          distinct [t].[employee_number], 
          [t].[fiscal_year], 
          [t].[sale] 
        from 
          (
            select 
              *, 
              case when row_number() over (
                partition by [classicmodels].[dbo].[sale].[fiscal_year] 
                order by 
                  [classicmodels].[dbo].[sale].[fiscal_year], 
                  [classicmodels].[dbo].[sale].[sale] desc
              ) = ? then 1 when not (
                row_number() over (
                  partition by [classicmodels].[dbo].[sale].[fiscal_year] 
                  order by 
                    [classicmodels].[dbo].[sale].[fiscal_year], 
                    [classicmodels].[dbo].[sale].[sale] desc
                ) = ?
              ) then 0 end [w0] 
            from 
              [classicmodels].[dbo].[sale]
          ) [t] 
        where 
          [w0] = 1 
        order by 
          [t].[fiscal_year]        
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
      [t].[employee_number]
    from 
      (
        select 
          [classicmodels].[dbo].[sale].[employee_number], 
          min(
            [classicmodels].[dbo].[sale].[sale]
          ) [sale] 
        from 
          [classicmodels].[dbo].[sale] 
        group by 
          [classicmodels].[dbo].[sale].[employee_number]
      ) [t] 
    order by 
      [t].[sale]    
    */
    public void findDistinctEmployeeNumberOrderByMinSale() {
        
        System.out.println("EXAMPLE 11\n"
                + ctx.select(field(name("t", "employee_number")))
                        .from(select(SALE.EMPLOYEE_NUMBER, min(SALE.SALE_).as("sale"))
                                .from(SALE)
                                .groupBy(SALE.EMPLOYEE_NUMBER).asTable("t"))
                        .orderBy(field(name("t", "sale")))
                        .fetch()
        );
    }
}