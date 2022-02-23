package com.classicmodels.repository;

import java.util.logging.Logger;
import static jooq.generated.tables.Employee.EMPLOYEE;
import jooq.generated.tables.pojos.Sale;
import static jooq.generated.tables.Sale.SALE;
import static jooq.generated.tables.Token.TOKEN;
import jooq.generated.tables.records.EmployeeRecord;
import jooq.generated.tables.records.SaleRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Repository
public class ClassicModelsRepository {

    private static final Logger log = Logger.getLogger(ClassicModelsRepository.class.getName());

    private final DSLContext ctx;
    private final TransactionTemplate template;

    public ClassicModelsRepository(DSLContext ctx, TransactionTemplate template) {
        this.ctx = ctx;
        this.template = template;
    }
 
    // AVOID
    public void fetchWithNoTransaction() {

        // Fetching JDBC Connection from DataSource (transaction 1)       
        ctx.selectFrom(SALE).fetchAny();
        // There is no Spring transaction executed
        // At this point, the connection is back in the pool                
        
        // Fetching JDBC Connection from DataSource (transaction 2)        
        ctx.selectFrom(TOKEN).fetchAny();        
        // There is no Spring transaction executed
        // At this point, the connection is back in the pool                
    }

    // PREFER
    @Transactional(readOnly = true)
    // Starting read-only transaction and fetching JDBC Connection from DataSource
    // You cannot add a write operation in this method
    public void fetchReadOnlyTransaction() {

        // The database connection is open, so avoid time-consuming tasks 
        
        ctx.selectFrom(SALE).fetchAny();  // uses the existent connection
        ctx.selectFrom(TOKEN).fetchAny(); // uses the existent connection       

        // The database connection is still open, so avoid time-consuming tasks here      
    } // At this point, the transaction committed and the connection is back in the pool 

    // PREFER
    public void fetchReadOnlyTransactionTemplate() {

        // The transaction and the database connection is not opened so far
        
        template.setReadOnly(true);
        template.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                
                ctx.selectFrom(SALE).fetchAny();  // uses the existent connection
                ctx.selectFrom(TOKEN).fetchAny(); // uses the existent connection
            }            
        });
        
        // At this point, the transaction committed and the connection is back in the pool 
    }    
    
    // PREFER
    public void fetchJOOQTransaction() {

        // The transaction and the database connection is not opened so far
        
        ctx.transaction(configuration -> {

            // The transaction is running and the database connection is open
                        
            DSL.using(configuration).selectFrom(SALE).fetchAny();
            // or, configuration.dsl().selectFrom(SALE).fetchAny();
            DSL.using(configuration).selectFrom(TOKEN).fetchAny();

            // The transaction is running and the database connection is open
            // Implicit commit executed here
        }); // The transaction is committed

        // The database connection is closed
        
        // or, if you want to return the result then use 
        SaleRecord record = ctx.transactionResult(configuration -> {
            return DSL.using(configuration).selectFrom(SALE).fetchAny();
        });        
        
        // outside the transaction
        Sale sale = record.into(Sale.class);
    }

    // AVOID
    public void updateNoTransaction() {

        // Fetching JDBC Connection from DataSource (transaction 1)
        
        ctx.delete(SALE)
                .where(SALE.SALE_ID.eq(1L))
                .execute();

        // Fetching JDBC Connection from DataSource (transaction 2)       
        
        ctx.insertInto(TOKEN)
                .set(TOKEN.SALE_ID, 1L)
                .set(TOKEN.TOKEN_ID, 1L)
                .set(TOKEN.AMOUNT, 1000d)
                .execute();

        // The DELETE is executed successfully
        // The INSERT causes an error
        // There is no rollback since no explicit transaction was available
        // At this point, the connection is back to pool                
    }

    // PREFER
    @Transactional
    // Starting transaction and fetching JDBC Connection from DataSource
    public void updateWithTransaction() {

        // The database connection is open, so avoid time-consuming tasks here              
        
        ctx.delete(SALE)
                .where(SALE.SALE_ID.eq(2L))
                .execute();

        // The database connection is still open, so avoid time-consuming tasks here      
        
        ctx.insertInto(TOKEN)
                .set(TOKEN.SALE_ID, 2L)
                .set(TOKEN.TOKEN_ID, 4L)
                .set(TOKEN.AMOUNT, 1000d)
                .execute();

        // The DELETE is executed successfully
        // The INSERT causes an error
        // Rolling back JDBC transaction on current connection
        // At this point, the connection is back to pool and transaction was roll backed                       
    }
        
    // PREFER    
    public void updateWithTransactionTemplate() {

        // The transaction and the database connection is not opened so far
            
        template.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {

                ctx.delete(SALE)
                        .where(SALE.SALE_ID.eq(2L))
                        .execute();

                // The database connection is still open, so avoid time-consuming tasks here      
                ctx.insertInto(TOKEN)
                        .set(TOKEN.SALE_ID, 2L)
                        .set(TOKEN.TOKEN_ID, 4L)
                        .set(TOKEN.AMOUNT, 1000d)
                        .execute();
                
                // The DELETE is executed successfully
                // The INSERT causes an error
                // Rolling back JDBC transaction on current connection
            }
        });
        
        // At this point, the connection is back to pool and transaction was roll backed                       
    }

    // PREFER      
    public void updateWithJOOQTransaction() {

        // The transaction and the database connection is not opened so far
        
        ctx.transaction(configuration -> {
            DSL.using(configuration).delete(SALE)
                    .where(SALE.SALE_ID.eq(2L))
                    .execute();

            // The database connection is still open, so avoid time-consuming tasks here      
            
            DSL.using(configuration).insertInto(TOKEN)
                    .set(TOKEN.SALE_ID, 2L)
                    .set(TOKEN.TOKEN_ID, 4L)
                    .set(TOKEN.AMOUNT, 1000d)
                    .execute();
            
            // The DELETE is executed successfully
            // The INSERT causes an error
            // Rolling back JDBC transaction on current connection           
        });

        // At this point, the connection is back to pool and transaction was roll backed                       
    }       
    
    // AVOID (this can lead to a long running transaction)
    @Transactional
    public void fetchAndStreamWithTransactional() {

        ctx.update(EMPLOYEE)
                .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(1000))
                .execute();   
        
        ctx.selectFrom(EMPLOYEE)
                .fetch() // jOOQ fetches the whole result set into memory via the database connection opened by @Transactional
                .stream() // stream over the in-memory result set (database connection is active)                
                // .map(), ... time-consuming pipeline operations holds the transaction open
                .forEach(System.out::println);
    }

    // PREFER
    public void fetchAndStreamWithJOOQTransaction() {

        Result<EmployeeRecord> result = ctx.transactionResult(configuration -> {
            
            DSL.using(configuration).update(EMPLOYEE)
                .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(1000))
                .execute();   
            
            return DSL.using(configuration).selectFrom(EMPLOYEE)
                    .fetch();
        });
        
        result.stream() // stream over the in-memory result set (database connection is closed)                
                // .map(), ... more time-consuming pipeline operations, but the transaction is closed
                .forEach(System.out::println);
    }
    
    // PREFER    
    public void fetchAndStreamWithTransactionTemplate() {

        // The transaction and the database connection is not opened so far
         Result<EmployeeRecord> result = template.execute(new TransactionCallback<Result<EmployeeRecord>>() {

            @Override
            public Result<EmployeeRecord> doInTransaction(TransactionStatus ts) {

                ctx.update(EMPLOYEE)
                        .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(1000))
                        .execute();

                return ctx.selectFrom(EMPLOYEE)
                        .fetch();
            }
        });
        
        result.stream() // stream over the in-memory result set (database connection is closed)                
                // .map(), ... more time-consuming pipeline operations, but the transaction is closed
                .forEach(System.out::println);
    }            
}
