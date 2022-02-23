package com.classicmodels.repository;

import static jooq.generated.tables.Sale.SALE;
import static jooq.generated.tables.Token.TOKEN;
import jooq.generated.tables.records.SaleRecord;
import jooq.generated.tables.records.TokenRecord;
import org.jooq.DSLContext;
import org.jooq.conf.Settings;
import org.jooq.conf.UpdateUnchangedRecords;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void resetOrginalChangedRefresh() {
        
        SaleRecord sr = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(1L))
                .fetchSingle();                        
        
        // modify the *sr* record in-memory
        sr.setFiscalYear(2002);
        sr.setSale(222222.22);
        System.out.println("Modified record:\n" + sr + " [" + sr.changed() + "]");
                       
        // get the *sr* before modifications (as it come from the database)
        // *sr* and *sOrg* are not the same object
        SaleRecord srOrg = sr.original();
        System.out.println("Original record:\n" + srOrg + " [" + srOrg.changed() + "]");
        int fiscalYear = sr.original(SALE.FISCAL_YEAR); // int fiscalYear = (int) sr.original("fiscal_year");
        System.out.println("Original fiscal year:\n" + fiscalYear);
                 
        // reset the record
        sr.reset(); // restore *sr* to the original content and changed to false
        System.out.println("Reseted record:\n" + sr + " [" + sr.changed() + "]");
        // reset a certain field
        sr.reset(SALE.FISCAL_YEAR); // sr.reset("fiscal_year");
        
        // refresh the record (execute a SELECT to load it from the database)
        sr.refresh();
        System.out.println("Refreshed record:\n" + sr + " [" + sr.changed() + "]");
        // refresh certain fields
        sr.refresh(SALE.FISCAL_YEAR, SALE.SALE_);  // sr.refresh("fiscal_year", "sale");
        
        // mark it as changed (all fields)
        sr.changed(true);
        System.out.println("Changed record: " + sr.changed());
        // mark certain fields
        sr.changed(SALE.FISCAL_YEAR, true); // sr.changed("fiscal_year", false);
    }
    
    @Transactional
    public void insertNewRecord() {

        SaleRecord sr = new SaleRecord();
        sr.setFiscalYear(2021);
        sr.setSale(4500.25);
        sr.setEmployeeNumber(1504L);
        sr.setFiscalMonth(1);
        sr.setRevenueGrowth(0.0);

        // Before insert (or any other DML) we attach the record to the current configuration        
        ctx.attach(sr); // or, sr.attach(ctx.configuration());
        
        sr.insert();        
        // sr.insert(SALE.FISCAL_YEAR, SALE.SALE_); // Insert a subset of fields (SALE.EMPLOYEE_NUMBER() is omitted)
        
        System.out.println("The inserted record ID: " + sr.getSaleId());
        
        // =====================================================================
        
        // We can avoid the explicit call of attach() by creating the record via DSLContext.newRecord()
        // So, more simple is to do this:
        
        SaleRecord anotherSr = ctx.newRecord(SALE);
        
        anotherSr.setFiscalYear(2021);
        anotherSr.setSale(4500.25);
        anotherSr.setEmployeeNumber(1504L);
        anotherSr.setFiscalMonth(1);
        anotherSr.setRevenueGrowth(0.0);
        
        anotherSr.insert();
        
        System.out.println("The inserted record ID: " + anotherSr.getSaleId());
                
        // =====================================================================
                
        // By default, re-inserting a record that didn't changed result in an insert that relies on the default values.
        // If your schema doesn't contain defaults for all columns then this insert may result in an error.
        // For example, re-inserting *sr* at this moment result in an error
        // sr.insert(); // leads to *Field 'fiscal_year' doesn't have a default value*                
        
        // Re-inserting the same data without creating a new record can be done via changed()                     
        sr.changed(true); // this attempts to insert the existing ID causing duplicate key
        sr.changed(SALE.SALE_ID, false); // so, mark SALE_ID as unchanged and it will be omitted from INSERT               
        sr.insert();
        System.out.println("The inserted record ID: " + sr.getSaleId());
        
        // You don't have to mark as changed all fields, you can do mark only the fields
        // that you are expecting to participate in the rendered INSERT
        sr.changed(SALE.SALE_ID, false);
        sr.changed(SALE.FISCAL_YEAR, true);
        sr.changed(SALE.SALE_, true);
        sr.changed(SALE.FISCAL_MONTH, true);        
        sr.changed(SALE.REVENUE_GROWTH, true);        
        sr.insert();
        
        System.out.println("The inserted record ID: " + sr.getSaleId());
        
        // Re-inserting the same data by creating a new record (*sr* and *srCopy* are not the same object!)
        SaleRecord srCopy1 = sr.copy();        
        srCopy1.insert();  
        
        System.out.println("The inserted srCopy1:\n" + srCopy1);
        
        // or, shortly        
        // sr.copy().insert();
        
        // A little bit more verbose
        SaleRecord srCopy2 = ctx.newRecord(SALE, sr);     
        srCopy2.changed(SALE.SALE_ID, false);
        srCopy2.insert();
        
        System.out.println("The inserted srCopy2:\n" + srCopy2);
        
        // A more verbose approach
        SaleRecord srCopy3 = new SaleRecord();        
        srCopy3.from(sr);
        srCopy3.changed(SALE.SALE_ID, false);
        ctx.attach(srCopy3);        
        srCopy3.insert();
        
        System.out.println("The inserted srCopy3:\n" + srCopy3);
     
        // =====================================================================
        
        // Modify the *sr* record in-memory to insert a new row with different data without creating a new record                        
        sr.setFiscalYear(2005);
        sr.setSale(101010.11);
        sr.setFiscalMonth(1);
        sr.setRevenueGrowth(0.0);
        System.out.println("Modified record:\n" + sr + " [" + sr.changed() + "]");
        sr.insert(); // insert a new row with different data without creating a new record                        
        
        // =====================================================================
                
        // Prevent unchanded records to be inserted.
        // By default, isInsertUnchangedRecords() returns true.        
        DSLContext derivedCtx = ctx.configuration().derive(new Settings()
                .withInsertUnchangedRecords(false)).dsl();
        derivedCtx.attach(sr); // or, sr.attach(derivedCtx.configuration());
        sr.insert();
        
        System.out.println("Prevent unchanded records to be inserted.");
        
        // =====================================================================
        
        // Insert without returning the generated primary key
        DSLContext derivedCtxNoReturnId = ctx.configuration().derive(new Settings()
                .withReturnIdentityOnUpdatableRecord(false)).dsl();
        
        SaleRecord srNoReturnId = derivedCtxNoReturnId.newRecord(SALE);
        
        srNoReturnId.setFiscalYear(2021);
        srNoReturnId.setSale(4500.25);
        srNoReturnId.setEmployeeNumber(1504L);
        srNoReturnId.setFiscalMonth(1);
        srNoReturnId.setRevenueGrowth(0.0);
        
        srNoReturnId.insert();
        
        System.out.println("The inserted record ID (should be null): " + srNoReturnId.getSaleId());        
        
        // =====================================================================
        
        // detach 'sr' from current configuration, no more DML operations 
        // can be done without reattaching it (this doesn't affect other attached records)
        sr.detach();        
        
        // =====================================================================             
    }
    
    @Transactional 
    public void insertRecordReturnAllFields() {
        
        DSLContext derivedCtx = ctx.configuration().derive(new Settings()
                .withReturnAllOnUpdatableRecord(true)).dsl();
        
        TokenRecord tr = derivedCtx.newRecord(TOKEN); // attached automatically
        tr.setSaleId(1L);
        tr.setAmount(340.43);
                        
        tr.insert();
        
        System.out.println("Inserted on: " + tr.getUpdatedOn());
    }

    @Transactional
    public void updateRecord() {
        
        // The fetched record is auto-attached to the current configuration by jOOQ
        // so, there is no need to manually attach *sr*
        SaleRecord sr = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(1L))
                .fetchSingle();                
        
        sr.setFiscalYear(2000);
        sr.setSale(1111.25);        
        
        sr.update(); // this updates *sr* to reflect the modifications
        
        System.out.println("The updated record ID: " + sr.getSaleId());
        
        // =====================================================================
        
        // Calling update() again is pointless, there is nothing to update and jOOQ knows it
        // but, we can force an update by marking its fields as changed
        sr.changed(true); // update all fields
        sr.update();
        
        System.out.println("The updated record ID: " + sr.getSaleId());
        
        // =====================================================================
        
        // Force again, but this time update only two fields (only these fields are rendered in UPDATE)
        sr.changed(SALE.FISCAL_YEAR, true); // this field will be part of the rendered UPDATE
        sr.changed(SALE.SALE_, true);       // this field will be part of the rendered UPDATE
        sr.update();
        
        System.out.println("The updated record ID: " + sr.getSaleId());
        
        // =====================================================================
        
        // Refresh *sr*, but first, modify it memory
        sr.setFiscalYear(2020);
        sr.setSale(0.0);
        
        // In this case reset() and refresh() produces the same result, so
        // you should prefer reset() which acts in memory
        System.out.println("Record before reset/referesh:\n" + sr);        
        sr.reset();   // reset *sr* to the original values (in memory)
        System.out.println("Record after reset:\n" + sr);        
        sr.refresh(); // a SELECT is executed to fetch the latest record from the database
        System.out.println("Record after referesh:\n" + sr);        
        
        // =====================================================================
        
        // Force an update via Settings.withUpdateUnchangedRecords(UpdateUnchangedRecords)  
        // by default, UpdateUnchangedRecords is NEVER
        DSLContext derivedCtx = ctx.configuration().derive(new Settings()
                .withUpdateUnchangedRecords(UpdateUnchangedRecords.SET_NON_PRIMARY_KEY_TO_RECORD_VALUES)).dsl();
        derivedCtx.attach(sr); // or, sr.attach(derivedCtx.configuration());        
        sr.update();        
        
        System.out.println("The updated record is:\n" + sr);                     
    }
    
    @Transactional 
    public void updateRecordReturnAllFields() {
        
        DSLContext derivedCtx = ctx.configuration().derive(new Settings()
                .withReturnAllOnUpdatableRecord(true)).dsl();
        
        TokenRecord tr = derivedCtx.selectFrom(TOKEN)
                .where(TOKEN.TOKEN_ID.eq(1L))
                .fetchSingle();
                
        tr.setAmount(999.99);
                       
        tr.update();
        
        System.out.println("Updated on: "+tr.getUpdatedOn());
    }
    
    @Transactional
    public void deleteRecord() {
        
        // The fetched record is auto-attached to the current configuration by jOOQ
        // so, there is no need to manually attach *sr*
        SaleRecord sr = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(5L))                
                .fetchSingle();                  
                
        System.out.println("Record to be deleted is:\n" + sr);
        
        sr.delete();                        
        
        System.out.println("Deleted record is:\n" + sr);
    }
        
    @Transactional
    public void mergeRecord() {
                
        SaleRecord srFetched = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(1L))
                .fetchSingle();                
        srFetched.setFiscalYear(2005);
        srFetched.changed(SALE.SALE_, true); // this field is just marked as changed, so it will be rendered in SQL
        srFetched.changed(SALE.FISCAL_MONTH, true); // this field is just marked as changed, so it will be rendered in SQL
        srFetched.changed(SALE.REVENUE_GROWTH, true); // this field is just marked as changed, so it will be rendered in SQL
        
        SaleRecord srNew = ctx.newRecord(SALE);
        srNew.setFiscalYear(2000);
        srNew.setSale(100.25);
        srNew.setEmployeeNumber(1370L);
        srNew.setFiscalMonth(1);
        srNew.setRevenueGrowth(0.0);
                
        // *srFetched* will be updated
        srFetched.merge();
        
        System.out.println("The merged record is (UPDATE):\n" + srFetched);         
        
        // *srNew* will be inserted
        srNew.merge();
        
        System.out.println("The merged record is (INSERT):\n" + srNew);         
        
        // =====================================================================
        
        SaleRecord sr = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(1L))
                .fetchSingle();    
        sr.setFiscalYear(2000);
        sr.setSale(8888.88);
        sr.setTrend("UP");
        sr.changed(SALE.FISCAL_MONTH, true); // this field is just marked as changed, so it will be rendered in SQL
        sr.changed(SALE.REVENUE_GROWTH, true); // this field is just marked as changed, so it will be rendered in SQL
        
        // since TREND is not part of merge() this will not merge anything
        sr.merge(SALE.FISCAL_YEAR, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH); 
        
        System.out.println("The merged record is (certain fields):\n" + sr);         
        
        // this merge TREND as well
        sr.changed(SALE.FISCAL_YEAR, true); // if we don't mark this field as changed it will be ignored even if is mentioned in the following merge()
        sr.changed(SALE.SALE_, true);       // if we don't mark this field as changed it will be ignored even if is mentioned in the following merge()
        sr.changed(SALE.FISCAL_MONTH, true); // this field is just marked as changed, so it will be rendered in SQL
        sr.changed(SALE.REVENUE_GROWTH, true); // this field is just marked as changed, so it will be rendered in SQL        
        sr.merge(SALE.FISCAL_YEAR, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH, SALE.TREND); 
        
        System.out.println("The merged record is (certain fields):\n" + sr);         
        
        // =====================================================================
        
        // Force an update via Settings.withUpdateUnchangedRecords(UpdateUnchangedRecords)  
        // by default, UpdateUnchangedRecords is NEVER
        DSLContext derivedCtx = ctx.configuration().derive(new Settings()
                .withUpdateUnchangedRecords(UpdateUnchangedRecords.SET_NON_PRIMARY_KEY_TO_RECORD_VALUES)).dsl();
        derivedCtx.attach(sr); // or, sr.attach(derivedCtx.configuration());        
        sr.merge();        
        
        System.out.println("The merged record is:\n" + sr);             
    }
    
    @Transactional
    public void storeRecord() {
                        
        SaleRecord srNew = ctx.newRecord(SALE);
        srNew.setFiscalYear(2000);
        srNew.setSale(100.25);
        srNew.setEmployeeNumber(1370L);
        srNew.setFiscalMonth(1);
        srNew.setRevenueGrowth(0.0);
                        
        // *srNew* will be inserted (jOOQ decide to execute an insert)
        srNew.store(); // render an INSERT      
        
        System.out.println("The stored record is (INSERT):\n" + srNew);         
        
        // =====================================================================
        
        SaleRecord srFetched = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(5L))
                .fetchSingle();                
        srFetched.setFiscalYear(2005);
        srFetched.changed(SALE.SALE_, true); // this field is just marked as changed, so it will be rendered in SQL
        
        // *srFetched* will be updated (jOOQ decide to execute an update)
        srFetched.store(); // render an UPDATE
        
        System.out.println("The stored record is (UPDATE):\n" + srFetched);         
        
        // =====================================================================

        // modify the primary key
        srFetched.setSaleId((long) (Math.random() * 999999999L));
        
        srFetched.store(); // render an INSERT
        
        System.out.println("The stored record is (INSERT caused by primary key modification):\n" + srFetched);         
                
        // =====================================================================
        
        // Force an update via Settings.withUpdateUnchangedRecords(UpdateUnchangedRecords)  
        // by default, UpdateUnchangedRecords is NEVER
        DSLContext derivedCtx = ctx.configuration().derive(new Settings()
                .withUpdateUnchangedRecords(UpdateUnchangedRecords.SET_NON_PRIMARY_KEY_TO_RECORD_VALUES)).dsl();
        derivedCtx.attach(srNew); // or, sr.attach(derivedCtx.configuration());        
        srNew.store(); // render an UPDATE        
        
        System.out.println("The stored record is (forced UPDATE):\n" + srNew);         
    }
    
    @Transactional
    public void storeRecordAfterUpdatePrimaryKeyViaInsert() {
        
        SaleRecord sr = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(4L))
                .fetchSingle();      
                
        sr.setSaleId((long) (Math.random() * 999999999L));
        sr.setFiscalYear(2005);        
        
        // Because the primary key was modified this executes an INSERT
        sr.store();
        
        System.out.println("The stored record is (INSERT caused by the primary key modification):\n" + sr);                    
    }
        
    @Transactional
    public void storeRecordAfterUpdatePrimaryKeyViaUpdate() {
        
        DSLContext derivedCtx = ctx.configuration().derive(new Settings()
                .withUpdatablePrimaryKeys(true)).dsl();
        
        SaleRecord sr = derivedCtx.selectFrom(SALE)                
                .where(SALE.SALE_ID.eq(7L))
                .fetchSingle();         
        
        // Forcing an UPDATE can be done via Settings.isUpdatablePrimaryKeys() 
        // By default, isUpdatablePrimaryKeys() return false
        sr.setSaleId((long) (Math.random() * 999999999L));
        sr.setFiscalYear(2007);
        
        sr.store();        
        
        System.out.println("The stored record is (force UPDATE of primary key):\n" + sr);                    
    }        
}