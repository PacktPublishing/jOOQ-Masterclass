package com.classicmodels.repository;

import com.classicmodels.enums.RateType;
import java.util.List;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Transactional
    public void insertSale() {

        ctx.insertInto(table("sale"))
                .columns(field("fiscal_year"), field("sale"),
                        field("fiscal_month"), field("revenue_growth"),
                        field("employee_number"), field("rate"))
                .values(2005, 56444.32, 1, 0.0, 1370L, RateType.PLATINUM)
                .execute();
    }

    public void fetchSale() {

        List<RateType> rates = ctx.select(field("rate"))
                .from(table("sale"))
                .where(field("rate").isNotNull())
                .fetch(field("rate", RateType.class));

        System.out.println("Rates: " + rates);
    }
}
