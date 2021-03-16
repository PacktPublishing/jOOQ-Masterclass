package com.classicmodels.repository;

import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import jooq.generated.tables.records.BankTransactionRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // load payments of customer identified as 333/NF959653
    public Result<BankTransactionRecord> fetchAllBankTransactionOfCertainPayment() {

        return ctx.selectFrom(BANK_TRANSACTION)
                .where(BANK_TRANSACTION.CUSTOMER_NUMBER.eq(333L)
                        .and(BANK_TRANSACTION.CHECK_NUMBER.eq("NF959653")))
                .fetch();
    }

    public BankTransactionRecord fetchBankTransaction(Long id) {

        return ctx.selectFrom(BANK_TRANSACTION)
                .where(BANK_TRANSACTION.TRANSACTION_ID.eq(id))
                .fetchSingle();
    }

    @Transactional
    public int newBankTransaction(BankTransactionRecord btr) {

        ctx.attach(btr); //or, like this btr.attach(ctx.configuration());
        
        return btr.insert();
    }

    @Transactional
    public int updateBankTransaction(BankTransactionRecord btr) {

        return btr.update();
    }

    @Transactional
    public int deleteBankTransaction(BankTransactionRecord btr) {

        return btr.delete();
    }
}
