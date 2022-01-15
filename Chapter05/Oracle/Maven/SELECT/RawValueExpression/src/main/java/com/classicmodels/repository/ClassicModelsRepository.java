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
      "CLASSICMODELS"."PRODUCT"."PRODUCT_ID", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_NAME", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_LINE", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_SCALE", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_VENDOR", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_DESCRIPTION", 
      "CLASSICMODELS"."PRODUCT"."QUANTITY_IN_STOCK", 
      "CLASSICMODELS"."PRODUCT"."BUY_PRICE", 
      "CLASSICMODELS"."PRODUCT"."MSRP" 
    from 
      "CLASSICMODELS"."PRODUCT" 
    where 
      (
        "CLASSICMODELS"."PRODUCT"."PRODUCT_VENDOR", 
        "CLASSICMODELS"."PRODUCT"."PRODUCT_SCALE", 
        "CLASSICMODELS"."PRODUCT"."PRODUCT_LINE"
      ) = (
        (?, ?, ?)
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
      "CLASSICMODELS"."PRODUCT"."PRODUCT_ID", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_NAME", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_LINE", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_SCALE", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_VENDOR", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_DESCRIPTION", 
      "CLASSICMODELS"."PRODUCT"."QUANTITY_IN_STOCK", 
      "CLASSICMODELS"."PRODUCT"."BUY_PRICE", 
      "CLASSICMODELS"."PRODUCT"."MSRP" 
    from 
      "CLASSICMODELS"."PRODUCT" 
    where 
      (
        "CLASSICMODELS"."PRODUCT"."PRODUCT_ID", 
        "CLASSICMODELS"."PRODUCT"."PRODUCT_VENDOR", 
        "CLASSICMODELS"."PRODUCT"."PRODUCT_SCALE", 
        "CLASSICMODELS"."PRODUCT"."PRODUCT_LINE"
      ) = (
        (
          select 
            "CLASSICMODELS"."PRODUCT"."PRODUCT_ID", 
            ?, 
            "CLASSICMODELS"."PRODUCT"."PRODUCT_SCALE", 
            "CLASSICMODELS"."PRODUCT"."PRODUCT_LINE" 
          from 
            "CLASSICMODELS"."PRODUCT" 
          where 
            "CLASSICMODELS"."PRODUCT"."PRODUCT_ID" = ?
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
      "CLASSICMODELS"."PRODUCT"."PRODUCT_ID", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_NAME", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_LINE", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_SCALE", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_VENDOR", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_DESCRIPTION", 
      "CLASSICMODELS"."PRODUCT"."QUANTITY_IN_STOCK", 
      "CLASSICMODELS"."PRODUCT"."BUY_PRICE", 
      "CLASSICMODELS"."PRODUCT"."MSRP" 
    from 
      "CLASSICMODELS"."PRODUCT" 
    where 
      (
        "CLASSICMODELS"."PRODUCT"."PRODUCT_VENDOR", 
        "CLASSICMODELS"."PRODUCT"."PRODUCT_SCALE", 
        "CLASSICMODELS"."PRODUCT"."PRODUCT_LINE"
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
      "CLASSICMODELS"."PRODUCT"."PRODUCT_ID", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_NAME", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_LINE", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_SCALE", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_VENDOR", 
      "CLASSICMODELS"."PRODUCT"."PRODUCT_DESCRIPTION", 
      "CLASSICMODELS"."PRODUCT"."QUANTITY_IN_STOCK", 
      "CLASSICMODELS"."PRODUCT"."BUY_PRICE", 
      "CLASSICMODELS"."PRODUCT"."MSRP" 
    from 
      "CLASSICMODELS"."PRODUCT" 
    where 
      (
        "CLASSICMODELS"."PRODUCT"."PRODUCT_VENDOR", 
        "CLASSICMODELS"."PRODUCT"."PRODUCT_SCALE", 
        "CLASSICMODELS"."PRODUCT"."PRODUCT_LINE"
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
      "CLASSICMODELS"."CUSTOMERDETAIL"."CUSTOMER_NUMBER", 
      "CLASSICMODELS"."CUSTOMERDETAIL"."ADDRESS_LINE_FIRST", 
      "CLASSICMODELS"."CUSTOMERDETAIL"."ADDRESS_LINE_SECOND", 
      "CLASSICMODELS"."CUSTOMERDETAIL"."CITY", 
      "CLASSICMODELS"."CUSTOMERDETAIL"."STATE", 
      "CLASSICMODELS"."CUSTOMERDETAIL"."POSTAL_CODE", 
      "CLASSICMODELS"."CUSTOMERDETAIL"."COUNTRY" 
    from 
      "CLASSICMODELS"."CUSTOMERDETAIL" 
    where 
      (
        "CLASSICMODELS"."CUSTOMERDETAIL"."CITY", 
        "CLASSICMODELS"."CUSTOMERDETAIL"."COUNTRY"
      ) in (
        (
          select 
            "CLASSICMODELS"."OFFICE"."CITY", 
            "CLASSICMODELS"."OFFICE"."COUNTRY" 
          from 
            "CLASSICMODELS"."OFFICE"
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
      "CLASSICMODELS"."ORDER"."ORDER_ID", 
      "CLASSICMODELS"."ORDER"."ORDER_DATE", 
      "CLASSICMODELS"."ORDER"."REQUIRED_DATE", 
      "CLASSICMODELS"."ORDER"."SHIPPED_DATE", 
      "CLASSICMODELS"."ORDER"."STATUS", 
      "CLASSICMODELS"."ORDER"."COMMENTS", 
      "CLASSICMODELS"."ORDER"."CUSTOMER_NUMBER" 
    from 
      "CLASSICMODELS"."ORDER" 
    where 
      (
        (
          "CLASSICMODELS"."ORDER"."ORDER_DATE" >= cast(? as date) 
          and (
            "CLASSICMODELS"."ORDER"."ORDER_DATE" > cast(? as date) 
            or (
              "CLASSICMODELS"."ORDER"."ORDER_DATE" = cast(? as date) 
              and "CLASSICMODELS"."ORDER"."SHIPPED_DATE" >= cast(? as date)
            )
          )
        ) 
        and (
          "CLASSICMODELS"."ORDER"."ORDER_DATE" <= cast(? as date) 
          and (
            "CLASSICMODELS"."ORDER"."ORDER_DATE" < cast(? as date) 
            or (
              "CLASSICMODELS"."ORDER"."ORDER_DATE" = cast(? as date) 
              and "CLASSICMODELS"."ORDER"."SHIPPED_DATE" <= cast(? as date)
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
     "CLASSICMODELS"."ORDER"."ORDER_ID", 
     "CLASSICMODELS"."ORDER"."ORDER_DATE", 
     "CLASSICMODELS"."ORDER"."REQUIRED_DATE", 
     "CLASSICMODELS"."ORDER"."SHIPPED_DATE", 
     "CLASSICMODELS"."ORDER"."STATUS", 
     "CLASSICMODELS"."ORDER"."COMMENTS", 
     "CLASSICMODELS"."ORDER"."CUSTOMER_NUMBER" 
   from 
     "CLASSICMODELS"."ORDER" 
   where 
     (
       (
         "CLASSICMODELS"."ORDER"."ORDER_DATE", "CLASSICMODELS"."ORDER"."SHIPPED_DATE"
       ) overlaps (
         cast(? as date), 
         cast(? as date)
       )
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
      "CLASSICMODELS"."SALE"."SALE_ID", 
      "CLASSICMODELS"."SALE"."FISCAL_YEAR", 
      "CLASSICMODELS"."SALE"."SALE", 
      "CLASSICMODELS"."SALE"."EMPLOYEE_NUMBER" 
    from 
      "CLASSICMODELS"."SALE" 
    where 
      (
        ? <= "CLASSICMODELS"."SALE"."SALE" 
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
      (
        "CLASSICMODELS"."OFFICE"."CITY" is null 
        and "CLASSICMODELS"."OFFICE"."COUNTRY" is null
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

    // EXAMPLE 10
    /*
    update 
      "CLASSICMODELS"."OFFICE" 
    set 
      ("POSTAL_CODE", "STATE") = (
        select 
          "CLASSICMODELS"."CUSTOMERDETAIL"."POSTAL_CODE", 
          "CLASSICMODELS"."CUSTOMERDETAIL"."STATE" 
        from 
          "CLASSICMODELS"."CUSTOMERDETAIL" 
        where 
          "CLASSICMODELS"."OFFICE"."CITY" = "CLASSICMODELS"."CUSTOMERDETAIL"."CITY" fetch next ? rows only
      ) 
    where 
      "CLASSICMODELS"."OFFICE"."OFFICE_CODE" = ?    
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