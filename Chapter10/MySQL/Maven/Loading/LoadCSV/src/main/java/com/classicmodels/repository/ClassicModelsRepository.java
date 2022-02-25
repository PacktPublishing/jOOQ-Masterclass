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
import org.jooq.Source;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Transactional
    public void loadCSVDefaults() {

        // import a CSV having
        //     - header (first line)
        //     - all columns of 'sale' table
        //     - comma separator                
        try {
            ctx.loadInto(SALE)
                    // .onDuplicateKeyError()   - default
                    // .onErrorAbort()          - default
                    // .bulkNone()              - default
                    // .batchNone()             - default
                    // .commitNone()            - default
                    .loadCSV(Paths.get("data", "csv", "allColumnsHeaderCommaSeparator.csv").toFile(), StandardCharsets.UTF_8)
                    .fieldsCorresponding()
                    .execute();

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Transactional
    public void loadCSVDefaultsFromString() {

        // import a CSV-string having        
        //     - all columns of 'sale' table
        //     - comma separator     
        
        String strcsv = """
                        sale_id,fiscal_year,sale,employee_number,hot,rate,vat,fiscal_month,revenue_growth,trend
                        1,2003,5282.64,1370,0,"SILVER","MIN",1,0.0,UP
                        2,2004,1938.24,1370,0,"SILVER","MIN",1,0.0,UP
                        3,2004,1676.14,1370,0,"SILVER","MIN",1,0.0,DOWN
                        4,2003,3213.0,1166,0,"SILVER","MIN",1,0.0,DOWN
                        5,2004,2121.35,1166,0,"SILVER","MIN",1,0.0,DOWN                        
                        """;
       
        try {
            ctx.loadInto(SALE)                   
                    .loadCSV(strcsv)
                    .fieldsCorresponding()
                    .execute();

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        byte[] strcsvbytes = strcsv.getBytes();        
        try {
            ctx.loadInto(SALE)                 
                    .onDuplicateKeyIgnore()
                    .loadCSV(Source.of(strcsvbytes))
                    .fieldsCorresponding()
                    .execute();

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadCSVOnlyCertainFields() {

        // import a CSV having
        //     - header (first line)
        //     - all columns of 'sale' table
        //     - comma separator                
        try {
            int processed = ctx.loadInto(SALE)
                    .loadCSV(Paths.get("data", "csv", "allColumnsHeaderCommaSeparator.csv").toFile(), StandardCharsets.UTF_8)
                    .fields(null, SALE.FISCAL_YEAR, SALE.SALE_, null, null, null, null, 
                            SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH, SALE.TREND)
                    .execute()
                    .processed(); // optional

            System.out.println("Processed: " + processed);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadCSVInTwoTables() {

        // import a CSV having
        //     - header (first line)
        //     - all columns of 'customer' and 'customerdetail' table
        //     - comma separator                
        try {
            int customerProcessed = ctx.loadInto(CUSTOMER)
                    .onDuplicateKeyIgnore()
                    .loadCSV(Paths.get("data", "csv", "twoTblAllColumnsHeaderCommaSeparator.csv").toFile(), StandardCharsets.UTF_8)
                    .fields(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_LAST_NAME,
                            CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.PHONE, CUSTOMER.SALES_REP_EMPLOYEE_NUMBER,
                            CUSTOMER.CREDIT_LIMIT, CUSTOMER.FIRST_BUY_DATE)
                    .execute()
                    .processed(); // optional

            System.out.println("Customer processed: " + customerProcessed);

            int customerdetailProcessed = ctx.loadInto(CUSTOMERDETAIL)
                    .onDuplicateKeyIgnore()
                    .loadCSV(Paths.get("data", "csv", "twoTblAllColumnsHeaderCommaSeparator.csv").toFile(), StandardCharsets.UTF_8)
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
    public void loadCSVCertainSettings() {

        // import a CSV having
        //     - no header
        //     - all columns of 'sale' table
        //     - "|", separator, {null}, null string                 
        try {
            List<LoaderError> errors = ctx.loadInto(SALE)
                    .loadCSV(Paths.get("data", "csv", "allColumnsNoHeaderCertainSettings.csv").toFile(), StandardCharsets.UTF_8)
                    .fields(SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, 
                            SALE.HOT, SALE.RATE, SALE.VAT, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH, SALE.TREND)
                    .ignoreRows(0) // this is a CSV file with no header and ignoreRows() is by default 1
                    .separator('|')
                    .nullString("{null}")
                    .quote('*') // this is the default quote (")                     
                    .execute()
                    .errors();

            System.out.println("Errors: " + errors);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadCSVRowListeners() {

        // import a CSV having
        //     - header (first line)
        //     - all columns of 'sale' table
        //     - comma separator                
        try {
            ctx.loadInto(SALE)
                    .loadCSV(Paths.get("data", "csv", "allColumnsHeaderCommaSeparator.csv").toFile(), StandardCharsets.UTF_8)
                    .fields(null, SALE.FISCAL_YEAR, SALE.SALE_, null, null, null, null, 
                            SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH, SALE.TREND)
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
    public void loadCSVCommitNone() {

        // import a CSV having
        //     - header (first line)
        //     - all columns of 'sale' table
        //     - comma separator        
        //     - duplicated IDs
        try {
            int executed = ctx.loadInto(SALE)
                    .onDuplicateKeyIgnore() // for testing commit/rollback operations described below comment this line
                    .batchAfter(2) // each *batch* has 2 rows
                    .commitNone() // (default, so it can be omitted) allow Spring Boot to handle transaction commit/rollback
                   
                    // -> By default, @Transactional commits the transaction at the end of this method 
                    //   (if something goes wrong then @Transactional rollback the entire payload (all batches))
                    // -> If you remove @Transactional then auto-commit (see, application.properties) takes action 
                    //   (if something goes wrong then nothing is rolled back but if you commented onDuplicateKeyIgnore()
                    //    then loading is aborted immediately)
                    // -> If you remove @Transactional and set auto-commit to false then nothing commits
                    
                    .loadCSV(Paths.get("data", "csv", "allColumnsHeaderCommaSeparatorWithDuplicates.csv").toFile(), StandardCharsets.UTF_8)
                    .fieldsCorresponding()
                    .execute()
                    .executed();

            System.out.println("Executed: " + executed);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadCSVOnDuplicateKeyUpdate() {

        // import a CSV having
        //     - header (first line)
        //     - all columns of 'sale' table
        //     - comma separator        
        //     - duplicated IDs
        try {
            int executed = ctx.loadInto(SALE)
                    .onDuplicateKeyUpdate() 
                    .loadCSV(Paths.get("data", "csv", "allColumnsHeaderCommaSeparatorWithDuplicates.csv").toFile(), StandardCharsets.UTF_8)
                    .fieldsCorresponding()
                    .execute()
                    .executed();

            System.out.println("Executed: " + executed);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadCSVOnDuplicateKeyIgnore() {

        // import a CSV having
        //     - header (first line)
        //     - all columns of 'sale' table
        //     - comma separator                       
        //     - duplicated IDs
        try {
            int ignored = ctx.loadInto(SALE)
                    .onDuplicateKeyIgnore() // bulk cannot be used                  
                    .batchAfter(3) // each *batch* has 3 rows
                    .commitEach() // commit after each batch
                    .loadCSV(Paths.get("data", "csv", "allColumnsHeaderCommaSeparatorWithDuplicates.csv").toFile(), StandardCharsets.UTF_8)
                    .fieldsCorresponding()
                    .execute()
                    .ignored();

            System.out.println("Ignored:" + ignored);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadCSVBulkBatchCommit() {

        // import a CSV having
        //     - header (first line)
        //     - all columns of 'sale' table
        //     - comma separator                       
        try {
            int inserted = ctx.loadInto(SALE)
                    .bulkAfter(2) // each *bulk* has 2 rows
                    .batchAfter(3) // each *batch* has 3 *bulks*, so 6 rows
                    .commitAfter(3) // commit after 3 *batches*, so after 9 *bulks*, so after 18 rows
                    .loadCSV(Paths.get("data", "csv", "allColumnsHeaderCommaSeparator.csv").toFile(), StandardCharsets.UTF_8)
                    .fieldsCorresponding()
                    .execute()
                    .stored();

            System.out.println("Inserted: " + inserted);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadCSVonDuplicateKeyError() {

        // import a CSV having
        //     - header (first line)
        //     - all columns of 'sale' table
        //     - comma separator                       
        //     - duplicated IDs
        try {
            ctx.loadInto(SALE)
                    .onDuplicateKeyError()
                    .bulkAfter(2) // each *bulk* has 2 rows                           
                    .batchAfter(2) // each *batch* has 2 "bulks", so has 4 rows
                    .loadCSV(Paths.get("data", "csv", "allColumnsHeaderCommaSeparatorWithDuplicates.csv").toFile(), StandardCharsets.UTF_8)
                    .fieldsCorresponding()
                    .execute();

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadCSVonErrorAbort() {

        // import a CSV having
        //     - header (first line)
        //     - all columns of 'sale' table
        //     - comma separator                
        //     - corrupted data
        try {

            List<LoaderError> errors = ctx.loadInto(SALE)
                    .bulkNone() // dont' bulk (default)
                    .batchNone() // don't batch (default)
                    .onErrorAbort() // or, continue via onErrorIgnore()
                    .loadCSV(Paths.get("data", "csv", "allColumnsHeaderCommaSeparatorCorruptedData.csv").toFile(), StandardCharsets.UTF_8)
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
