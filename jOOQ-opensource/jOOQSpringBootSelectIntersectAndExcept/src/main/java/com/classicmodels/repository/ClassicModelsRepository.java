package com.classicmodels.repository;

import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Office.OFFICE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.select;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext create;

    public ClassicModelsRepository(DSLContext create) {
        this.create = create;
    }

    /* Fetch cities and countries where we have offices and customers */
    public Object[][] intersectOfficeCustomerCityAndCountry() {

        Object[][] result = create.select(OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .intersect(select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                        .from(CUSTOMERDETAIL))
                .orderBy(OFFICE.CITY, OFFICE.COUNTRY)
                .fetchArrays();

        // if duplicates are needed then use intersectAll()
        /*
        Object[][] result = create.select(OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .intersectAll(select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                        .from(CUSTOMERDETAIL))
                .orderBy(OFFICE.CITY, OFFICE.COUNTRY)
                .fetchArrays();
         */
        return result;
    }

    /* Fetch cities and countries where we have customers but we don't have offices */
    public Object[][] exceptOfficeCustomerCityAndCountry() {

        Object[][] result = create.select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                .from(CUSTOMERDETAIL)
                .except(select(OFFICE.CITY, OFFICE.COUNTRY)
                        .from(OFFICE))
                .orderBy(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                .fetchArrays();

        // if duplicates are needed then use exceptAll()
        /*
        Object[][] result = create.select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                .from(CUSTOMERDETAIL)
                .exceptAll(select(OFFICE.CITY, OFFICE.COUNTRY)
                        .from(OFFICE))
                .orderBy(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                .fetchArrays();
         */
        return result;
    }
}
