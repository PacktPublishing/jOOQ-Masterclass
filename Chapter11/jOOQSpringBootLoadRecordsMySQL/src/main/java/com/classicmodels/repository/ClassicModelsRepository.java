package com.classicmodels.repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.CustomerRecord;
import jooq.generated.tables.records.CustomerdetailRecord;
import jooq.generated.tables.records.SaleRecord;
import org.jooq.DSLContext;
import org.jooq.LoaderError;
import org.jooq.Query;
import org.jooq.Result;
import org.jooq.Record;
import org.jooq.Record3;
import org.jooq.exception.DataAccessException;
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
    public void loadRecordsDefaults() {

        Result<SaleRecord> result1 = ctx.selectFrom(SALE)
                .fetch();

        try {
            ctx.loadInto(SALE)
                    // .onDuplicateKeyError()   - default
                    // .onErrorAbort()          - default
                    // .bulkNone()              - default
                    // .batchNone()             - default
                    // .commitNone()            - default
                    .loadRecords(result1)
                    .fields(null, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.HOT, SALE.RATE, SALE.VAT, SALE.TREND)
                    .execute();

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Result<Record> result2 = ctx.select(SALE.asterisk().except(SALE.SALE_ID))
                .from(SALE)
                .fetch();
        
        try {
            ctx.loadInto(SALE)
                    // .onDuplicateKeyError()   - default
                    // .onErrorAbort()          - default
                    // .bulkNone()              - default
                    // .batchNone()             - default
                    // .commitNone()            - default
                    .loadRecords(result2)
                    .fieldsCorresponding()                    
                    .execute();

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Result<Record3<Integer, Double, String>> result3 = ctx.select(SALE.FISCAL_YEAR, SALE.SALE_, SALE.TREND)
                .from(SALE)
                .fetch();
        
        try {
            ctx.loadInto(SALE)
                    // .onDuplicateKeyError()   - default
                    // .onErrorAbort()          - default
                    // .bulkNone()              - default
                    // .batchNone()             - default
                    // .commitNone()            - default
                    .loadRecords(result3)
                    .fieldsCorresponding()                    
                    .execute();

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadRecordsOnlyCertainFields() {
        
        try {
            int processed = ctx.loadInto(SALE)
                    .loadRecords(ctx.selectFrom(SALE).fetchStream())
                    .fields(null, SALE.FISCAL_YEAR, SALE.SALE_, null, null, null, null, SALE.TREND)
                    .execute()
                    .processed(); // optional

            System.out.println("Processed: " + processed);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadRecordsInTwoTables() {

        Map<CustomerRecord, CustomerdetailRecord> result = ctx.select()
                .from(CUSTOMER)
                .join(CUSTOMERDETAIL)
                .on(CUSTOMER.CUSTOMER_NUMBER.eq(CUSTOMERDETAIL.CUSTOMER_NUMBER))
                .fetchMap(CUSTOMER, CUSTOMERDETAIL);
        
        try {
            int customerProcessed = ctx.loadInto(CUSTOMER)
                    .onDuplicateKeyIgnore()
                    .loadRecords(result.keySet())
                    .fieldsCorresponding()                    
                    .execute()
                    .processed(); // optional

            System.out.println("Customer processed: " + customerProcessed);

            int customerdetailProcessed = ctx.loadInto(CUSTOMERDETAIL)
                    .onDuplicateKeyIgnore()
                    .loadRecords(result.values())
                    .fieldsCorresponding()                    
                    .execute()
                    .processed(); // optional

            System.out.println("Customerdetail processed: " + customerdetailProcessed);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadRecordsRowListeners() {

        Record3<Integer, Double, String>[] result = ctx.select(SALE.FISCAL_YEAR, SALE.SALE_, SALE.TREND)
                .from(SALE)
                .fetchArray();        
        
        try {
            ctx.loadInto(SALE)
                    .loadRecords(result)
                    .fieldsCorresponding()
                    .onRowEnd(ll -> {
                        System.out.println("Just processed row: " + Arrays.toString(ll.row()));
                        System.out.format("Executed: %d, ignored: %d, processed: %d, stored: %d\n",
                                ll.executed(), ll.ignored(), ll.processed(), ll.stored());
                    })
                    .execute();

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadRecordsOnDuplicateKeyUpdate() {
       
        SaleRecord r1 = new SaleRecord(1L, 2005, 582.64, 1370L, null, null, null, "UP");
        SaleRecord r2 = new SaleRecord(2L, 2005, 138.24, 1370L, null, null, null, "CONSTANT");
        SaleRecord r3 = new SaleRecord(3L, 2005, 176.14, 1370L, null, null, null, "DOWN");
        
        try {
            int executed = ctx.loadInto(SALE)
                    .onDuplicateKeyUpdate() // bulk cannot be used                                                          
                    .batchAfter(2) // each *batch* has 2 rows
                    .commitNone()  // (default) allow Spring Boot to handle transaction commit
                                   // if you remove @Transactional then auto-commit (see, application.properties) takes action
                                   // if you remove @Transactional and set auto-commit to false then nothing commits
                    .loadRecords(r1, r2, r3)
                    .fieldsCorresponding()
                    .execute()
                    .executed();

            System.out.println("Executed: " + executed);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadRecordsOnDuplicateKeyIgnore() {
        
        SaleRecord r1 = new SaleRecord(1L, 2004, 33582.64, 1370L, null, null, null, "UP");
        SaleRecord r2 = new SaleRecord(2L, 2004, 2138.24, 1504L, null, null, null, "UP");
        SaleRecord r3 = new SaleRecord(3L, 2003, 1746.14, 1370L, null, null, null, "DOWN");
        
        try {
            int ignored = ctx.loadInto(SALE)
                    .onDuplicateKeyIgnore() // bulk cannot be used                  
                    .batchAfter(2) // each *batch* has 2 rows
                    .commitAll() // commit all batches at once
                    .loadRecords(r1, r2, r3)
                    .fieldsCorresponding()
                    .execute()
                    .ignored();

            System.out.println("Ignored:" + ignored);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadRecordsBulkBatchCommit() {
        
        Result<Record3<Integer, Double, String>> result = ctx.select(SALE.FISCAL_YEAR, SALE.SALE_, SALE.TREND)
                .from(SALE)
                .fetch();

        try {
            int inserted = ctx.loadInto(SALE)
                    .bulkAfter(2) // each *bulk* has 2 rows
                    .batchAfter(3) // each *batch* has 3 *bulks*, so 6 rows
                    .commitAfter(3) // commit after 3 *batches*, so after 9 *bulks*, so after 18 rows
                    .loadRecords(result)                    
                    .fieldsCorresponding()
                    .execute()
                    .stored();

            System.out.println("Inserted: " + inserted);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadRecordsOnDuplicateKeyError() {
        
        SaleRecord r1 = new SaleRecord(1L, 2004, 33582.64, 1370L, null, null, null, "UP");
        SaleRecord r2 = new SaleRecord(2L, 2004, 2138.24, 1504L, null, null, null, "UP");
        SaleRecord r3 = new SaleRecord(3L, 2003, 1746.14, 1370L, null, null, null, "DOWN");
                
        try {
            ctx.loadInto(SALE)
                    .onDuplicateKeyError()
                    .bulkAfter(1) // each *bulk* has 1 rows                           
                    .batchAfter(2) // each *batch* has 2 "bulks", so has 4 rows
                    .loadRecords(r1, r2, r3)
                    .fieldsCorresponding()
                    .execute();

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadRecordsOnErrorAbort() {
               
        SaleRecord r1 = new SaleRecord(1L, 2004, 33582.64, 1370L, null, null, null, "UP");
        SaleRecord r2 = new SaleRecord(2L, 2004, 2138.24, 1504L, null, null, null, "UP");
        SaleRecord r3 = new SaleRecord(3L, 2003, 1746.14, 1370L, null, null, null, "DOWN");
        
        try {
            List<LoaderError> errors = ctx.loadInto(SALE)                   
                    .bulkNone() // dont' bulk (default)
                    .batchNone() // don't batch (default)
                    .onErrorAbort() // or, continue via onErrorIgnore()
                    .loadRecords(r1, r2, r3)
                    .fieldsCorresponding()
                    .execute()
                    .errors();

            System.out.println("Errors: " + errors);
            
            for (LoaderError error : errors) {
                // The exception that caused the error
                DataAccessException exception = error.exception();

                // The row that caused the error
                int rowIndex = error.rowIndex();
                String[] row = error.row();

                // The query that caused the error
                Query query = error.query();

                System.out.println("ERROR: " + exception
                        + " ROW:" + rowIndex + ":(" + Arrays.toString(row) + ")"
                        + " QUERY: " + query.getSQL());
            }

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
