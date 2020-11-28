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
import static org.jooq.impl.DSL.groupConcat;
import static org.jooq.impl.DSL.groupConcatDistinct;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.maxDistinct;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.minDistinct;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
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
      count(
        distinct `alias_37460252`.`city`, 
        `alias_37460252`.`country`
      ) 
    from 
      (
        select 
          `classicmodels`.`customerdetail`.`city`, 
          `classicmodels`.`customerdetail`.`country` 
        from 
          `classicmodels`.`customerdetail` 
        where 
          `classicmodels`.`customerdetail`.`address_line_second` is not null
      ) as `alias_37460252`
     */
    public void findDistinctCustomerCityCountryWithNoNullAddress() {

        // Table<?>
        var t = select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                .from(CUSTOMERDETAIL)
                .where(CUSTOMERDETAIL.ADDRESS_LINE_SECOND.isNotNull())
                .asTable();

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
}
