package com.classicmodels.providers;

import org.jooq.TransactionContext;
import org.jooq.TransactionProvider;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class MyTransactionProvider implements TransactionProvider {
    
    private final PlatformTransactionManager transactionManager;
    
    public MyTransactionProvider(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    
    @Override
    public void begin(TransactionContext context) {
        
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        definition.setName("TRANSACTION_" + Math.round(1000));
        definition.setReadOnly(true);
        definition.setTimeout(1);
        
        TransactionStatus status = this.transactionManager.getTransaction(definition);
        context.transaction(new SpringTransaction(status));
    }
    
    @Override
    public void commit(TransactionContext ctx) {
        this.transactionManager.commit(getTransactionStatus(ctx));
    }
    
    @Override
    public void rollback(TransactionContext ctx) {
        this.transactionManager.rollback(getTransactionStatus(ctx));
    }
    
    private TransactionStatus getTransactionStatus(TransactionContext ctx) {
        SpringTransaction transaction = (SpringTransaction) ctx.transaction();        
        return transaction.getTxStatus();
    }
   
}