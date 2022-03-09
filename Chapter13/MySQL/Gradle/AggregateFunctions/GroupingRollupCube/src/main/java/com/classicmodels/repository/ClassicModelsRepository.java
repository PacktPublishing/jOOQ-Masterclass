package com.classicmodels.repository;

import static jooq.generated.tables.Office.OFFICE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.case_;
import static org.jooq.impl.DSL.grouping;
import static org.jooq.impl.DSL.rollup;
import static org.jooq.impl.DSL.sum;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
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
}
