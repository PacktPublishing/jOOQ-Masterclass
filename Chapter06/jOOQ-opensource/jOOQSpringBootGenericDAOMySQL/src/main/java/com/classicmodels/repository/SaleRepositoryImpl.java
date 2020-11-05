package com.classicmodels.repository;

import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.pojos.Sale;
import jooq.generated.tables.records.SaleRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class SaleRepositoryImpl
        extends ClassicModelsRepositoryImpl<SaleRecord, Sale, Long>
        implements SaleRepository {

    private final DSLContext ctx;

    public SaleRepositoryImpl(DSLContext ctx) {
        super(SALE, Sale.class, ctx);
        this.ctx = ctx;
    }
}
