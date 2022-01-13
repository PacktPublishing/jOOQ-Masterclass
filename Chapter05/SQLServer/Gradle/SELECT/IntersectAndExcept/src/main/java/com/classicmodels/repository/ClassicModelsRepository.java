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
      [classicmodels].[dbo].[product].[buy_price] 
    from 
      [classicmodels].[dbo].[product] 
    intersect 
    select 
      [classicmodels].[dbo].[orderdetail].[price_each] 
    from 
      [classicmodels].[dbo].[orderdetail]
    */
    public void intersectBuyPriceWithPriceEach() {
        
        System.out.println("EXAMPLE 1\n " + 
                ctx.select(PRODUCT.BUY_PRICE)
                        .from(PRODUCT)
                        .intersect(select(ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL))
                        .fetch()
        );                
    }
    
    // EXAMPLE 2
    /*
    select 
      [classicmodels].[dbo].[product].[buy_price] 
    from 
      [classicmodels].[dbo].[product] 
    except 
    select 
      [classicmodels].[dbo].[orderdetail].[price_each] 
    from 
      [classicmodels].[dbo].[orderdetail] 
    order by 
      [buy_price]
    */
    public void exceptBuyPriceFromPriceEach() {
        
        System.out.println("EXAMPLE 2\n" + 
                ctx.select(PRODUCT.BUY_PRICE)
                        .from(PRODUCT)
                        .except(select(ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL))
                        .orderBy(PRODUCT.BUY_PRICE)
                        .fetch()
        );        
    }
    
    // EXAMPLE 3
    /* Fetch cities and countries where we have offices and customers */    
    /*
    select 
      [classicmodels].[dbo].[office].[city], 
      [classicmodels].[dbo].[office].[country] 
    from 
      [classicmodels].[dbo].[office] 
    intersect 
    select 
      [classicmodels].[dbo].[customerdetail].[city], 
      [classicmodels].[dbo].[customerdetail].[country] 
    from 
      [classicmodels].[dbo].[customerdetail] 
    order by 
      [city], 
      [country]    
    */
    public void intersectOfficeCustomerCityAndCountry() {

        System.out.println("EXAMPLE 3\n" + 
                ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                        .from(OFFICE)
                        .intersect(select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                                .from(CUSTOMERDETAIL))
                        .orderBy(OFFICE.CITY, OFFICE.COUNTRY)
                        .fetch()
        );
    }    
                    
    // EXAMPLE 4
    /* Fetch cities and countries where we have customers but we don't have offices */
    /*
    select 
      [classicmodels].[dbo].[office].[city], 
      [classicmodels].[dbo].[office].[country] 
    from 
      [classicmodels].[dbo].[office] 
    except 
    select 
      [classicmodels].[dbo].[customerdetail].[city], 
      [classicmodels].[dbo].[customerdetail].[country] 
    from 
      [classicmodels].[dbo].[customerdetail] 
    order by 
      [city], 
      [country]    
    */
    public void exceptOfficeCustomerCityAndCountry() {
        
        System.out.println("EXAMPLE 4\n" + 
                ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                        .from(OFFICE)
                        .except(select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                                .from(CUSTOMERDETAIL))
                        .orderBy(OFFICE.CITY, OFFICE.COUNTRY)
                        .fetch()
        );
    }
    
    // EXAMPLE 5
    /*
    select 
      [p].[city], 
      [p].[country] 
    from 
      (
        values 
          (?, ?), 
          (?, ?), 
          (?, ?), 
          (?, ?), 
          (?, ?), 
          (?, ?)
      ) [p] ([city], [country]) 
    except 
    select 
      [classicmodels].[dbo].[office].[city], 
      [classicmodels].[dbo].[office].[country] 
    from 
      [classicmodels].[dbo].[office]    
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
          distinct [classicmodels].[dbo].[office].[city], 
          [classicmodels].[dbo].[office].[country] 
        from 
          [classicmodels].[dbo].[office] 
        where 
          exists (
            select 
              [alias_1].[v0], 
              [alias_1].[v1] 
            from 
              (
                select 
                  [classicmodels].[dbo].[customerdetail].[city] [v0], 
                  [classicmodels].[dbo].[customerdetail].[country] [v1] 
                from 
                  [classicmodels].[dbo].[customerdetail]
              ) [alias_1] 
            where 
              (
                [classicmodels].[dbo].[office].[city] = [alias_1].[v0] 
                and [classicmodels].[dbo].[office].[country] = [alias_1].[v1]
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
          [classicmodels].[dbo].[office].[city], 
          [classicmodels].[dbo].[office].[country] 
        from 
          [classicmodels].[dbo].[office] 
        where 
          exists (
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
              (
                (
                  [classicmodels].[dbo].[customerdetail].[city] = [classicmodels].[dbo].[office].[city] 
                  or (
                    [classicmodels].[dbo].[office].[city] is null 
                    and [classicmodels].[dbo].[customerdetail].[city] is null
                  )
                ) 
                and (
                  [classicmodels].[dbo].[customerdetail].[country] = [classicmodels].[dbo].[office].[country] 
                  or (
                    [classicmodels].[dbo].[office].[country] is null 
                    and [classicmodels].[dbo].[customerdetail].[country] is null
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
          distinct [classicmodels].[dbo].[office].[city], 
          [classicmodels].[dbo].[office].[country] 
        from 
          [classicmodels].[dbo].[office] 
          left outer join [classicmodels].[dbo].[customerdetail] on (
            [classicmodels].[dbo].[office].[city] = [classicmodels].[dbo].[customerdetail].[city] 
            and [classicmodels].[dbo].[office].[country] = [classicmodels].[dbo].[customerdetail].[country]
          ) 
        where 
          [classicmodels].[dbo].[customerdetail].[city] is null        
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
          [classicmodels].[dbo].[office].[city], 
          [classicmodels].[dbo].[office].[country] 
        from 
          [classicmodels].[dbo].[office] 
        where 
          not (
            exists (
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
                (
                  (
                    [classicmodels].[dbo].[customerdetail].[city] = [classicmodels].[dbo].[office].[city] 
                    or (
                      [classicmodels].[dbo].[office].[city] is null 
                      and [classicmodels].[dbo].[customerdetail].[city] is null
                    )
                  ) 
                  and (
                    [classicmodels].[dbo].[customerdetail].[country] = [classicmodels].[dbo].[office].[country] 
                    or (
                      [classicmodels].[dbo].[office].[country] is null 
                      and [classicmodels].[dbo].[customerdetail].[country] is null
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