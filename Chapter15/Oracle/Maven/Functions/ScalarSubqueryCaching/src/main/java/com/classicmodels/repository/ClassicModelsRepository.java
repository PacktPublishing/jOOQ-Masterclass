package com.classicmodels.repository;

import java.math.BigDecimal;
import java.util.List;
import static jooq.generated.Routines.cardCommission;
import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import org.jooq.DSLContext;
import org.jooq.conf.Settings;
import static org.jooq.impl.DSL.select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void callCardCommissionFunc() {

        // select "CLASSICMODELS"."CARD_COMMISSION"("CLASSICMODELS"."BANK_TRANSACTION"."CARD_TYPE") from "CLASSICMODELS"."BANK_TRANSACTION"
        ctx.select(cardCommission(BANK_TRANSACTION.CARD_TYPE))
                .from(BANK_TRANSACTION)
                .fetch();
        
        List<BigDecimal> commission = ctx.fetchValues(select(cardCommission(BANK_TRANSACTION.CARD_TYPE))
                                   .from(BANK_TRANSACTION));
        System.out.println("Commission: " + commission);

        // select (select "CLASSICMODELS"."CARD_COMMISSION"("CLASSICMODELS"."BANK_TRANSACTION"."CARD_TYPE") from DUAL) from "CLASSICMODELS"."BANK_TRANSACTION"        
        ctx.configuration().derive(new Settings()
                .withRenderScalarSubqueriesForStoredFunctions(true))
                .dsl()
                .select(cardCommission(BANK_TRANSACTION.CARD_TYPE))
                .from(BANK_TRANSACTION)
                .fetch();
    }

}
