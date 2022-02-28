package com.classicmodels.repository;

import java.io.IOException;
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
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Transactional
    public void loadArraysDefaults() {

        Object[][] result = ctx.selectFrom(SALE)
                .fetchArrays();

        try {
            ctx.loadInto(SALE)
                    // .onDuplicateKeyError()   - default
                    // .onErrorAbort()          - default
                    // .bulkNone()              - default
                    // .batchNone()             - default
                    // .commitNone()            - default
                    .loadArrays(Arrays.stream(result))
                    .fields(null, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.HOT, 
                            SALE.RATE, SALE.VAT, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH, SALE.TREND)
                    .execute();

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadArraysOnlyCertainFields() {

        Object[][] result = ctx.selectFrom(SALE)
                .fetchArrays();

        try {
            int processed = ctx.loadInto(SALE)
                    .loadArrays(Arrays.asList(result))
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
    public void loadArrayInTwoTables() {

        try {
            int customerProcessed = ctx.loadInto(CUSTOMER)
                    .onDuplicateKeyIgnore()
                    .loadArrays(
                            new Object[]{11199, "Australian Home", "Paoule", "Sart", "40.11.2555", 1370, 21000.00, 20210, 11199, "43 Rue 2", "", "Paris", "", 25017, "France"},
                            new Object[]{111100, "Joliyon", "Schmitt,Rue", "10.22.2535", 1370, 21000.00, 20201, 111100, "51, Avenue 3", "SILVER", "MIN", "", 43000, ""},
                            new Object[]{111101, "Marquez Xioa", "Calor", "Sar", "11.12.2525", 1370, 21000.00, 21805, 111101, "51, St 5", "SILVER", "MIN", "", 44000, "USA"}
                    )
                    .fields(CUSTOMER.CUSTOMER_NUMBER, CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_LAST_NAME,
                            CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.PHONE, CUSTOMER.SALES_REP_EMPLOYEE_NUMBER,
                            CUSTOMER.CREDIT_LIMIT, CUSTOMER.FIRST_BUY_DATE)
                    .execute()
                    .processed(); // optional

            System.out.println("Customer processed: " + customerProcessed);

            int customerdetailProcessed = ctx.loadInto(CUSTOMERDETAIL)
                    .onDuplicateKeyIgnore()
                    .loadArrays(
                            new Object[]{11199, "Australian Home", "Paoule", "Sart", "40.11.2555", 1370, 21000.00, 20210, 11199, "43 Rue 2", "", "Paris", "", 25017, "France"},
                            new Object[]{111100, "Joliyon", "Schmitt,Rue", "10.22.2535", 1370, 21000.00, 20201, 111100, "51, Avenue 3", "SILVER", "MIN", "", 43000, ""},
                            new Object[]{111101, "Marquez Xioa", "Calor", "Sar", "11.12.2525", 1370, 21000.00, 21805, 111101, "51, St 5", "SILVER", "MIN", "", 44000, "USA"}
                    )
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
    public void loadArrayRowListeners() {

        try {
            ctx.loadInto(SALE)
                    .loadArrays(
                            new Object[]{1, 2003, 5282.64, 1370, 0, "SILVER", "MIN", 1, 0.0, "UP"},
                            new Object[]{2, 2004, 1938.24, 1370, 0, "SILVER", "MIN", 1, 0.0, "UP"},
                            new Object[]{3, 2004, 1676.14, 1370, 0, "SILVER", "MIN", 1, 0.0, "DOWN"}
                    )
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
    public void loadArraysCommitNone() {

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
                    
                    .loadArrays(
                            new Object[]{1, 2005, 582.64, 1370, 0, "SILVER", "MIN", 1, 0.0, "UP"},
                            new Object[]{2, 2005, 138.24, 1370, 0, "SILVER", "MIN", 1, 0.0, "CONSTANT"},
                            new Object[]{3, 2005, 176.14, 1370, 0, "SILVER", "MIN", 1, 0.0, "DOWN"}
                    )
                    .fields(SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, 
                            SALE.HOT, SALE.RATE, SALE.VAT, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH, SALE.TREND)
                    .execute()
                    .executed();

            System.out.println("Executed: " + executed);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Transactional
    public void loadArraysOnDuplicateKeyUpdate() {

        try {
            int executed = ctx.loadInto(SALE)
                    .onDuplicateKeyUpdate()
                    .loadArrays(
                            new Object[]{1, 2005, 582.64, 1370, 0, "SILVER", "MIN", 1, 0.0, "UP"},
                            new Object[]{2, 2005, 138.24, 1370, 0, "SILVER", "MIN", 1, 0.0, "CONSTANT"},
                            new Object[]{3, 2005, 176.14, 1370, 0, "SILVER", "MIN", 1, 0.0, "DOWN"}
                    )
                    .fields(SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, 
                            SALE.HOT, SALE.RATE, SALE.VAT, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH, SALE.TREND)
                    .execute()
                    .executed();

            System.out.println("Executed: " + executed);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadArraysOnDuplicateKeyIgnore() {

        try {
            int ignored = ctx.loadInto(SALE)
                    .onDuplicateKeyIgnore() // bulk cannot be used                  
                    .batchAfter(2) // each *batch* has 2 rows
                    .commitEach() // commit after each batch
                    .loadArrays(
                            new Object[]{1, 2005, 582.64, 1370, 0, "SILVER", "MIN", 1, 0.0, "UP"},
                            new Object[]{2, 2005, 138.24, 1370, 0, "SILVER", "MIN", 1, 0.0, "CONSTANT"},
                            new Object[]{3, 2005, 176.14, 1370, 0, "SILVER", "MIN", 1, 0.0, "DOWN"}
                    )
                    .fields(SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, 
                            SALE.HOT, SALE.RATE, SALE.VAT, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH, SALE.TREND)
                    .execute()
                    .ignored();

            System.out.println("Ignored:" + ignored);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadArraysBulkBatchCommit() {

        try {
            int inserted = ctx.loadInto(SALE)
                    .bulkAfter(2) // each *bulk* has 2 rows
                    .batchAfter(3) // each *batch* has 3 *bulks*, so 6 rows
                    .commitAfter(3) // commit after 3 *batches*, so after 9 *bulks*, so after 18 rows
                    .loadArrays(
                            new Object[]{2005, 582.64, 1370, 0, "SILVER", "MIN", 1, 0.0, "UP"},
                            new Object[]{2005, 138.24, 1370, 0, "SILVER", "MIN", 1, 0.0, "CONSTANT"},
                            new Object[]{2005, 176.14, 1370, 0, "SILVER", "MIN", 1, 0.0, "DOWN"},
                            new Object[]{2003, 2582.64, 1504, 0, "SILVER", "MIN", 1, 0.0, "DOWN"},
                            new Object[]{2005, 1338.21, 1370, 0, "SILVER", "MIN", 1, 0.0, "CONSTANT"},
                            new Object[]{2005, 1746.12, 1504, 0, "SILVER", "MIN", 1, 0.0, "DOWN"},
                            new Object[]{2005, 6426.12, 1504, 0, "SILVER", "MIN", 1, 0.0, "DOWN"},
                            new Object[]{2004, 2746.12, 1504, 0, "SILVER", "MIN", 1, 0.0, "DOWN"},
                            new Object[]{2003, 1336.12, 1166, 0, "SILVER", "MIN", 1, 0.0, "DOWN"},
                            new Object[]{2005, 646.12, 1166, 0, "SILVER", "MIN", 1, 0.0, "DOWN"},
                            new Object[]{2003, 1746.12, 1504, 0, "SILVER", "MIN", 1, 0.0, "DOWN"},
                            new Object[]{2005, 582.64, 1370, 0, "SILVER", "MIN", 1, 0.0, "UP"},
                            new Object[]{2005, 138.24, 1370, 0, "SILVER", "MIN", 1, 0.0, "CONSTANT"},
                            new Object[]{2005, 176.14, 1370, 0, "SILVER", "MIN", 1, 0.0, "DOWN"},
                            new Object[]{2003, 2582.64, 1504, 0, "SILVER", "MIN", 1, 0.0, "DOWN"},
                            new Object[]{2005, 1338.21, 1370, 0, "SILVER", "MIN", 1, 0.0, "CONSTANT"},
                            new Object[]{2005, 1746.12, 1504, 0, "SILVER", "MIN", 1, 0.0, "DOWN"},
                            new Object[]{2005, 6426.12, 1504, 0, "SILVER", "MIN", 1, 0.0, "DOWN"},
                            new Object[]{2004, 2746.12, 1504, 0, "SILVER", "MIN", 1, 0.0, "DOWN"},
                            new Object[]{2003, 1336.12, 1166, 0, "SILVER", "MIN", 1, 0.0, "DOWN"},
                            new Object[]{2005, 646.12, 1166, 0, "SILVER", "MIN", 1, 0.0, "DOWN"},
                            new Object[]{2003, 1746.12, 1504, 0, "SILVER", "MIN", 1, 0.0, "DOWN"}
                    )
                    .fields(SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.HOT, 
                            SALE.RATE, SALE.VAT, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH, SALE.TREND)
                    .execute()
                    .stored();

            System.out.println("Inserted: " + inserted);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadArraysOnDuplicateKeyError() {

        try {
            ctx.loadInto(SALE)
                    .onDuplicateKeyError()
                    .bulkAfter(1) // each *bulk* has 1 rows                           
                    .batchAfter(2) // each *batch* has 2 "bulks", so has 4 rows
                    .loadArrays(
                            new Object[]{1, 2005, 582.64, 1370, 0, "SILVER", "MIN", 1, 0.0, "UP"},
                            new Object[]{2, 2005, 138.24, 1370, 0, "SILVER", "MIN", 1, 0.0, "CONSTANT"},
                            new Object[]{3, 2005, 176.14, 1370, 0, "SILVER", "MIN", 1, 0.0, "DOWN"}
                    )
                    .fields(SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, 
                            SALE.HOT, SALE.RATE, SALE.VAT, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH, SALE.TREND)
                    .execute();

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadArraysOnErrorAbort() {

        try {

            List<LoaderError> errors = ctx.loadInto(SALE)
                    .bulkNone() // dont' bulk (default)
                    .batchNone() // don't batch (default)
                    .onErrorAbort() // or, continue via onErrorIgnore()
                    .loadArrays(
                            new Object[]{1, 1, 0.0},
                            new Object[]{0, "SILVER", "MIN", 1, 0.0, "CONSTANT"},
                            new Object[]{3, 2005, 176.14, 1370, 0, "SILVER", "MIN", 1, 0.0, "DOWN"}
                    )
                    .fields(SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, 
                            SALE.HOT, SALE.RATE, SALE.VAT, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH, SALE.TREND)
                    .execute()
                    .errors();

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

            System.out.println("Errors: " + errors);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
