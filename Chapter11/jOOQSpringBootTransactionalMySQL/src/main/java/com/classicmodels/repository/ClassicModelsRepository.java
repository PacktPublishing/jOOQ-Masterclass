package com.classicmodels.repository;

import java.util.logging.Logger;
import jooq.generated.tables.Sale;
import static jooq.generated.tables.Sale.SALE;
import static jooq.generated.tables.Token.TOKEN;
import jooq.generated.tables.records.SaleRecord;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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
    public void fetchNoTransaction() {

        // Fetching JDBC Connection from DataSource
        
        ctx.selectFrom(SALE).fetchAny();

        // There is no Spring transaction executed
        // At this point, the connection is back in the pool                
    }

    // PREFER
    @Transactional(readOnly = true)
    // Starting read-only transaction and fetching JDBC Connection from DataSource
    // You cannot add a write operation in this method
    public void fetchReadOnlyTransaction() {

        // The database connection is open, so avoid time-consuming tasks 
        
        ctx.selectFrom(SALE).fetchAny(); // uses the existent connection

        // The database connection is still open, so avoid time-consuming tasks here      
    } // At this point, the transaction committed and the connection is back in the pool 

    // PREFER
    public void fetchJOOQTransaction() {

        // The transaction and the database connection is not opened so far
        
        ctx.transaction(configuration -> {

            // The transaction is running and the database connection is open
            
            DSL.using(configuration).selectFrom(SALE).fetchAny();

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
}