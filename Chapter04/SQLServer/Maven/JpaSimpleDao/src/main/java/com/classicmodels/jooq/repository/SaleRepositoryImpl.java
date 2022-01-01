package com.classicmodels.jooq.repository;

import java.util.List;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.pojos.Sale;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class SaleRepositoryImpl implements SaleRepository {

    private final DSLContext ctx;

    public SaleRepositoryImpl(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public List<Sale> findSaleByFiscalYear(int year) {

        return ctx.selectFrom(SALE)
                .where(SALE.FISCAL_YEAR.eq(year))
                .fetchInto(Sale.class);
    }

    @Override
    public List<Sale> findSaleAscGtLimit(double limit) {

        return ctx.selectFrom(SALE)
                .where(SALE.SALE_.ge(limit))
                .orderBy(SALE.SALE_)
                .fetchInto(Sale.class);
    }

}
