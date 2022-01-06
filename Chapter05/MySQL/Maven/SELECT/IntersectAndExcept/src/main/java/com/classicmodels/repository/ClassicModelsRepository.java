package com.classicmodels.repository;

import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Office.OFFICE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }
   
    /* Emulating INTERSECT(ALL)/EXCEPT(ALL) for databases that don't support them (e.g., MySQL) */                    
    
    // EXAMPLE 1
    // Emulate INTERSECT via IN (useful when no duplicates and NULLs are present)
    // Emulate INTERSECT via WHERE EXISTS (useful when duplicates and/or NULLs are present)     
    public void emulateIntersectOfficeCustomerCityAndCountry() {
        
        /*
        select 
          distinct `classicmodels`.`office`.`city`, 
          `classicmodels`.`office`.`country` 
        from 
          `classicmodels`.`office` 
        where 
          (
            `classicmodels`.`office`.`city`, 
            `classicmodels`.`office`.`country`
          ) in (
            select 
              `classicmodels`.`customerdetail`.`city`, 
              `classicmodels`.`customerdetail`.`country` 
            from 
              `classicmodels`.`customerdetail`
          )
        */
        System.out.println("EXAMPLE 1.1\n" +
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
          `classicmodels`.`office`.`city`, 
          `classicmodels`.`office`.`country` 
        from 
          `classicmodels`.`office` 
        where 
          exists (
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
                (
                  `classicmodels`.`customerdetail`.`city` = `classicmodels`.`office`.`city` 
                  or (
                    `classicmodels`.`office`.`city` is null 
                    and `classicmodels`.`customerdetail`.`city` is null
                  )
                ) 
                and (
                  `classicmodels`.`customerdetail`.`country` = `classicmodels`.`office`.`country` 
                  or (
                    `classicmodels`.`office`.`country` is null 
                    and `classicmodels`.`customerdetail`.`country` is null
                  )
                )
              )
          )        
        */
        System.out.println("EXAMPLE 1.2\n +"+
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
    
    // EXAMPLE 2
    // Emulate EXCEPT via LEFT OUTER JOIN (useful when NULLs are present)
    // Emulate EXCEPT via WHERE NOT EXISTS (useful when duplicates and/or NULLs are present)     
    public void emulateExceptOfficeCustomerCityAndCountry() {
      
        /*
        select 
          distinct `classicmodels`.`office`.`city`, 
          `classicmodels`.`office`.`country` 
        from 
          `classicmodels`.`office` 
          left outer join `classicmodels`.`customerdetail` on (
            `classicmodels`.`office`.`city` = `classicmodels`.`customerdetail`.`city` 
            and `classicmodels`.`office`.`country` = `classicmodels`.`customerdetail`.`country`
          ) 
        where 
          `classicmodels`.`customerdetail`.`city` is null        
        */
        System.out.println("EXAMPLE 2.1\n" + 
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
          `classicmodels`.`office`.`city`, 
          `classicmodels`.`office`.`country` 
        from 
          `classicmodels`.`office` 
        where 
          not exists (
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
                (
                  `classicmodels`.`customerdetail`.`city` = `classicmodels`.`office`.`city` 
                  or (
                    `classicmodels`.`office`.`city` is null 
                    and `classicmodels`.`customerdetail`.`city` is null
                  )
                ) 
                and (
                  `classicmodels`.`customerdetail`.`country` = `classicmodels`.`office`.`country` 
                  or (
                    `classicmodels`.`office`.`country` is null 
                    and `classicmodels`.`customerdetail`.`country` is null
                  )
                )
              )
          )        
        */
        System.out.println("EXAMPLE 2.2\n" +
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