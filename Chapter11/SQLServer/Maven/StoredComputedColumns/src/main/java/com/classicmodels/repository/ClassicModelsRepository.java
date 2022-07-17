package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
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
    public void remainingToTransfer() {
        
                ctx.update(BANK_TRANSACTION)
                        .set(BANK_TRANSACTION.TRANSFER_AMOUNT, BigDecimal.valueOf(50))
                        .execute();        
    }
}
