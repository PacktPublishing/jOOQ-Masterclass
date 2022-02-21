package com.classicmodels.service;

import java.util.Random;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecondService {

    private final DSLContext ctx;

    public SecondService(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void insertSecondSale() {
        
        ctx.insertInto(SALE)                
                .set(SALE.SALE_, 91111.11)
                .set(SALE.FISCAL_YEAR, 2021)
                .set(SALE.FISCAL_MONTH, 1)
                .set(SALE.REVENUE_GROWTH, 0.0)
                .execute();
        
        if(new Random().nextBoolean()) {
            throw new RuntimeException("DummyException: this should cause rollback of both inserts!");
        }
    }    
}