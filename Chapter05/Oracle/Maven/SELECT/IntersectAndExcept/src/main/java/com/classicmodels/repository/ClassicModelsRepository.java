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
      "CLASSICMODELS"."PRODUCT"."BUY_PRICE" 
    from 
      "CLASSICMODELS"."PRODUCT" 
    intersect 
    select 
      "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH" 
    from 
      "CLASSICMODELS"."ORDERDETAIL"    
    */
    public void intersectBuyPriceWithPriceEach() {
        
        System.out.println("EXAMPLE 1.1\n " + 
                ctx.select(PRODUCT.BUY_PRICE)
                        .from(PRODUCT)
                        .intersect(select(ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL))
                        .fetch()
        );
        
        // if duplicates are needed in the result set then use intersectAll()        
        // in this case, both queries produces the same result
        // AVAILABLE IN ORACLE 20C
        /*
        System.out.println("EXAMPLE 1.2\n" +
                ctx.select(PRODUCT.BUY_PRICE)
                        .from(PRODUCT)
                        .intersectAll(select(ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL))
                        .fetch()
        );
        */
    }
    
    // EXAMPLE 2
    /*
    select 
      "CLASSICMODELS"."PRODUCT"."BUY_PRICE" 
    from 
      "CLASSICMODELS"."PRODUCT" minus 
    select 
      "CLASSICMODELS"."ORDERDETAIL"."PRICE_EACH" 
    from 
      "CLASSICMODELS"."ORDERDETAIL" 
    order by 
      "BUY_PRICE"    
    */
    public void exceptBuyPriceFromPriceEach() {
        
        System.out.println("EXAMPLE 2.1\n" + 
                ctx.select(PRODUCT.BUY_PRICE)
                        .from(PRODUCT)
                        .except(select(ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL))
                        .orderBy(PRODUCT.BUY_PRICE)
                        .fetch()
        );
        
        // if duplicates are needed in the result set then use exceptAll()        
        // in this case, both queries produces the same result
        // AVAILABLE IN ORACLE 20C
        /*
        System.out.println("EXAMPLE 2.2\n" + 
                ctx.select(PRODUCT.BUY_PRICE)
                        .from(PRODUCT)
                        .exceptAll(select(ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL))
                        .orderBy(PRODUCT.BUY_PRICE)
                        .fetch()
        );
        */
    }
    
    // EXAMPLE 3
    /* Fetch cities and countries where we have offices and customers */    
    /*
    select 
      "CLASSICMODELS"."OFFICE"."CITY", 
      "CLASSICMODELS"."OFFICE"."COUNTRY" 
    from 
      "CLASSICMODELS"."OFFICE" 
    intersect 
    select 
      "CLASSICMODELS"."CUSTOMERDETAIL"."CITY", 
      "CLASSICMODELS"."CUSTOMERDETAIL"."COUNTRY" 
    from 
      "CLASSICMODELS"."CUSTOMERDETAIL" 
    order by 
      "CITY", 
      "COUNTRY"    
    */
    public void intersectOfficeCustomerCityAndCountry() {

        System.out.println("EXAMPLE 3.1\n" + 
                ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                        .from(OFFICE)
                        .intersect(select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                                .from(CUSTOMERDETAIL))
                        .orderBy(OFFICE.CITY, OFFICE.COUNTRY)
                        .fetch()
        );

        // if duplicates are needed in the result set then use intersectAll()        
        // AVAILABLE IN ORACLE 20C
        /*
        System.out.println("EXAMPLE 3.2\n" + 
                ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                        .from(OFFICE)
                        .intersectAll(select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                                .from(CUSTOMERDETAIL))
                        .orderBy(OFFICE.CITY, OFFICE.COUNTRY)
                        .fetch()
        );
        */        
    }    
                    
    // EXAMPLE 4
    /* Fetch cities and countries where we have customers but we don't have offices */
    /*
    select 
      "CLASSICMODELS"."OFFICE"."CITY", 
      "CLASSICMODELS"."OFFICE"."COUNTRY" 
    from 
      "CLASSICMODELS"."OFFICE" minus 
    select 
      "CLASSICMODELS"."CUSTOMERDETAIL"."CITY", 
      "CLASSICMODELS"."CUSTOMERDETAIL"."COUNTRY" 
    from 
      "CLASSICMODELS"."CUSTOMERDETAIL" 
    order by 
      "CITY", 
      "COUNTRY"    
    */
    public void exceptOfficeCustomerCityAndCountry() {
        
        System.out.println("EXAMPLE 4.1\n" + 
                ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                        .from(OFFICE)
                        .except(select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                                .from(CUSTOMERDETAIL))
                        .orderBy(OFFICE.CITY, OFFICE.COUNTRY)
                        .fetch()
        );

        // if duplicates are needed in the result set then use exceptAll()        
        // AVAILABLE IN ORACLE 20C
        /*
        System.out.println("EXAMPLE 4.2\n" + 
                ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                        .from(OFFICE)
                        .exceptAll(select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                                .from(CUSTOMERDETAIL))
                        .orderBy(OFFICE.CITY, OFFICE.COUNTRY)
                        .fetch()
        );
        */
    }
    
     // EXAMPLE 5
    /*
    select 
      "p"."CITY", 
      "p"."COUNTRY" 
    from 
      (
        select 
          ? "CITY", 
          ? "COUNTRY" 
        from 
          dual 
        union all 
        select 
          ?, 
          ? 
        from 
          dual 
        union all 
        select 
          ?, 
          ? 
        from 
          dual 
        union all 
        select 
          ?, 
          ? 
        from 
          dual 
        union all 
        select 
          ?, 
          ? 
        from 
          dual 
        union all 
        select 
          ?, 
          ? 
        from 
          dual
      ) "p" minus 
    select 
      "CLASSICMODELS"."OFFICE"."CITY", 
      "CLASSICMODELS"."OFFICE"."COUNTRY" 
    from 
      "CLASSICMODELS"."OFFICE"       
    */
    public void findCitiesWithNoOffices() {

        Row2[] rows = {row("Paris", "France"), row("Lion", "France"), row("Nisa", "France"),
            row("Boston", "USA"), row("Los Angeles", "USA"), row("Sydney", "Australia")};
        
        System.out.println("EXAMPLE 5\n"
                + ctx.select().from(values(rows
                ).as("p", OFFICE.CITY.getName(), OFFICE.COUNTRY.getName()))
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
        select 
          distinct "CLASSICMODELS"."OFFICE"."CITY", 
          "CLASSICMODELS"."OFFICE"."COUNTRY" 
        from 
          "CLASSICMODELS"."OFFICE" 
        where 
          (
            "CLASSICMODELS"."OFFICE"."CITY", "CLASSICMODELS"."OFFICE"."COUNTRY"
          ) in (
            (
              select 
                "CLASSICMODELS"."CUSTOMERDETAIL"."CITY", 
                "CLASSICMODELS"."CUSTOMERDETAIL"."COUNTRY" 
              from 
                "CLASSICMODELS"."CUSTOMERDETAIL"
            )
          )        
        */
        System.out.println("EXAMPLE 6.1\n" +
                //ctx.select for duplicates an no NULLs are present
                ctx.selectDistinct(OFFICE.CITY, OFFICE.COUNTRY) 
                        .from(OFFICE)
                        .where(row(OFFICE.CITY, OFFICE.COUNTRY)
                                .in(select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                                        .from(CUSTOMERDETAIL)))
                        .fetch()                                       
        );                    
        
        /*
        select 
          "CLASSICMODELS"."OFFICE"."CITY", 
          "CLASSICMODELS"."OFFICE"."COUNTRY" 
        from 
          "CLASSICMODELS"."OFFICE" 
        where 
          exists (
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
                (
                  "CLASSICMODELS"."CUSTOMERDETAIL"."CITY" = "CLASSICMODELS"."OFFICE"."CITY" 
                  or (
                    "CLASSICMODELS"."OFFICE"."CITY" is null 
                    and "CLASSICMODELS"."CUSTOMERDETAIL"."CITY" is null
                  )
                ) 
                and (
                  "CLASSICMODELS"."CUSTOMERDETAIL"."COUNTRY" = "CLASSICMODELS"."OFFICE"."COUNTRY" 
                  or (
                    "CLASSICMODELS"."OFFICE"."COUNTRY" is null 
                    and "CLASSICMODELS"."CUSTOMERDETAIL"."COUNTRY" is null
                  )
                )
              )
          )        
        */
        System.out.println("EXAMPLE 6.2\n +"+
                ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
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
          distinct "CLASSICMODELS"."OFFICE"."CITY", 
          "CLASSICMODELS"."OFFICE"."COUNTRY" 
        from 
          "CLASSICMODELS"."OFFICE" 
          left outer join "CLASSICMODELS"."CUSTOMERDETAIL" on (
            "CLASSICMODELS"."OFFICE"."CITY" = "CLASSICMODELS"."CUSTOMERDETAIL"."CITY" 
            and "CLASSICMODELS"."OFFICE"."COUNTRY" = "CLASSICMODELS"."CUSTOMERDETAIL"."COUNTRY"
          ) 
        where 
          "CLASSICMODELS"."CUSTOMERDETAIL"."CITY" is null        
        */
        System.out.println("EXAMPLE 7.1\n" + 
                //ctx.select for duplicates an no NULLs are present
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
          "CLASSICMODELS"."OFFICE"."CITY", 
          "CLASSICMODELS"."OFFICE"."COUNTRY" 
        from 
          "CLASSICMODELS"."OFFICE" 
        where 
          not (
            exists (
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
                  (
                    "CLASSICMODELS"."CUSTOMERDETAIL"."CITY" = "CLASSICMODELS"."OFFICE"."CITY" 
                    or (
                      "CLASSICMODELS"."OFFICE"."CITY" is null 
                      and "CLASSICMODELS"."CUSTOMERDETAIL"."CITY" is null
                    )
                  ) 
                  and (
                    "CLASSICMODELS"."CUSTOMERDETAIL"."COUNTRY" = "CLASSICMODELS"."OFFICE"."COUNTRY" 
                    or (
                      "CLASSICMODELS"."OFFICE"."COUNTRY" is null 
                      and "CLASSICMODELS"."CUSTOMERDETAIL"."COUNTRY" is null
                    )
                  )
                )
            )
          )       
        */
        System.out.println("EXAMPLE 7.2\n" +
                 ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
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