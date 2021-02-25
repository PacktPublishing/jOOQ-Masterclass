package com.classicmodels.repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.LoaderError;
import org.jooq.Query;
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
    public void loadJSONDefaults() {

        // import a JSON having
        //     - "fields" (contains header information as exported by jOOQ)
        //     - all columns of 'sale' table        
        try {
            ctx.loadInto(SALE)
                    // .onDuplicateKeyError()   - default
                    // .onErrorAbort()          - default
                    // .bulkNone()              - default
                    // .batchNone()             - default
                    // .commitNone()            - default
                    .loadJSON(Paths.get("data", "json", "jsonWithFields.json").toFile(), StandardCharsets.UTF_8)
                    .fieldsCorresponding()
                    .execute();

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadJSONOnlyCertainFields() {

        // import a JSON having
        //     - no "fields" header
        //     - all columns of 'sale' table                        
        try {
            int processed = ctx.loadInto(SALE)
                    .loadJSON(Paths.get("data", "json", "jsonWithoutFields.json").toFile(), StandardCharsets.UTF_8)
                    .fields(null, SALE.FISCAL_YEAR, SALE.SALE_, null, null, null, null, SALE.TREND)
                    .execute()
                    .processed(); // optional

            System.out.println("Processed: " + processed);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadJSONInTwoTables() {

        // import a JSON having
        //     - "fields" (contains header information as exported by jOOQ)
        //     - all columns of 'customer' and 'customerdetail' table
        try {
            int customerProcessed = ctx.loadInto(CUSTOMER)
                    .onDuplicateKeyIgnore()
                    .loadJSON(Paths.get("data", "json", "jsonTwoTablesFields.json").toFile(), StandardCharsets.UTF_8)
                    .fields(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_LAST_NAME,
                            CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.PHONE, CUSTOMER.SALES_REP_EMPLOYEE_NUMBER,
                            CUSTOMER.CREDIT_LIMIT, CUSTOMER.FIRST_BUY_DATE)
                    .execute()
                    .processed(); // optional

            System.out.println("Customer processed: " + customerProcessed);

            int customerdetailProcessed = ctx.loadInto(CUSTOMERDETAIL)
                    .onDuplicateKeyIgnore()
                    .loadJSON(Paths.get("data", "json", "jsonTwoTablesFields.json").toFile(), StandardCharsets.UTF_8)
                    .fields(null, null, null, null, null, null, null, null,
                            CUSTOMERDETAIL.CUSTOMER_NUMBER, CUSTOMERDETAIL.ADDRESS_LINE_FIRST,
                            CUSTOMERDETAIL.ADDRESS_LINE_SECOND, CUSTOMERDETAIL.CITY,
                            CUSTOMERDETAIL.STATE, CUSTOMERDETAIL.POSTAL_CODE, CUSTOMERDETAIL.COUNTRY)
                    .execute()
                    .processed(); // optional

            System.out.println("Customerdetail processed: " + customerdetailProcessed);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    @Transactional
    public void loadJSONRowListeners() {

        // import a JSON having
        //     - "fields" (contains header information as exported by jOOQ)
        //     - all columns of 'sale' table                        
        try {
            ctx.loadInto(SALE)
                    .loadJSON(Paths.get("data", "json", "jsonWithFields.json").toFile(), StandardCharsets.UTF_8)
                    .fields(null, SALE.FISCAL_YEAR, SALE.SALE_, null, null, null, null, SALE.TREND)
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
    public void loadJSONOnDuplicateKeyUpdate() {

        // import a JSON having
        //     - "fields" (contains header information as exported by jOOQ)
        //     - all columns of 'sale' table         
        //     - duplicated IDs
        try {
            int executed = ctx.loadInto(SALE)
                    .onDuplicateKeyUpdate() // bulk cannot be used                                                          
                    .batchAfter(2) // each *batch* has 2 rows
                    .commitNone()  // (default) allow Spring Boot to handle transaction commit
                                   // if you remove @Transactional then auto-commit (see, application.properties) takes action
                                   // if you remove @Transactional and set auto-commit to false then nothing commits
                    .loadJSON(Paths.get("data", "json", "jsonWithFieldsAndDuplicates.json").toFile(), StandardCharsets.UTF_8)
                    .fieldsCorresponding()
                    .execute()
                    .executed();

            System.out.println("Executed: " + executed);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }

    @Transactional
    public void loadJSONOnDuplicateKeyIgnore() {

        // import a JSON having
        //     - "fields" (contains header information as exported by jOOQ)
        //     - all columns of 'sale' table         
        //     - duplicated IDs
        try {
            int ignored = ctx.loadInto(SALE)
                    .onDuplicateKeyIgnore() // bulk cannot be used                  
                    .batchAfter(5) // each *batch* has 5 rows
                    .commitAll() // commit all batches at once
                    .loadJSON(Paths.get("data", "json", "jsonWithFieldsAndDuplicates.json").toFile(), StandardCharsets.UTF_8)
                    .fieldsCorresponding()
                    .execute()
                    .ignored();

            System.out.println("Ignored:" + ignored);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadJSONBulkBatchCommit() {
        
        // import a JSON having
        //     - "fields" (contains header information as exported by jOOQ)
        //     - all columns of 'sale' table                                      
        try {
            int inserted = ctx.loadInto(SALE)
                    .bulkAfter(2) // each *bulk* has 2 rows
                    .batchAfter(3) // each *batch* has 3 *bulks*, so 6 rows
                    .commitAfter(3) // commit after 3 *batches*, so after 9 *bulks*, so after 18 rows
                    .loadJSON(Paths.get("data", "json", "jsonWithFields.json").toFile(), StandardCharsets.UTF_8)
                    .fieldsCorresponding()
                    .execute()
                    .stored();

            System.out.println("Inserted: " + inserted);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadJSONonDuplicateKeyError() {
        
        // import a JSON having
        //     - "fields" (contains header information as exported by jOOQ)
        //     - all columns of 'sale' table                                      
        //     - duplicated IDs
        try {
            ctx.loadInto(SALE)
                    .onDuplicateKeyError()
                    .bulkAfter(2) // each *bulk* has 2 rows                           
                    .batchAfter(2) // each *batch* has 2 "bulks", so has 4 rows
                    .loadJSON(Paths.get("data", "json", "jsonWithFieldsAndDuplicates.json").toFile(), StandardCharsets.UTF_8)
                    .fieldsCorresponding()
                    .execute();

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadJSONonErrorAbort() {

        // import a JSON having
        //     - "fields" (contains header information as exported by jOOQ)
        //     - all columns of 'sale' table                                              
        //     - corrupted data
        try {

            List<LoaderError> errors = ctx.loadInto(SALE)                    
                    .bulkNone() // dont' bulk (default)
                    .batchNone() // don't batch (default)
                    .onErrorAbort() // or, continue via onErrorIgnore()
                    .loadJSON(Paths.get("data", "json", "jsonWithFieldsAndCorruptedData.json").toFile(), StandardCharsets.UTF_8)
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

    @Transactional
    public void cleanUpSaleTable() {

        // clean up the 'sale' table (this step is not part of loading, is just for 
        // helping us to easily track what was loaded)
        ctx.deleteFrom(SALE).execute();
    }
}