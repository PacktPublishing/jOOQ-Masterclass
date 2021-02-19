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
                    .fields(null, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.HOT, SALE.RATE, SALE.VAT, SALE.TREND)
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
                    .fields(null, SALE.FISCAL_YEAR, SALE.SALE_, null, null, null, null, SALE.TREND)
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
                            new Object[]{111100, "Joliyon", "Schmitt,Rue", "10.22.2535", 1370, 21000.00, 20201, 111100, "51, Avenue 3", "", "", "", 43000, ""},
                            new Object[]{111101, "Marquez Xioa", "Calor", "Sar", "11.12.2525", 1370, 21000.00, 21805, 111101, "51, St 5", "", "", "", 44000, "USA"}
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
                            new Object[]{111100, "Joliyon", "Schmitt,Rue", "10.22.2535", 1370, 21000.00, 20201, 111100, "51, Avenue 3", "", "", "", 43000, ""},
                            new Object[]{111101, "Marquez Xioa", "Calor", "Sar", "11.12.2525", 1370, 21000.00, 21805, 111101, "51, St 5", "", "", "", 44000, "USA"}
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
                            new Object[]{1, 2003, 5282.64, 1370, 0, "", "", "UP"},
                            new Object[]{2, 2004, 1938.24, 1370, 0, "", "", "UP"},
                            new Object[]{3, 2004, 1676.14, 1370, 0, "", "", "DOWN"}
                    )
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
    public void loadArraysOnDuplicateKeyUpdate() {
       
        try {
            int executed = ctx.loadInto(SALE)
                    .onDuplicateKeyUpdate() // bulk cannot be used                                                          
                    .batchAfter(2) // each *batch* has 2 rows
                    .commitNone()  // (default) allow Spring Boot to handle transaction commit
                                   // if you remove @Transactional then auto-commit (see, application.properties) takes action
                                   // if you remove @Transactional and set auto-commit to false then nothing commits
                    .loadArrays(
                            new Object[]{1, 2005, 582.64, 1370, 0, "", "", "UP"},
                            new Object[]{2, 2005, 138.24, 1370, 0, "", "", " CONSTANT"},
                            new Object[]{3, 2005, 176.14, 1370, 0, "", "", "DOWN"}
                    )
                    .fields(SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.HOT, SALE.RATE, SALE.VAT, SALE.TREND)
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
                    .commitAll() // commit all batches at once
                    .loadArrays(
                            new Object[]{1, 2005, 582.64, 1370, 0, "", "", "UP"},
                            new Object[]{2, 2005, 138.24, 1370, 0, "", "", " CONSTANT"},
                            new Object[]{3, 2005, 176.14, 1370, 0, "", "", "DOWN"}
                    )
                    .fields(SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.HOT, SALE.RATE, SALE.VAT, SALE.TREND)
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
                            new Object[]{2005, 582.64, 1370, 0, "", "", "UP"},
                            new Object[]{2005, 138.24, 1370, 0, "", "", " CONSTANT"},
                            new Object[]{2005, 176.14, 1370, 0, "", "", "DOWN"},
                            new Object[]{2003, 2582.64, 1504, 0, "", "", "DOWN"},
                            new Object[]{2005, 1338.21, 1370, 0, "", "", " CONSTANT"},
                            new Object[]{2005, 1746.12, 1504, 0, "", "", "DOWN"},
                            new Object[]{2005, 6426.12, 1504, 0, "", "", "DOWN"},
                            new Object[]{2004, 2746.12, 1504, 0, "", "", "DOWN"},
                            new Object[]{2003, 1336.12, 1166, 0, "", "", "DOWN"},
                            new Object[]{2005, 646.12, 1166, 0, "", "", "DOWN"},
                            new Object[]{2003, 1746.12, 1504, 0, "", "", "DOWN"},
                            new Object[]{2005, 582.64, 1370, 0, "", "", "UP"},
                            new Object[]{2005, 138.24, 1370, 0, "", "", " CONSTANT"},
                            new Object[]{2005, 176.14, 1370, 0, "", "", "DOWN"},
                            new Object[]{2003, 2582.64, 1504, 0, "", "", "DOWN"},
                            new Object[]{2005, 1338.21, 1370, 0, "", "", " CONSTANT"},
                            new Object[]{2005, 1746.12, 1504, 0, "", "", "DOWN"},
                            new Object[]{2005, 6426.12, 1504, 0, "", "", "DOWN"},
                            new Object[]{2004, 2746.12, 1504, 0, "", "", "DOWN"},
                            new Object[]{2003, 1336.12, 1166, 0, "", "", "DOWN"},
                            new Object[]{2005, 646.12, 1166, 0, "", "", "DOWN"},
                            new Object[]{2003, 1746.12, 1504, 0, "", "", "DOWN"}
                    )
                    .fields(SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.HOT, SALE.RATE, SALE.VAT, SALE.TREND)                    
                    .execute()
                    .stored();

            System.out.println("Inserted: " + inserted);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadArraysonDuplicateKeyError() {

        try {
            ctx.loadInto(SALE)
                    .onDuplicateKeyError()
                    .bulkAfter(1) // each *bulk* has 1 rows                           
                    .batchAfter(2) // each *batch* has 2 "bulks", so has 4 rows
                    .loadArrays(
                            new Object[]{1, 2005, 582.64, 1370, 0, "", "", "UP"},
                            new Object[]{2, 2005, 138.24, 1370, 0, "", "", " CONSTANT"},
                            new Object[]{3, 2005, 176.14, 1370, 0, "", "", "DOWN"}
                    )
                    .fields(SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.HOT, SALE.RATE, SALE.VAT, SALE.TREND)                    
                    .execute();

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void loadArraysonErrorAbort() {
       
        try {

            List<LoaderError> errors = ctx.loadInto(SALE)
                    .onErrorAbort() // or, continue via onErrorIgnore()
                    .bulkNone() // dont' bulk (default)
                    .batchNone() // don't batch (default)                  
                    .loadArrays(
                            new Object[]{1},
                            new Object[]{0, "", "", " CONSTANT"},
                            new Object[]{3, 2005, 176.14, 1370, 0, "", "", "DOWN"}
                    )
                    .fields(SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.HOT, SALE.RATE, SALE.VAT, SALE.TREND)                    
                    .execute()
                    .errors();

            System.out.println("Errors: " + errors);

        } catch (IOException ex) {
            Logger.getLogger(ClassicModelsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
