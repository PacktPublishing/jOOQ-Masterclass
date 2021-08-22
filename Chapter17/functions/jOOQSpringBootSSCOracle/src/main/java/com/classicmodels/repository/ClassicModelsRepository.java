package com.classicmodels.repository;

import static jooq.generated.Routines.cardCommission;
import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import org.jooq.DSLContext;
import org.jooq.conf.Settings;
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

        // select "SYSTEM"."CARD_COMMISSION"("SYSTEM"."BANK_TRANSACTION"."CARD_TYPE") from "SYSTEM"."BANK_TRANSACTION"
        ctx.select(cardCommission(BANK_TRANSACTION.CARD_TYPE))
                .from(BANK_TRANSACTION)
                .fetch();

        // select (select "SYSTEM"."CARD_COMMISSION"("SYSTEM"."BANK_TRANSACTION"."CARD_TYPE") from DUAL) from "SYSTEM"."BANK_TRANSACTION"        
        ctx.configuration().derive(new Settings()
                .withRenderScalarSubqueriesForStoredFunctions(true))
                .dsl()
                .select(cardCommission(BANK_TRANSACTION.CARD_TYPE))
                .from(BANK_TRANSACTION)
                .fetch();
    }

}
