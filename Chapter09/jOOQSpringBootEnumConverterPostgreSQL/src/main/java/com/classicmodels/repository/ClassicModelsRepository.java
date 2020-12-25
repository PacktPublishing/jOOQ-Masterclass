package com.classicmodels.repository;

import java.util.List;
import jooq.generated.enums.RatingType;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
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

        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.RATING)
                .values(2005, 56444.32, 1370L, RatingType.PLATINUM)
                .execute();
    }

    public void fetchSale() {

        List<RatingType> ratings = ctx.select(SALE.RATING)
                .from(SALE)
                .where(SALE.RATING.isNotNull())
                .fetch(SALE.RATING);

        System.out.println("Rating type: " + ratings);
    }
}
