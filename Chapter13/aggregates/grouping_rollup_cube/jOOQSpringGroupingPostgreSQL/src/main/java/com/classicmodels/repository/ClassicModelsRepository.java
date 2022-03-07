package com.classicmodels.repository;

import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.case_;
import static org.jooq.impl.DSL.castNull;
import static org.jooq.impl.DSL.cube;
import static org.jooq.impl.DSL.grouping;
import static org.jooq.impl.DSL.groupingSets;
import static org.jooq.impl.DSL.isnull;
import static org.jooq.impl.DSL.rollup;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.INTEGER;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void groupingEmployeeNumberFiscalYear() {

        // the following two queries uses two grouping sets aggregated in the third query        
        ctx.select(SALE.EMPLOYEE_NUMBER, sum(SALE.SALE_))
                .from(SALE)
                .groupBy(SALE.EMPLOYEE_NUMBER)
                .fetch();

        ctx.select(SALE.FISCAL_YEAR, sum(SALE.SALE_))
                .from(SALE)
                .groupBy(SALE.FISCAL_YEAR)
                .fetch();

        // aggregated data for the previous two grouping sets         
        ctx.select(SALE.EMPLOYEE_NUMBER, castNull(INTEGER).as("fiscal_year"), sum(SALE.SALE_))
                .from(SALE)
                .groupBy(SALE.EMPLOYEE_NUMBER)
                .unionAll(select(castNull(BIGINT).as("employee_number"), SALE.FISCAL_YEAR, sum(SALE.SALE_))
                        .from(SALE)
                        .groupBy(SALE.FISCAL_YEAR))
                .fetch();

        // write the previous query more compact via groupingSets()
        ctx.select(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, sum(SALE.SALE_))
                .from(SALE)
                .groupBy(groupingSets(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR))
                .fetch();
    }

    public void groupingCityCountriesNull() {

        ctx.select(OFFICE.CITY, OFFICE.COUNTRY, sum(OFFICE.INTERNAL_BUDGET))
                .from(OFFICE)
                .groupBy(groupingSets(OFFICE.CITY, OFFICE.COUNTRY))
                .fetch();

        ctx.select(grouping(OFFICE.CITY).as("grouping_city"),
                OFFICE.CITY,
                grouping(OFFICE.COUNTRY).as("grouping_country"),
                OFFICE.COUNTRY,
                sum(OFFICE.INTERNAL_BUDGET))
                .from(OFFICE)
                .groupBy(groupingSets(OFFICE.CITY, OFFICE.COUNTRY))
                .fetch();

        ctx.select(case_().when(grouping(OFFICE.CITY).eq(1), "{generated}")
                .else_(OFFICE.CITY).as("city"),
                case_().when(grouping(OFFICE.COUNTRY).eq(1), "{generated}")
                        .else_(OFFICE.COUNTRY).as("country"),
                sum(OFFICE.INTERNAL_BUDGET))
                .from(OFFICE)
                .groupBy(groupingSets(OFFICE.CITY, OFFICE.COUNTRY))
                .fetch();

        ctx.select(case_().when(grouping(OFFICE.CITY).eq(1), "-")
                .else_(isnull(OFFICE.CITY, "Unspecified")).as("city"),
                case_().when(grouping(OFFICE.COUNTRY).eq(1), "-")
                        .else_(isnull(OFFICE.COUNTRY, "Unspecified")).as("country"),
                                sum(OFFICE.INTERNAL_BUDGET))
                        .from(OFFICE)
                        .groupBy(groupingSets(OFFICE.CITY, OFFICE.COUNTRY))
                        .fetch();

        ctx.select(case_().when(grouping(OFFICE.CITY).eq(1), "{generated}")
                .else_(OFFICE.CITY).as("city"),
                case_().when(grouping(OFFICE.COUNTRY).eq(1), "{generated}")
                        .else_(OFFICE.COUNTRY).as("country"),
                sum(OFFICE.INTERNAL_BUDGET))
                .from(OFFICE)
                .where(OFFICE.CITY.isNotNull().and(OFFICE.COUNTRY.isNotNull()))
                .groupBy(groupingSets(OFFICE.CITY, OFFICE.COUNTRY))
                .fetch();
    }

    public void rollupTerritoryStateCountryCity() {

        ctx.select(case_().when(grouping(OFFICE.TERRITORY).eq(1), "{generated}")
                .else_(OFFICE.TERRITORY).as("territory"),
                case_().when(grouping(OFFICE.STATE).eq(1), "{generated}")
                        .else_(OFFICE.STATE).as("state"),
                case_().when(grouping(OFFICE.COUNTRY).eq(1), "{generated}")
                        .else_(OFFICE.COUNTRY).as("country"),
                case_().when(grouping(OFFICE.CITY).eq(1), "{generated}")
                        .else_(OFFICE.CITY).as("city"),
                sum(OFFICE.INTERNAL_BUDGET))
                .from(OFFICE)
                .where(OFFICE.COUNTRY.eq("USA"))
                .groupBy(rollup(OFFICE.TERRITORY, OFFICE.STATE, OFFICE.COUNTRY, OFFICE.CITY))
                .fetch();
    }

    public void cubeStateCountryCity() {

        ctx.select(case_().when(grouping(OFFICE.STATE).eq(1), "{generated}")
                .else_(OFFICE.STATE).as("state"),
                case_().when(grouping(OFFICE.COUNTRY).eq(1), "{generated}")
                        .else_(OFFICE.COUNTRY).as("country"),
                case_().when(grouping(OFFICE.CITY).eq(1), "{generated}")
                        .else_(OFFICE.CITY).as("city"),
                sum(OFFICE.INTERNAL_BUDGET))
                .from(OFFICE)
                .where(OFFICE.COUNTRY.eq("USA"))
                .groupBy(cube(OFFICE.STATE, OFFICE.COUNTRY, OFFICE.CITY))
                .fetch();
    }
}
