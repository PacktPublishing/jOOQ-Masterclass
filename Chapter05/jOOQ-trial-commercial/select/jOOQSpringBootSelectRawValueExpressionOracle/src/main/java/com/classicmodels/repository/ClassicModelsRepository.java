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
      "SYSTEM"."PRODUCT"."PRODUCT_ID", 
      "SYSTEM"."PRODUCT"."PRODUCT_NAME", 
      "SYSTEM"."PRODUCT"."PRODUCT_LINE", 
      "SYSTEM"."PRODUCT"."PRODUCT_SCALE", 
      "SYSTEM"."PRODUCT"."PRODUCT_VENDOR", 
      "SYSTEM"."PRODUCT"."PRODUCT_DESCRIPTION", 
      "SYSTEM"."PRODUCT"."QUANTITY_IN_STOCK", 
      "SYSTEM"."PRODUCT"."BUY_PRICE", 
      "SYSTEM"."PRODUCT"."MSRP" 
    from 
      "SYSTEM"."PRODUCT" 
    where 
      (
        "SYSTEM"."PRODUCT"."PRODUCT_VENDOR", 
        "SYSTEM"."PRODUCT"."PRODUCT_SCALE", 
        "SYSTEM"."PRODUCT"."PRODUCT_LINE"
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
      "SYSTEM"."PRODUCT"."PRODUCT_ID", 
      "SYSTEM"."PRODUCT"."PRODUCT_NAME", 
      "SYSTEM"."PRODUCT"."PRODUCT_LINE", 
      "SYSTEM"."PRODUCT"."PRODUCT_SCALE", 
      "SYSTEM"."PRODUCT"."PRODUCT_VENDOR", 
      "SYSTEM"."PRODUCT"."PRODUCT_DESCRIPTION", 
      "SYSTEM"."PRODUCT"."QUANTITY_IN_STOCK", 
      "SYSTEM"."PRODUCT"."BUY_PRICE", 
      "SYSTEM"."PRODUCT"."MSRP" 
    from 
      "SYSTEM"."PRODUCT" 
    where 
      (
        "SYSTEM"."PRODUCT"."PRODUCT_ID", 
        "SYSTEM"."PRODUCT"."PRODUCT_VENDOR", 
        "SYSTEM"."PRODUCT"."PRODUCT_SCALE", 
        "SYSTEM"."PRODUCT"."PRODUCT_LINE"
      ) = (
        (
          select 
            "SYSTEM"."PRODUCT"."PRODUCT_ID", 
            ?, 
            "SYSTEM"."PRODUCT"."PRODUCT_SCALE", 
            "SYSTEM"."PRODUCT"."PRODUCT_LINE" 
          from 
            "SYSTEM"."PRODUCT" 
          where 
            "SYSTEM"."PRODUCT"."PRODUCT_ID" = ?
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
      "SYSTEM"."PRODUCT"."PRODUCT_ID", 
      "SYSTEM"."PRODUCT"."PRODUCT_NAME", 
      "SYSTEM"."PRODUCT"."PRODUCT_LINE", 
      "SYSTEM"."PRODUCT"."PRODUCT_SCALE", 
      "SYSTEM"."PRODUCT"."PRODUCT_VENDOR", 
      "SYSTEM"."PRODUCT"."PRODUCT_DESCRIPTION", 
      "SYSTEM"."PRODUCT"."QUANTITY_IN_STOCK", 
      "SYSTEM"."PRODUCT"."BUY_PRICE", 
      "SYSTEM"."PRODUCT"."MSRP" 
    from 
      "SYSTEM"."PRODUCT" 
    where 
      (
        "SYSTEM"."PRODUCT"."PRODUCT_VENDOR", 
        "SYSTEM"."PRODUCT"."PRODUCT_SCALE", 
        "SYSTEM"."PRODUCT"."PRODUCT_LINE"
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
      "SYSTEM"."PRODUCT"."PRODUCT_ID", 
      "SYSTEM"."PRODUCT"."PRODUCT_NAME", 
      "SYSTEM"."PRODUCT"."PRODUCT_LINE", 
      "SYSTEM"."PRODUCT"."PRODUCT_SCALE", 
      "SYSTEM"."PRODUCT"."PRODUCT_VENDOR", 
      "SYSTEM"."PRODUCT"."PRODUCT_DESCRIPTION", 
      "SYSTEM"."PRODUCT"."QUANTITY_IN_STOCK", 
      "SYSTEM"."PRODUCT"."BUY_PRICE", 
      "SYSTEM"."PRODUCT"."MSRP" 
    from 
      "SYSTEM"."PRODUCT" 
    where 
      (
        "SYSTEM"."PRODUCT"."PRODUCT_VENDOR", 
        "SYSTEM"."PRODUCT"."PRODUCT_SCALE", 
        "SYSTEM"."PRODUCT"."PRODUCT_LINE"
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
      "SYSTEM"."CUSTOMERDETAIL"."CUSTOMER_NUMBER", 
      "SYSTEM"."CUSTOMERDETAIL"."ADDRESS_LINE_FIRST", 
      "SYSTEM"."CUSTOMERDETAIL"."ADDRESS_LINE_SECOND", 
      "SYSTEM"."CUSTOMERDETAIL"."CITY", 
      "SYSTEM"."CUSTOMERDETAIL"."STATE", 
      "SYSTEM"."CUSTOMERDETAIL"."POSTAL_CODE", 
      "SYSTEM"."CUSTOMERDETAIL"."COUNTRY" 
    from 
      "SYSTEM"."CUSTOMERDETAIL" 
    where 
      (
        "SYSTEM"."CUSTOMERDETAIL"."CITY", 
        "SYSTEM"."CUSTOMERDETAIL"."COUNTRY"
      ) in (
        (
          select 
            "SYSTEM"."OFFICE"."CITY", 
            "SYSTEM"."OFFICE"."COUNTRY" 
          from 
            "SYSTEM"."OFFICE"
        )
      )    
    */
    public void findProductsByVendorScaleAndProductLineInSelect() {

        // row value expression
        Condition condition = row(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                .in(select(OFFICE.CITY, OFFICE.COUNTRY).from(OFFICE));

        // SQL query using the above row value expression
        System.out.println("EXAMPLE 5\n" +
                ctx.selectFrom(CUSTOMERDETAIL)
                        .where(condition)
                        .fetch()
        );
    }

    // EXAMPLE 6: BETWEEN predicates
    /*
    select 
      "SYSTEM"."ORDER"."ORDER_ID", 
      "SYSTEM"."ORDER"."ORDER_DATE", 
      "SYSTEM"."ORDER"."REQUIRED_DATE", 
      "SYSTEM"."ORDER"."SHIPPED_DATE", 
      "SYSTEM"."ORDER"."STATUS", 
      "SYSTEM"."ORDER"."COMMENTS", 
      "SYSTEM"."ORDER"."CUSTOMER_NUMBER" 
    from 
      "SYSTEM"."ORDER" 
    where 
      (
        (
          "SYSTEM"."ORDER"."ORDER_DATE" >= cast(? as date) 
          and (
            "SYSTEM"."ORDER"."ORDER_DATE" > cast(? as date) 
            or (
              "SYSTEM"."ORDER"."ORDER_DATE" = cast(? as date) 
              and "SYSTEM"."ORDER"."SHIPPED_DATE" >= cast(? as date)
            )
          )
        ) 
        and (
          "SYSTEM"."ORDER"."ORDER_DATE" <= cast(? as date) 
          and (
            "SYSTEM"."ORDER"."ORDER_DATE" < cast(? as date) 
            or (
              "SYSTEM"."ORDER"."ORDER_DATE" = cast(? as date) 
              and "SYSTEM"."ORDER"."SHIPPED_DATE" <= cast(? as date)
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
     "SYSTEM"."ORDER"."ORDER_ID", 
     "SYSTEM"."ORDER"."ORDER_DATE", 
     "SYSTEM"."ORDER"."REQUIRED_DATE", 
     "SYSTEM"."ORDER"."SHIPPED_DATE", 
     "SYSTEM"."ORDER"."STATUS", 
     "SYSTEM"."ORDER"."COMMENTS", 
     "SYSTEM"."ORDER"."CUSTOMER_NUMBER" 
   from 
     "SYSTEM"."ORDER" 
   where 
     (
       (
         "SYSTEM"."ORDER"."ORDER_DATE", "SYSTEM"."ORDER"."SHIPPED_DATE"
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
      "SYSTEM"."SALE"."SALE_ID", 
      "SYSTEM"."SALE"."FISCAL_YEAR", 
      "SYSTEM"."SALE"."SALE", 
      "SYSTEM"."SALE"."EMPLOYEE_NUMBER" 
    from 
      "SYSTEM"."SALE" 
    where 
      (
        ? <= "SYSTEM"."SALE"."SALE" 
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
      (
        "SYSTEM"."OFFICE"."CITY" is null 
        and "SYSTEM"."OFFICE"."COUNTRY" is null
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
      "SYSTEM"."OFFICE" 
    set 
      ("POSTAL_CODE", "STATE") = (
        select 
          "SYSTEM"."CUSTOMERDETAIL"."POSTAL_CODE", 
          "SYSTEM"."CUSTOMERDETAIL"."STATE" 
        from 
          "SYSTEM"."CUSTOMERDETAIL" 
        where 
          "SYSTEM"."OFFICE"."CITY" = "SYSTEM"."CUSTOMERDETAIL"."CITY" fetch next ? rows only
      ) 
    where 
      "SYSTEM"."OFFICE"."OFFICE_CODE" = ?    
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