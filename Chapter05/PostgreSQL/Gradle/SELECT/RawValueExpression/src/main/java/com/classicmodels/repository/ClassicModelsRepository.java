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
      "public"."product"."product_id",
      "public"."product"."product_name",
      "public"."product"."product_line",
      "public"."product"."product_scale",
      "public"."product"."product_vendor",
      "public"."product"."product_description",
      "public"."product"."quantity_in_stock",
      "public"."product"."buy_price",
      "public"."product"."msrp"
    from
      "public"."product"
    where
      (
         "public"."product"."product_vendor",
         "public"."product"."product_scale",
         "public"."product"."product_line"
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
      "public"."product"."product_id",
      "public"."product"."product_name",
      "public"."product"."product_line",
      "public"."product"."product_scale",
      "public"."product"."product_vendor",
      "public"."product"."product_description",
      "public"."product"."quantity_in_stock",
      "public"."product"."buy_price",
      "public"."product"."msrp"
    from
      "public"."product"
    where
      (
        "public"."product"."product_id",
        "public"."product"."product_vendor",
        "public"."product"."product_scale",
        "public"."product"."product_line"
      ) = (
            select
              "public"."product"."product_id",
              ?,
              "public"."product"."product_scale",
              "public"."product"."product_line"
            from
              "public"."product"
            where
              "public"."product"."product_id" = ?
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
      "public"."product"."product_id",
      "public"."product"."product_name",
      "public"."product"."product_line",
      "public"."product"."product_scale",
      "public"."product"."product_vendor",
      "public"."product"."product_description",
      "public"."product"."quantity_in_stock",
      "public"."product"."buy_price",
      "public"."product"."msrp"
    from
      "public"."product"
    where
       (
         "public"."product"."product_vendor",
         "public"."product"."product_scale",
         "public"."product"."product_line"
       ) in ((?, ?, ?), (?, ?, ?))
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
      "public"."product"."product_id",
      "public"."product"."product_name",
      "public"."product"."product_line",
      "public"."product"."product_scale",
      "public"."product"."product_vendor",
      "public"."product"."product_description",
      "public"."product"."quantity_in_stock",
      "public"."product"."buy_price",
      "public"."product"."msrp"
    from
      "public"."product"
    where
      (
         "public"."product"."product_vendor",
         "public"."product"."product_scale",
         "public"."product"."product_line"
      ) in ((?, ?, ?), (?, ?, ?))
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
      "public"."customerdetail"."customer_number",
      "public"."customerdetail"."address_line_first",
      "public"."customerdetail"."address_line_second",
      "public"."customerdetail"."city",
      "public"."customerdetail"."state",
      "public"."customerdetail"."postal_code",
      "public"."customerdetail"."country"
    from
      "public"."customerdetail"
    where
       (
         "public"."customerdetail"."city",
         "public"."customerdetail"."country"
       ) in (
         select
           "public"."office"."city",
           "public"."office"."country"
         from
           "public"."office"
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
      "public"."order"."order_id",
      "public"."order"."order_date",
      "public"."order"."required_date",
      "public"."order"."shipped_date",
      "public"."order"."status",
      "public"."order"."comments",
      "public"."order"."customer_number"
    from
      "public"."order"
    where
       (
         "public"."order"."order_date",
         "public"."order"."shipped_date"
       ) between (cast(? as date), cast(? as date))
             and (cast(? as date), cast(? as date))
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
      "public"."order"."order_id",
      "public"."order"."order_date",
      "public"."order"."required_date",
      "public"."order"."shipped_date",
      "public"."order"."status",
      "public"."order"."comments",
      "public"."order"."customer_number"
    from
      "public"."order"
    where
      (
        ( 
          "public"."order"."order_date",
          "public"."order"."shipped_date"
        ) overlaps (cast(? as date), cast(? as date))
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
      "public"."sale"."sale_id",
      "public"."sale"."fiscal_year",
      "public"."sale"."sale",
      "public"."sale"."employee_number"
    from
      "public"."sale"
    where
      (
        ? <= "public"."sale"."sale"
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
      "public"."office"."office_code",
      "public"."office"."city",
      "public"."office"."phone",
      "public"."office"."address_line_first",
      "public"."office"."address_line_second",
      "public"."office"."state",
      "public"."office"."country",
      "public"."office"."postal_code",
      "public"."office"."territory"
    from
      "public"."office"
    where
       (
         "public"."office"."city",
         "public"."office"."country"
       ) is (not) null
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

    // EXAMPLE 10
    /*
    update
      "public"."office"
    set
      ("postal_code", "state") = (
        select
          "public"."customerdetail"."postal_code",
          "public"."customerdetail"."state"
        from
          "public"."customerdetail"
        where
          "public"."office"."city" = "public"."customerdetail"."city"
        limit
          ?
      )
    where
      "public"."office"."office_code" = ?
    */
    @Transactional
    public void updateOfficePostalCodeAndState() {

        System.out.println("EXAMPLE 10 (rows affected): " +
                + ctx.update(OFFICE)
                        .set(row(OFFICE.POSTAL_CODE, OFFICE.STATE),
                                select(CUSTOMERDETAIL.POSTAL_CODE, CUSTOMERDETAIL.STATE).from(CUSTOMERDETAIL)
                                        .where(OFFICE.CITY.eq(CUSTOMERDETAIL.CITY)).limit(1))
                        .where(OFFICE.OFFICE_CODE.eq("1"))
                        .execute()
        );
    }
}