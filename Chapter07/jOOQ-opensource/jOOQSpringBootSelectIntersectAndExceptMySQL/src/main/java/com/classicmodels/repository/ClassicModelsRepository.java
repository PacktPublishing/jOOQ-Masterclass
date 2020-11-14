package com.classicmodels.repository;

import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Office.OFFICE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.select;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // EXAMPLE 1
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

        System.out.println(
                ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                        .from(OFFICE)
                        .intersect(select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                                .from(CUSTOMERDETAIL))
                        .orderBy(OFFICE.CITY, OFFICE.COUNTRY)
                        .fetch()
        );

        // if duplicates are needed then use intersectAll()
        /*
        System.out.println(
                ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                        .from(OFFICE)
                        .intersectAll(select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                                .from(CUSTOMERDETAIL))
                        .orderBy(OFFICE.CITY, OFFICE.COUNTRY)
                        .fetch()
        );
        */
    }

    // EXAMPLE 2
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
        
        System.out.println(
                ctx.select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                        .from(CUSTOMERDETAIL)
                        .except(select(OFFICE.CITY, OFFICE.COUNTRY)
                                .from(OFFICE))
                        .orderBy(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                        .fetch()
        );

        // if duplicates are needed then use exceptAll()
        /*
        System.out.println(
                ctx.select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                        .from(CUSTOMERDETAIL)
                        .exceptAll(select(OFFICE.CITY, OFFICE.COUNTRY)
                                .from(OFFICE))
                        .orderBy(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                        .fetch()
        );
        */
    }
}