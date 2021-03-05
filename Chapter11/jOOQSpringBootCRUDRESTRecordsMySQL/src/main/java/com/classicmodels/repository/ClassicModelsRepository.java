package com.classicmodels.repository;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import jooq.generated.tables.daos.BankTransactionRepository;
import jooq.generated.tables.pojos.BankTransaction;
import jooq.generated.tables.records.BankTransactionRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
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
    public Result<BankTransactionRecord> fetchAllBankTransactionOfCertainPayment() {

        return ctx.selectFrom(BANK_TRANSACTION)
                .where(BANK_TRANSACTION.CUSTOMER_NUMBER.eq(333L)
                        .and(BANK_TRANSACTION.CHECK_NUMBER.eq("NF959653")))
                .fetch();
    }

    public BankTransactionRecord fetchBankTransaction(Long id) {

        BankTransactionRecord result = ctx.selectFrom(BANK_TRANSACTION)
                .where(BANK_TRANSACTION.TRANSACTION_ID.eq(id))
                .fetchOne();

        return result;
    }

    @Transactional
    public int insertOrUpdateBankTransaction(String bt) {
        
        try {
            return ctx.loadInto(BANK_TRANSACTION)
                    .onDuplicateKeyUpdate()
                    .loadJSON(bt)
                    .fields(BANK_TRANSACTION.TRANSACTION_ID, BANK_TRANSACTION.BANK_NAME,
                            BANK_TRANSACTION.BANK_IBAN, BANK_TRANSACTION.TRANSFER_AMOUNT,
                            BANK_TRANSACTION.CACHING_DATE, BANK_TRANSACTION.CUSTOMER_NUMBER,
                            BANK_TRANSACTION.CHECK_NUMBER, BANK_TRANSACTION.STATUS)
                    .execute()
                    .stored();
        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
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
