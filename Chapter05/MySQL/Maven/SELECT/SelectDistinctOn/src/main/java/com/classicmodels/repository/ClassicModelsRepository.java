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
import static org.jooq.impl.DSL.groupConcat;
import static org.jooq.impl.DSL.groupConcatDistinct;
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
      distinct `classicmodels`.`office`.`city`, 
      `classicmodels`.`office`.`country` 
    from 
      `classicmodels`.`office` 
    where 
      (
        `classicmodels`.`office`.`city` is not null 
        and `classicmodels`.`office`.`country` is not null
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
      `classicmodels`.`office`.`office_code`, 
      `classicmodels`.`office`.`city`, 
      `classicmodels`.`office`.`phone`, 
      `classicmodels`.`office`.`address_line_first`, 
      `classicmodels`.`office`.`address_line_second`, 
      `classicmodels`.`office`.`state`, 
      `classicmodels`.`office`.`country`, 
      `classicmodels`.`office`.`postal_code`, 
      `classicmodels`.`office`.`territory` 
    from 
      `classicmodels`.`office` 
    where 
      (
        not(
          `classicmodels`.`office`.`address_line_second` <=> ?
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
      `classicmodels`.`payment`.`invoice_amount`, 
      `classicmodels`.`payment`.`payment_date` 
    from 
      `classicmodels`.`payment` 
    where 
      (
        not(
          cast(
            `classicmodels`.`payment`.`payment_date` as date
          ) <=> cast(
            `classicmodels`.`payment`.`caching_date` as date
          )
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
      `classicmodels`.`office`.`office_code`, 
      `classicmodels`.`office`.`city`, 
      `classicmodels`.`office`.`phone`, 
      `classicmodels`.`office`.`address_line_first`, 
      `classicmodels`.`office`.`address_line_second`, 
      `classicmodels`.`office`.`state`, 
      `classicmodels`.`office`.`country`, 
      `classicmodels`.`office`.`postal_code`, 
      `classicmodels`.`office`.`territory`, 
      `classicmodels`.`customerdetail`.`customer_number`, 
      `classicmodels`.`customerdetail`.`address_line_first`, 
      `classicmodels`.`customerdetail`.`address_line_second`, 
      `classicmodels`.`customerdetail`.`city`, 
      `classicmodels`.`customerdetail`.`state`, 
      `classicmodels`.`customerdetail`.`postal_code`, 
      `classicmodels`.`customerdetail`.`country` 
    from 
      `classicmodels`.`office` 
      join `classicmodels`.`customerdetail` on `classicmodels`.`office`.`postal_code` = `classicmodels`.`customerdetail`.`postal_code` 
    where 
      (
        not(
          (
            `classicmodels`.`office`.`city`, 
            `classicmodels`.`office`.`country`
          ) <=> (
            `classicmodels`.`customerdetail`.`city`, 
            `classicmodels`.`customerdetail`.`country`
          )
        )
      )
     */
    public void findOfficeAndCustomerOfficePostalCodeDistinctCityCountry() {

        System.out.println("EXAMPLE 4 \n"
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
      count(*) as `all`, 
      count(
        `classicmodels`.`payment`.`caching_date`
      ) as `all_caching_date`, 
      count(
        distinct `classicmodels`.`payment`.`caching_date`
      ) as `distinct_cachcing_date` 
    from 
      `classicmodels`.`payment`
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
      `classicmodels`.`product`.`product_line`, 
      count(*) 
    from 
      `classicmodels`.`product` 
    group by 
      `classicmodels`.`product`.`product_line` 
    having 
      (
        count(*) + ?
      ) > all (
        select 
          count(
            distinct `classicmodels`.`product`.`product_id`
          ) 
        from 
          `classicmodels`.`product` 
        group by 
          `classicmodels`.`product`.`product_line`
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
       count(distinct `t`.`city`, `t`.`country`) 
    from
       (
          select
             `classicmodels`.`customerdetail`.`city`,
             `classicmodels`.`customerdetail`.`country` 
          from
             `classicmodels`.`customerdetail` 
          where
             `classicmodels`.`customerdetail`.`address_line_second` is not null
       )
       as `t`    
     */
    public void findDistinctCustomerCityCountryWithNoNullAddress() {

        // Table<?>
        var t = select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                .from(CUSTOMERDETAIL)
                .where(CUSTOMERDETAIL.ADDRESS_LINE_SECOND.isNotNull())
                .asTable("t");

        System.out.println("EXAMPLE 7\n"
                + ctx.select(
                        (countDistinct(t.fields())))
                        .from(t)
                        .fetch()
        );
    }

    // EXAMPLE 8
    /*
    select 
      avg(
        `classicmodels`.`orderdetail`.`price_each`
      ), 
      avg(
        distinct `classicmodels`.`orderdetail`.`price_each`
      ), 
      sum(
        `classicmodels`.`orderdetail`.`price_each`
      ), 
      sum(
        distinct `classicmodels`.`orderdetail`.`price_each`
      ), 
      min(
        `classicmodels`.`orderdetail`.`price_each`
      ), 
      min(
        distinct `classicmodels`.`orderdetail`.`price_each`
      ), 
      max(
        `classicmodels`.`orderdetail`.`price_each`
      ), 
      max(
        distinct `classicmodels`.`orderdetail`.`price_each`
      ) 
    from 
      `classicmodels`.`orderdetail`
     */
    public void avgSumMinMaxPriceEach() {

        System.out.println("EXAMPLE 8\n"
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

    // EXAMPLE 9
    /*
    select 
      group_concat(
        `classicmodels`.`office`.`country` separator ','
      ), 
      group_concat(
        distinct `classicmodels`.`office`.`country` separator ','
      ) 
    from 
      `classicmodels`.`office`
     */
    public void groupConcatOfficeCountries() {

        System.out.println("EXAMPLE 9\n"
                + ctx.select(
                        groupConcat(OFFICE.COUNTRY),
                        groupConcatDistinct(OFFICE.COUNTRY)
                ).from(OFFICE)
                        .fetch()
        );
    }

    // EXAMPLE 10
    /*
    select 
      distinct count(*) over () as `sales` 
    from 
      `classicmodels`.`sale` 
    group by 
      `classicmodels`.`sale`.`employee_number`    
    */
    public void countDistinctSalesByEmployeeNumber() {

        System.out.println("EXAMPLE 10 (count result): "
                + ctx.selectDistinct(count().over().as("sales"))
                        .from(SALE)
                        .groupBy(SALE.EMPLOYEE_NUMBER)
                        .fetchOneInto(int.class)
        );
    }
    
    /* Emulate PostgreSQL DISTINCT ON */
    // EXAMPLE 11
    /* The following statement sorts the result set by the product's vendor and scale, 
       and then for each group of duplicates, it keeps the first row in the returned result set */    
    /* 
    
    */    
    public void findProductsByVendorScale() {

        System.out.println("EXAMPLE 11\n" +
                ctx.selectDistinct(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE)
                        .on(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE)
                        .from(PRODUCT)         
                        .orderBy(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE)
                        .fetch()
        );
        
        /* or, like this */        
        /*
        System.out.println("EXAMPLE 11\n" +
                ctx.select(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE)
                        .distinctOn(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE)
                        .from(PRODUCT) 
                        .orderBy(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE)
                        .fetch()
        );
        */
    }
    
    // EXAMPLE 12
    /* What is the employee numbers of the max sales per fiscal years */        
    public void findEmployeeNumberOfMaxSalePerFiscalYear() {

        /*
        select 
          `t`.`employee_number`, 
          `t`.`fiscal_year`, 
          `t`.`sale` 
        from 
          (
            select 
              `classicmodels`.`sale`.`employee_number`, 
              `classicmodels`.`sale`.`fiscal_year`, 
              `classicmodels`.`sale`.`sale`, 
              row_number() over (
                partition by `classicmodels`.`sale`.`fiscal_year` 
                order by 
                  `classicmodels`.`sale`.`fiscal_year`, 
                  `classicmodels`.`sale`.`sale` desc
              ) as `rn` 
            from 
              `classicmodels`.`sale`
          ) as `t` 
        where 
          `rn` = 1 
        order by 
          `fiscal_year`, 
          `sale` desc        
        */
        System.out.println("EXAMPLE 12.1\n" +
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
          `classicmodels`.`sale`.`employee_number`, 
          `classicmodels`.`sale`.`fiscal_year`, 
          `classicmodels`.`sale`.`sale` 
        from 
          `classicmodels`.`sale` 
          join (
            select 
              `classicmodels`.`sale`.`fiscal_year` as `fy`, 
              max(`classicmodels`.`sale`.`sale`) as `ms` 
            from 
              `classicmodels`.`sale` 
            group by 
              `classicmodels`.`sale`.`fiscal_year`
          ) as `alias_51404515` on (
            `classicmodels`.`sale`.`fiscal_year` = fy 
            and `classicmodels`.`sale`.`sale` = ms
          ) 
        order by 
          `classicmodels`.`sale`.`fiscal_year`, 
          `classicmodels`.`sale`.`sale` desc        
         */
        System.out.println("EXAMPLE 12.2\n"
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
           `t`.`employee_number`,
           `t`.`fiscal_year`,
           `t`.`sale` 
        from
           (
              select distinct
                 `classicmodels`.`sale`.`employee_number`,
                 `classicmodels`.`sale`.`fiscal_year`,
                 `classicmodels`.`sale`.`sale`,
                 row_number() over (partition by `classicmodels`.`sale`.`fiscal_year` 
              order by
                 `classicmodels`.`sale`.`fiscal_year`, `classicmodels`.`sale`.`sale` desc) as `rn` 
              from
                 `classicmodels`.`sale`
           )
           as `t` 
        where
           `t`.`rn` = ? 
        order by
           `t`.`fiscal_year`        
         */
        // Table<?>
        var t = selectDistinct(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_,
                rowNumber().over(partitionBy(SALE.FISCAL_YEAR)
                        .orderBy(SALE.FISCAL_YEAR, SALE.SALE_.desc())).as("rn"))
                .from(SALE).asTable("t");

        System.out.println("EXAMPLE 12.3\n"
                + ctx.select(t.field("employee_number"), t.field("fiscal_year"), t.field("sale"))
                        .from(t)
                        .where(t.field("rn", Integer.class).eq(1))
                        .orderBy(t.field("fiscal_year"))
                        .fetch()
        );    
        
        /* SQL alternative based on row_number() and QUALIFY */
        /*
        select 
          distinct `t`.`employee_number`, 
          `t`.`fiscal_year`, 
          `t`.`sale` 
        from 
          (
            select 
              *, 
              (
                row_number() over (
                  partition by `classicmodels`.`sale`.`fiscal_year` 
                  order by 
                    `classicmodels`.`sale`.`fiscal_year`, 
                    `classicmodels`.`sale`.`sale` desc
                ) = ?
              ) as `w0` 
            from 
              `classicmodels`.`sale`
          ) as `t` 
        where 
          `w0` 
        order by 
          `t`.`fiscal_year`        
        */
        System.out.println("EXAMPLE 12.4\n"
                + ctx.selectDistinct(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, SALE.SALE_)
                        .from(SALE)
                        .qualify(rowNumber().over(partitionBy(SALE.FISCAL_YEAR)
                                .orderBy(SALE.FISCAL_YEAR, SALE.SALE_.desc())).eq(1))
                        .orderBy(SALE.FISCAL_YEAR)
                        .fetch()
        );
    }     
    
    // EXAMPLE 13
    /* What is the distinct employee numbers ordered by min sales */
    /*
    select 
      `t`.`employee_number` 
    from 
      (
        select 
          `classicmodels`.`sale`.`employee_number`, 
          min(`classicmodels`.`sale`.`sale`) as `sale` 
        from 
          `classicmodels`.`sale` 
        group by 
          `classicmodels`.`sale`.`employee_number`
      ) as `t` 
    order by 
      `t`.`sale`    
    */
    public void findDistinctEmployeeNumberOrderByMinSale() {
        
        System.out.println("EXAMPLE 13\n"
                + ctx.select(field(name("t", "employee_number")))
                        .from(select(SALE.EMPLOYEE_NUMBER, min(SALE.SALE_).as("sale"))
                                .from(SALE)
                                .groupBy(SALE.EMPLOYEE_NUMBER).asTable("t"))
                        .orderBy(field(name("t", "sale")))
                        .fetch()
        );
    }
}