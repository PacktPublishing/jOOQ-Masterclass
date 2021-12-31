package com.classicmodels.repository;

import java.util.List;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.daos.SaleRepository;
import jooq.generated.tables.pojos.Sale;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class SaleRepositoryImpl extends SaleRepository {

    private final DSLContext ctx;

    public SaleRepositoryImpl(DSLContext ctx) {
        super(ctx.configuration());
        this.ctx = ctx;
    }

    public List<Sale> findSaleAscGtLimit(double limit) {

        return ctx.selectFrom(SALE)
                .where(SALE.SALE_.ge(limit))
                .orderBy(SALE.SALE_)
                .fetchInto(Sale.class);
    }

}
