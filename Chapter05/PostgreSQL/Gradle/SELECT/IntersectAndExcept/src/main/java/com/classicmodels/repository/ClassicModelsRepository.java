package com.classicmodels.repository;

import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.jooq.Row2;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.values;
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
      "public"."product"."buy_price"
    from
      "public"."product"
    intersect
      select
        "public"."orderdetail"."price_each"
      from
        "public"."orderdetail"
     */
    public void intersectBuyPriceWithPriceEach() {

        System.out.println("EXAMPLE 1.1\n "
                + ctx.select(PRODUCT.BUY_PRICE)
                        .from(PRODUCT)
                        .intersect(select(ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL))
                        .fetch()
        );

        // if duplicates are needed in the result set then use intersectAll()        
        // in this case, both queries produces the same result
        System.out.println("EXAMPLE 1.2\n"
                + ctx.select(PRODUCT.BUY_PRICE)
                        .from(PRODUCT)
                        .intersectAll(select(ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL))
                        .fetch()
        );
    }

    // EXAMPLE 2
    /*
    select
      "public"."product"."buy_price"
    from
      "public"."product"
    except
      select
        "public"."orderdetail"."price_each"
      from
        "public"."orderdetail"
    order by "buy_price"
     */
    public void exceptBuyPriceFromPriceEach() {

        System.out.println("EXAMPLE 2.1\n"
                + ctx.select(PRODUCT.BUY_PRICE)
                        .from(PRODUCT)
                        .except(select(ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL))
                        .orderBy(PRODUCT.BUY_PRICE)
                        .fetch()
        );

        // if duplicates are needed in the result set then use exceptAll()        
        // in this case, both queries produces the same result
        System.out.println("EXAMPLE 2.2\n"
                + ctx.select(PRODUCT.BUY_PRICE)
                        .from(PRODUCT)
                        .exceptAll(select(ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL))
                        .orderBy(PRODUCT.BUY_PRICE)
                        .fetch()
        );
    }

    // EXAMPLE 3
    /* Fetch cities and countries where we have offices and customers */
    /*
    select
      "public"."office"."city",
      "public"."office"."country"
    from
      "public"."office"
    intersect
      select
        "public"."customerdetail"."city",
        "public"."customerdetail"."country"
      from
        "public"."customerdetail"
      order by
        "city",
        "country"
     */
    public void intersectOfficeCustomerCityAndCountry() {

        System.out.println("EXAMPLE 3.1\n"
                + ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                        .from(OFFICE)
                        .intersect(select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                                .from(CUSTOMERDETAIL))
                        .orderBy(OFFICE.CITY, OFFICE.COUNTRY)
                        .fetch()
        );

        // if duplicates are needed in the result set then use intersectAll()        
        System.out.println("EXAMPLE 3.2\n"
                + ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                        .from(OFFICE)
                        .intersectAll(select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                                .from(CUSTOMERDETAIL))
                        .orderBy(OFFICE.CITY, OFFICE.COUNTRY)
                        .fetch()
        );
    }

    // EXAMPLE 4
    /* Fetch cities and countries where we have customers but we don't have offices */
    /*
    select
      "public"."customerdetail"."city",
      "public"."customerdetail"."country"
    from
      "public"."customerdetail"
    except
      select
        "public"."office"."city",
        "public"."office"."country"
      from
        "public"."office"
      order by
        "city",
        "country"
     */
    public void exceptOfficeCustomerCityAndCountry() {

        System.out.println("EXAMPLE 4.1\n"
                + ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                        .from(OFFICE)
                        .except(select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                                .from(CUSTOMERDETAIL))
                        .orderBy(OFFICE.CITY, OFFICE.COUNTRY)
                        .fetch()
        );

        // if duplicates are needed in the result set then use exceptAll()        
        System.out.println("EXAMPLE 4.2\n"
                + ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                        .from(OFFICE)
                        .exceptAll(select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                                .from(CUSTOMERDETAIL))
                        .orderBy(OFFICE.CITY, OFFICE.COUNTRY)
                        .fetch()
        );
    }

    // EXAMPLE 5
    /*
    select 
      "p"."city", 
      "p"."country" 
    from 
      (
        values 
          (?, ?), 
          (?, ?), 
          (?, ?), 
          (?, ?), 
          (?, ?), 
          (?, ?)
      ) as "p"("city", "country") 
    except 
    select 
      "public"."office"."city", 
      "public"."office"."country" 
    from 
      "public"."office"    
    */
    public void findCitiesWithNoOffices() {

        Row2[] rows = {row("Paris", "France"), row("Lion", "France"), row("Nisa", "France"),
            row("Boston", "USA"), row("Los Angeles", "USA"), row("Sydney", "Australia")};
        
        System.out.println("EXAMPLE 5\n"
                + ctx.select().from(values(rows)
                .as("p", OFFICE.CITY.getName(), OFFICE.COUNTRY.getName()))
                        .except(select(OFFICE.CITY, OFFICE.COUNTRY).from(OFFICE))
                        .fetch(OFFICE.CITY)
        );
    }

    /* Emulating INTERSECT(ALL)/EXCEPT(ALL) for databases that don't support them (e.g., MySQL) */
    // EXAMPLE 6    
    // Emulate INTERSECT via IN (useful when no duplicates and NULLs are present)
    // Emulate INTERSECT via WHERE EXISTS (useful when duplicates and/or NULLs are present)     
    public void emulateIntersectOfficeCustomerCityAndCountry() {

        /*
        select distinct 
          "public"."office"."city",
          "public"."office"."country"
        from
          "public"."office"
        where
        (
          "public"."office"."city",
          "public"."office"."country"
        ) in 
          (
            select
              "public"."customerdetail"."city",
              "public"."customerdetail"."country"
            from
              "public"."customerdetail"
          )
         */
        System.out.println("EXAMPLE 6.1\n"
                + //ctx.select for duplicates an no NULLs are present
                ctx.selectDistinct(OFFICE.CITY, OFFICE.COUNTRY)
                        .from(OFFICE)
                        .where(row(OFFICE.CITY, OFFICE.COUNTRY)
                                .in(select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                                        .from(CUSTOMERDETAIL)))
                        .fetch()
        );

        /*
        select
          "public"."office"."city",
          "public"."office"."country"
        from
          "public"."office"
        where
          exists (
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
                (
                  "public"."customerdetail"."city" = "public"."office"."city"
                  or (
                       "public"."office"."city" is null
                       and "public"."customerdetail"."city" is null
                  )
              )
              and (
                "public"."customerdetail"."country" = "public"."office"."country"
                or (
                      "public"."office"."country" is null
                      and "public"."customerdetail"."country" is null
                )
              )
            )
        )
         */
        System.out.println("EXAMPLE 6.2\n +"
                + ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                        .from(OFFICE)
                        .whereExists(select().from(CUSTOMERDETAIL)
                                .where(CUSTOMERDETAIL.CITY.eq(OFFICE.CITY)
                                        .or(OFFICE.CITY.isNull().and(CUSTOMERDETAIL.CITY.isNull()))
                                        .and(CUSTOMERDETAIL.COUNTRY.eq(OFFICE.COUNTRY)
                                                .or(OFFICE.COUNTRY.isNull().and(CUSTOMERDETAIL.COUNTRY.isNull())))))
                        .fetch()
        );
    }

    // EXAMPLE 7
    // Emulate EXCEPT via LEFT OUTER JOIN (useful when NULLs are present)
    // Emulate EXCEPT via WHERE NOT EXISTS (useful when duplicates and/or NULLs are present)     
    public void emulateExceptOfficeCustomerCityAndCountry() {

        /*
        select
          "public"."office"."city",
          "public"."office"."country"
        from
          "public"."office"
            left outer join "public"."customerdetail" on (
              "public"."office"."city" = "public"."customerdetail"."city"
               and "public"."office"."country" = "public"."customerdetail"."country"
            )
        where
          "public"."customerdetail"."city" is null
         */
        System.out.println("EXAMPLE 7.1\n"
                + //ctx.select for duplicates an no NULLs are present
                ctx.selectDistinct(OFFICE.CITY, OFFICE.COUNTRY)
                        .from(OFFICE)
                        .leftJoin(CUSTOMERDETAIL)
                        .on(OFFICE.CITY.eq(CUSTOMERDETAIL.CITY)
                                .and(OFFICE.COUNTRY.eq(CUSTOMERDETAIL.COUNTRY)))
                        .where(CUSTOMERDETAIL.CITY.isNull())
                        .fetch()
        );

        /*
        select
          "public"."office"."city",
          "public"."office"."country"
        from
          "public"."office"
        where
          not exists (
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
                (
                  "public"."customerdetail"."city" = "public"."office"."city"
                or (
                  "public"."office"."city" is null
                  and "public"."customerdetail"."city" is null
                )
              )   
              and (
                  "public"."customerdetail"."country" = "public"."office"."country"
                or (
                  "public"."office"."country" is null
                   and "public"."customerdetail"."country" is null
                )
              )
            )
        )
         */
        System.out.println("EXAMPLE 7.2\n"
                + ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                        .from(OFFICE)
                        .whereNotExists(select().from(CUSTOMERDETAIL)
                                .where(CUSTOMERDETAIL.CITY.eq(OFFICE.CITY)
                                        .or(OFFICE.CITY.isNull().and(CUSTOMERDETAIL.CITY.isNull()))
                                        .and(CUSTOMERDETAIL.COUNTRY.eq(OFFICE.COUNTRY)
                                                .or(OFFICE.COUNTRY.isNull().and(CUSTOMERDETAIL.COUNTRY.isNull())))))
                        .fetch()
        );
    }
}
