package com.classicmodels.service;

import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FirstService {

    private final DSLContext ctx;
    private final SecondService secondService;

    public FirstService(DSLContext ctx, SecondService secondService) {
        this.ctx = ctx;
        this.secondService = secondService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void insertFirstSale() {

        ctx.insertInto(SALE)
                .set(SALE.SALE_, 12222.22)
                .set(SALE.FISCAL_YEAR, 2022)
                .set(SALE.FISCAL_MONTH, 1)
                .set(SALE.REVENUE_GROWTH, 0.0)
                .execute();

        secondService.insertSecondSale();
    }
}
