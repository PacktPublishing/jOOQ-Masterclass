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
      [classicmodels].[dbo].[product].[product_id], 
      [classicmodels].[dbo].[product].[product_name], 
      [classicmodels].[dbo].[product].[product_line], 
      [classicmodels].[dbo].[product].[product_scale], 
      [classicmodels].[dbo].[product].[product_vendor], 
      [classicmodels].[dbo].[product].[product_description], 
      [classicmodels].[dbo].[product].[quantity_in_stock], 
      [classicmodels].[dbo].[product].[buy_price], 
      [classicmodels].[dbo].[product].[msrp] 
    from 
      [classicmodels].[dbo].[product] 
    where 
      (
        [classicmodels].[dbo].[product].[product_vendor] = ? 
        and [classicmodels].[dbo].[product].[product_scale] = ? 
        and [classicmodels].[dbo].[product].[product_line] = ?
      )    
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
      [classicmodels].[dbo].[product].[product_id], 
      [classicmodels].[dbo].[product].[product_name], 
      [classicmodels].[dbo].[product].[product_line], 
      [classicmodels].[dbo].[product].[product_scale], 
      [classicmodels].[dbo].[product].[product_vendor], 
      [classicmodels].[dbo].[product].[product_description], 
      [classicmodels].[dbo].[product].[quantity_in_stock], 
      [classicmodels].[dbo].[product].[buy_price], 
      [classicmodels].[dbo].[product].[msrp] 
    from 
      [classicmodels].[dbo].[product] 
    where 
      exists (
        select 
          [alias_1].[v0], 
          [alias_1].[v1], 
          [alias_1].[v2], 
          [alias_1].[v3] 
        from 
          (
            select 
              [classicmodels].[dbo].[product].[product_id] [v0], 
              ? [v1], 
              [classicmodels].[dbo].[product].[product_scale] [v2], 
              [classicmodels].[dbo].[product].[product_line] [v3] 
            from 
              [classicmodels].[dbo].[product] 
            where 
              [classicmodels].[dbo].[product].[product_id] = ?
          ) [alias_1] 
        where 
          (
            [classicmodels].[dbo].[product].[product_id] = [alias_1].[v0] 
            and [classicmodels].[dbo].[product].[product_vendor] = [alias_1].[v1] 
            and [classicmodels].[dbo].[product].[product_scale] = [alias_1].[v2] 
            and [classicmodels].[dbo].[product].[product_line] = [alias_1].[v3]
          )
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
        System.out.println("EXAMPLE 2\n" +
                ctx.selectFrom(PRODUCT)
                        .where(condition)
                        .fetchOne()
        );
        
        // or, as a single fluent query
        System.out.println("EXAMPLE 2\n" +
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
      [classicmodels].[dbo].[product].[product_id], 
      [classicmodels].[dbo].[product].[product_name], 
      [classicmodels].[dbo].[product].[product_line], 
      [classicmodels].[dbo].[product].[product_scale], 
      [classicmodels].[dbo].[product].[product_vendor], 
      [classicmodels].[dbo].[product].[product_description], 
      [classicmodels].[dbo].[product].[quantity_in_stock], 
      [classicmodels].[dbo].[product].[buy_price], 
      [classicmodels].[dbo].[product].[msrp] 
    from 
      [classicmodels].[dbo].[product] 
    where 
      (
        (
          [classicmodels].[dbo].[product].[product_vendor] = ? 
          and [classicmodels].[dbo].[product].[product_scale] = ? 
          and [classicmodels].[dbo].[product].[product_line] = ?
        ) 
        or (
          [classicmodels].[dbo].[product].[product_vendor] = ? 
          and [classicmodels].[dbo].[product].[product_scale] = ? 
          and [classicmodels].[dbo].[product].[product_line] = ?
        )
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
      [classicmodels].[dbo].[product].[product_id], 
      [classicmodels].[dbo].[product].[product_name], 
      [classicmodels].[dbo].[product].[product_line], 
      [classicmodels].[dbo].[product].[product_scale], 
      [classicmodels].[dbo].[product].[product_vendor], 
      [classicmodels].[dbo].[product].[product_description], 
      [classicmodels].[dbo].[product].[quantity_in_stock], 
      [classicmodels].[dbo].[product].[buy_price], 
      [classicmodels].[dbo].[product].[msrp] 
    from 
      [classicmodels].[dbo].[product] 
    where 
      (
        (
          [classicmodels].[dbo].[product].[product_vendor] = ? 
          and [classicmodels].[dbo].[product].[product_scale] = ? 
          and [classicmodels].[dbo].[product].[product_line] = ?
        ) 
        or (
          [classicmodels].[dbo].[product].[product_vendor] = ? 
          and [classicmodels].[dbo].[product].[product_scale] = ? 
          and [classicmodels].[dbo].[product].[product_line] = ?
        )
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
      [classicmodels].[dbo].[customerdetail].[customer_number], 
      [classicmodels].[dbo].[customerdetail].[address_line_first], 
      [classicmodels].[dbo].[customerdetail].[address_line_second], 
      [classicmodels].[dbo].[customerdetail].[city], 
      [classicmodels].[dbo].[customerdetail].[state], 
      [classicmodels].[dbo].[customerdetail].[postal_code], 
      [classicmodels].[dbo].[customerdetail].[country] 
    from 
      [classicmodels].[dbo].[customerdetail] 
    where 
      exists (
        select 
          [alias_1].[v0], 
          [alias_1].[v1] 
        from 
          (
            select 
              [classicmodels].[dbo].[office].[city] [v0], 
              [classicmodels].[dbo].[office].[country] [v1] 
            from 
              [classicmodels].[dbo].[office]
          ) [alias_1] 
        where 
          (
            [classicmodels].[dbo].[customerdetail].[city] = [alias_1].[v0] 
            and [classicmodels].[dbo].[customerdetail].[country] = [alias_1].[v1]
          )
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
      [classicmodels].[dbo].[order].[order_id], 
      [classicmodels].[dbo].[order].[order_date], 
      [classicmodels].[dbo].[order].[required_date], 
      [classicmodels].[dbo].[order].[shipped_date], 
      [classicmodels].[dbo].[order].[status], 
      [classicmodels].[dbo].[order].[comments], 
      [classicmodels].[dbo].[order].[customer_number] 
    from 
      [classicmodels].[dbo].[order] 
    where 
      (
        (
          [classicmodels].[dbo].[order].[order_date] >= ? 
          and (
            [classicmodels].[dbo].[order].[order_date] > ? 
            or (
              [classicmodels].[dbo].[order].[order_date] = ? 
              and [classicmodels].[dbo].[order].[shipped_date] >= ?
            )
          )
        ) 
        and (
          [classicmodels].[dbo].[order].[order_date] <= ? 
          and (
            [classicmodels].[dbo].[order].[order_date] < ? 
            or (
              [classicmodels].[dbo].[order].[order_date] = ? 
              and [classicmodels].[dbo].[order].[shipped_date] <= ?
            )
          )
        )
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
      [classicmodels].[dbo].[order].[order_id], 
      [classicmodels].[dbo].[order].[order_date], 
      [classicmodels].[dbo].[order].[required_date], 
      [classicmodels].[dbo].[order].[shipped_date], 
      [classicmodels].[dbo].[order].[status], 
      [classicmodels].[dbo].[order].[comments], 
      [classicmodels].[dbo].[order].[customer_number] 
    from 
      [classicmodels].[dbo].[order] 
    where 
      (
        ? <= [classicmodels].[dbo].[order].[shipped_date] 
        and [classicmodels].[dbo].[order].[order_date] <= ?
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
      [classicmodels].[dbo].[sale].[sale_id], 
      [classicmodels].[dbo].[sale].[fiscal_year], 
      [classicmodels].[dbo].[sale].[sale], 
      [classicmodels].[dbo].[sale].[employee_number] 
    from 
      [classicmodels].[dbo].[sale] 
    where 
      (
        ? <= cast(
          [classicmodels].[dbo].[sale].[sale] as float
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
      (
        [classicmodels].[dbo].[office].[city] is null 
        and [classicmodels].[dbo].[office].[country] is (not) null
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