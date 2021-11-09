package com.classicmodels.providers;

import org.jooq.Transaction;
import org.springframework.transaction.TransactionStatus;

class SpringTransaction implements Transaction {

    private final TransactionStatus transactionStatus;

    SpringTransaction(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    TransactionStatus getTxStatus() {
        return this.transactionStatus;
    }

}
