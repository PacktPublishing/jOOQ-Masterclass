package com.classicmodels.repository;

import java.time.LocalDate;
import java.util.List;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Row3;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // EXAMPLE 1 - Comparison predicates 
    /*
    select 
      `classicmodels`.`product`.`product_id`, 
      `classicmodels`.`product`.`product_name`, 
      `classicmodels`.`product`.`product_line`, 
      `classicmodels`.`product`.`product_scale`, 
      `classicmodels`.`product`.`product_vendor`, 
      `classicmodels`.`product`.`product_description`, 
      `classicmodels`.`product`.`quantity_in_stock`, 
      `classicmodels`.`product`.`buy_price`, 
      `classicmodels`.`product`.`msrp` 
    from 
      `classicmodels`.`product` 
    where 
      (
        `classicmodels`.`product`.`product_vendor`, 
        `classicmodels`.`product`.`product_scale`, 
        `classicmodels`.`product`.`product_line`
      ) = (?, ?, ?)
    */
    public void findProductsByVendorScaleAndProductLine() {

        // row value expression
        Condition condition = row(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE, PRODUCT.PRODUCT_LINE)
                .eq("Carousel DieCast Legends", "1:24", "Classic Cars");

        // SQL query using the above row value expression
        System.out.println("EXAMPLE 1\n"
                + ctx.selectFrom(PRODUCT)
                        .where(condition)
                        .fetch()
        );
        
        // or, as a single fluent query
        System.out.println("EXAMPLE 1\n"
                + ctx.selectFrom(PRODUCT)
                        .where(
                                row(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE, PRODUCT.PRODUCT_LINE)
                                .eq("Carousel DieCast Legends", "1:24", "Classic Cars")
                        )
                        .fetch()
        );
    }

    // EXAMPLE 2
    /*
    select 
      `classicmodels`.`product`.`product_id`, 
      `classicmodels`.`product`.`product_name`, 
      `classicmodels`.`product`.`product_line`, 
      `classicmodels`.`product`.`product_scale`, 
      `classicmodels`.`product`.`product_vendor`, 
      `classicmodels`.`product`.`product_description`, 
      `classicmodels`.`product`.`quantity_in_stock`, 
      `classicmodels`.`product`.`buy_price`, 
      `classicmodels`.`product`.`msrp` 
    from 
      `classicmodels`.`product` 
    where 
      (
        `classicmodels`.`product`.`product_id`, 
        `classicmodels`.`product`.`product_vendor`, 
        `classicmodels`.`product`.`product_scale`, 
        `classicmodels`.`product`.`product_line`
      ) = (
        select 
          `classicmodels`.`product`.`product_id`, 
          ?, 
          `classicmodels`.`product`.`product_scale`, 
          `classicmodels`.`product`.`product_line` 
        from 
          `classicmodels`.`product` 
        where 
          `classicmodels`.`product`.`product_id` = ?
      )
    */
    public void findProductByIdVendorAsCarouselDieCastLegendsScaleAndProductLine() {

        // row value expression
        Condition condition = row(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_VENDOR,
                PRODUCT.PRODUCT_SCALE, PRODUCT.PRODUCT_LINE)
                .eq(select(PRODUCT.PRODUCT_ID, val("Carousel DieCast Legends"),
                        PRODUCT.PRODUCT_SCALE, PRODUCT.PRODUCT_LINE)
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_ID.eq(63L)));

        // SQL query using the above row value expression
        System.out.println("EXAMPLE 2.1\n" +
                ctx.selectFrom(PRODUCT)
                        .where(condition)
                        .fetchOne()
        );
        
        // or, as a single fluent query
        System.out.println("EXAMPLE 2.2\n" +
                ctx.selectFrom(PRODUCT)
                        .where(
                                row(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_VENDOR,
                                        PRODUCT.PRODUCT_SCALE, PRODUCT.PRODUCT_LINE)
                                        .eq(select(PRODUCT.PRODUCT_ID, val("Carousel DieCast Legends"),
                                                PRODUCT.PRODUCT_SCALE, PRODUCT.PRODUCT_LINE)
                                                .from(PRODUCT)
                                                .where(PRODUCT.PRODUCT_ID.eq(63L)))
                        )
                        .fetchOne()
        );
    }

    // EXAMPLE 3 - IN predicates
    /*
    select 
      `classicmodels`.`product`.`product_id`, 
      `classicmodels`.`product`.`product_name`, 
      `classicmodels`.`product`.`product_line`, 
      `classicmodels`.`product`.`product_scale`, 
      `classicmodels`.`product`.`product_vendor`, 
      `classicmodels`.`product`.`product_description`, 
      `classicmodels`.`product`.`quantity_in_stock`, 
      `classicmodels`.`product`.`buy_price`, 
      `classicmodels`.`product`.`msrp` 
    from 
      `classicmodels`.`product` 
    where 
      (
        `classicmodels`.`product`.`product_vendor`, 
        `classicmodels`.`product`.`product_scale`, 
        `classicmodels`.`product`.`product_line`
      ) in (
        (?, ?, ?), 
        (?, ?, ?)
      )
    */
    public void findProductsByVendorScaleAndProductLineIn() {

        // row value expression
        Condition condition = row(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE, PRODUCT.PRODUCT_LINE)
                .in(row("Carousel DieCast Legends", "1:24", "Classic Cars"),
                        row("Exoto Designs", "1:18", "Vintage Cars"));

        // SQL query using the above row value expression
        System.out.println("EXAMPLE 3\n" + 
                ctx.selectFrom(PRODUCT)
                        .where(condition)
                        .fetch()
        );
    }

    // EXAMPLE 4
    /*
    select 
      `classicmodels`.`product`.`product_id`, 
      `classicmodels`.`product`.`product_name`, 
      `classicmodels`.`product`.`product_line`, 
      `classicmodels`.`product`.`product_scale`, 
      `classicmodels`.`product`.`product_vendor`, 
      `classicmodels`.`product`.`product_description`, 
      `classicmodels`.`product`.`quantity_in_stock`, 
      `classicmodels`.`product`.`buy_price`, 
      `classicmodels`.`product`.`msrp` 
    from 
      `classicmodels`.`product` 
    where 
      (
        `classicmodels`.`product`.`product_vendor`, 
        `classicmodels`.`product`.`product_scale`, 
        `classicmodels`.`product`.`product_line`
      ) in (
        (?, ?, ?), 
        (?, ?, ?)
      )
    */
    public void findProductsByVendorScaleAndProductLineInCollection(
            List<Row3<String, String, String>> rows) {

        // row value expression
        Condition condition = row(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE, PRODUCT.PRODUCT_LINE)
                .in(rows);

        // SQL query using the above row value expression
        System.out.println("EXAMPLE 4\n" + 
                ctx.selectFrom(PRODUCT)
                        .where(condition)
                        .fetch()
        );
    }

    // EXAMPLE 5
    /*
    select 
      `classicmodels`.`customerdetail`.`customer_number`, 
      `classicmodels`.`customerdetail`.`address_line_first`, 
      `classicmodels`.`customerdetail`.`address_line_second`, 
      `classicmodels`.`customerdetail`.`city`, 
      `classicmodels`.`customerdetail`.`state`, 
      `classicmodels`.`customerdetail`.`postal_code`, 
      `classicmodels`.`customerdetail`.`country` 
    from 
      `classicmodels`.`customerdetail` 
    where 
      (
        `classicmodels`.`customerdetail`.`city`, 
        `classicmodels`.`customerdetail`.`country`
      ) in (
        select 
          `classicmodels`.`office`.`city`, 
          `classicmodels`.`office`.`country` 
        from 
          `classicmodels`.`office`
      )
    */
    public void findProductsByVendorScaleAndProductLineInSelect() {

        // row value expression
        Condition condition = row(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                .in(select(OFFICE.CITY, OFFICE.COUNTRY).from(OFFICE));

        // SQL query using the above row value expression
        System.out.println("EXAMPLE 5.1\n" +
                ctx.selectFrom(CUSTOMERDETAIL)
                        .where(condition)
                        .fetch()
        );
        
        // or, fluent and compact
        System.out.println("EXAMPLE 5.2\n" +
                ctx.selectFrom(CUSTOMERDETAIL)
                        .where(row(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                                .in(select(OFFICE.CITY, OFFICE.COUNTRY).from(OFFICE)))
                        .fetch()
        );
    }

    // EXAMPLE 6: BETWEEN predicates
    /*
    select 
      `classicmodels`.`order`.`order_id`, 
      `classicmodels`.`order`.`order_date`, 
      `classicmodels`.`order`.`required_date`, 
      `classicmodels`.`order`.`shipped_date`, 
      `classicmodels`.`order`.`status`, 
      `classicmodels`.`order`.`comments`, 
      `classicmodels`.`order`.`customer_number` 
    from 
      `classicmodels`.`order` 
    where 
      (
        (
          `classicmodels`.`order`.`order_date`, 
          `classicmodels`.`order`.`shipped_date`
        ) >= (?, ?) 
        and (
          `classicmodels`.`order`.`order_date`, 
          `classicmodels`.`order`.`shipped_date`
        ) <= (?, ?)
      )
    */
    public void findOrdersBetweenOrderDateAndShippedDate() {

        // row value expression
        Condition condition = row(ORDER.ORDER_DATE, ORDER.SHIPPED_DATE)
                .between(LocalDate.of(2003, 1, 6), LocalDate.of(2003, 1, 13))
                .and(LocalDate.of(2003, 12, 13), LocalDate.of(2003, 12, 11));

        // SQL query using the above row value expression
        System.out.println("EXAMPLE 6\n" + 
                ctx.selectFrom(ORDER)
                        .where(condition)
                        .fetch()
        );
    }

    // EXAMPLE 7: Dates overlapping
    /*
    select 
      `classicmodels`.`order`.`order_id`, 
      `classicmodels`.`order`.`order_date`, 
      `classicmodels`.`order`.`required_date`, 
      `classicmodels`.`order`.`shipped_date`, 
      `classicmodels`.`order`.`status`, 
      `classicmodels`.`order`.`comments`, 
      `classicmodels`.`order`.`customer_number` 
    from 
      `classicmodels`.`order` 
    where 
      (
        ? <= `classicmodels`.`order`.`shipped_date` 
        and `classicmodels`.`order`.`order_date` <= ?
      )
    */
    public void findOverlappingDates() {

        // row value expression
        Condition condition = row(ORDER.ORDER_DATE, ORDER.SHIPPED_DATE)
                .overlaps(LocalDate.of(2003, 1, 6), LocalDate.of(2003, 1, 13));

        // SQL query using the above row value expression
        System.out.println("EXAMPLE 7\n" +
                ctx.selectFrom(ORDER)
                        .where(condition)
                        .fetch()
        );
    }

    // EXAMPLE 8
    /*
    select 
      `classicmodels`.`sale`.`sale_id`, 
      `classicmodels`.`sale`.`fiscal_year`, 
      `classicmodels`.`sale`.`sale`, 
      `classicmodels`.`sale`.`employee_number` 
    from 
      `classicmodels`.`sale` 
    where 
      (
        ? <= cast(
          `classicmodels`.`sale`.`sale` as decimal
        ) 
        and ? <= ?
      )
    */
    public void findOverlappingSales() {

        // row value expression
        Condition condition = row(val(0.0), SALE.SALE_)
                .overlaps(5000.0, 5500.0);

        // SQL query using the above row value expression
        System.out.println("EXAMPLE 8\n" +
                ctx.selectFrom(SALE)
                        .where(condition)
                        .fetch()
        );
    }

    // EXAMPLE 9 - NULLs and row value expression
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
        `classicmodels`.`office`.`city` is null 
        and `classicmodels`.`office`.`country` is (not) null
      )
    */
    public void findOfficeNullCityAndCountry() {

        System.out.println("EXAMPLE 9.1\n" +
                ctx.selectFrom(OFFICE)
                        .where(row(OFFICE.CITY, OFFICE.COUNTRY).isNull())
                        .fetch()
        );
        
        System.out.println("EXAMPLE 9.2\n" +
                ctx.selectFrom(OFFICE)
                        .where(row(OFFICE.CITY, OFFICE.COUNTRY).isNotNull())
                        .fetch()
        );
    }
}