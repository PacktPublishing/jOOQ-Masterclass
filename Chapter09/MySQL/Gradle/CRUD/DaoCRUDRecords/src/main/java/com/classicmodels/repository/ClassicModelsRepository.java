package com.classicmodels.repository;

import java.util.List;
import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import jooq.generated.tables.daos.BankTransactionRepository;
import jooq.generated.tables.pojos.BankTransaction;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;
    private final BankTransactionRepository bankTransactionRepository;

    public ClassicModelsRepository(DSLContext ctx, BankTransactionRepository bankTransactionRepository) {
        this.ctx = ctx;
        this.bankTransactionRepository = bankTransactionRepository;
    }

    // load payments of customer identified as 333/NF959653
    public List<BankTransaction> fetchAllBankTransactionOfCertainPayment() {

        return ctx.selectFrom(BANK_TRANSACTION)
                .where(BANK_TRANSACTION.CUSTOMER_NUMBER.eq(333L)
                        .and(BANK_TRANSACTION.CHECK_NUMBER.eq("NF959653")))
                .fetchInto(BankTransaction.class);
    }

    public BankTransaction fetchBankTransaction(Long id) {

        BankTransaction result = ctx.selectFrom(BANK_TRANSACTION)
                .where(BANK_TRANSACTION.TRANSACTION_ID.eq(id))
                .fetchSingleInto(BankTransaction.class);

        return result;
    }

    @Transactional
    public void newBankTransaction(BankTransaction ntb) {

        bankTransactionRepository.insert(ntb);
    }

    @Transactional
    public void updateBankTransaction(BankTransaction utb) {

        bankTransactionRepository.update(utb);
    }

    @Transactional
    public void deleteBankTransaction(BankTransaction dtb) {

        bankTransactionRepository.delete(dtb);
    }
}
