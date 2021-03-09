package com.classicmodels.repository;

import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.SaleRecord;
import org.jooq.DSLContext;
import org.jooq.conf.Settings;
import org.jooq.conf.UpdateUnchangedRecords;
import org.springframework.stereotype.Repository;

@Repository
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
        System.out.println("Modified record:\n " + sr + " [" + sr.changed() + "]");
                       
        // get the *sr* before modifications (as it come from the database)
        // *sr* and *sOrg* are not the same object
        SaleRecord srOrg = sr.original();
        System.out.println("Original record:\n " + srOrg + " [" + srOrg.changed() + "]");
                 
        // reset the record
        sr.reset(); // restore *sr* to the original content
        System.out.println("Reseted record:\n " + sr + " [" + sr.changed() + "]");
        
        // refersh the record (execute a SELECT to load it from the database)
        sr.refresh();
        System.out.println("Refreshed record:\n " + sr + " [" + sr.changed() + "]");
        
        // mark it as changed (all fields)
        sr.changed(true);
        System.out.println("Changed record: " + sr.changed());
    }
    
    public void insertNewRecord() {

        SaleRecord sr = new SaleRecord();
        sr.setFiscalYear(2021);
        sr.setSale(4500.25);
        sr.setEmployeeNumber(1504L);

        // Before insert (or any other DML) we attach the record to the current configuration        
        ctx.attach(sr); // or, sr.attach(ctx.configuration());
        
        sr.insert();        
        // sr.insert(SALE.FISCAL_YEAR, SALE.SALE_); // Insert a subset of fields (SALE.EMPLOYEE_NUMBER() is omitted)
        
        System.out.println("The inserted record ID: " + sr.getSaleId());

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
        sr.insert();
        
        // Re-inserting the same data by creating a new record (*sr* and *srCopy* are not the same object!)
        SaleRecord srCopy1 = sr.copy();
        srCopy1.insert();               
        
        // A more verbose approach
        SaleRecord srCopy2 = new SaleRecord();        
        srCopy2.from(sr);
        srCopy2.changed(SALE.SALE_ID, false);
        ctx.attach(srCopy2);        
        srCopy2.insert();
     
        // =====================================================================
        
        // Modify the *sr* record in-memory to insert a new row with different data without creating a new record                        
        sr.setFiscalYear(2005);
        sr.setSale(101010.11);
        System.out.println("Modified record:\n " + sr + " [" + sr.changed() + "]");
        sr.insert(); // insert a new row with different data without creating a new record                        
        
        // =====================================================================
                
        // Prevent unchanded records to be inserted.
        // By default, isInsertUnchangedRecords() returns true.        
        DSLContext derivedCtx = ctx.configuration().derive(new Settings()
                .withInsertUnchangedRecords(false)).dsl();
        derivedCtx.attach(sr); // or, sr.attach(derivedCtx.configuration());
        sr.insert();
        
        // =====================================================================
        
        // detach 'sr' from current configuration, no more INSERT can be done without reattaching it
        sr.detach();
        
        // =====================================================================             
    }

    public void updateRecord() {
        
        // The fetched record is auto-attached to the current configuration by jOOQ
        // so, there is no need to manually attach *sr*
        SaleRecord sr = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(1L))
                .fetchSingle();                
        
        sr.setFiscalYear(2000);
        sr.setSale(1111.11);
        
        sr.update(); // this updates *sr* to reflect the modifications
        
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
        System.out.println("The updated record ID: " + sr.getSaleId());
        
        // =====================================================================
        
        // Refresh *sr*, but first, modify it memory
        sr.setFiscalYear(2020);
        sr.setSale(0.0);
        
        System.out.println("Record before referesh:\n " + sr);        
        sr.refresh(); // a SELECT is executed to fetch the latest record from the database
        System.out.println("Record after referesh:\n " + sr);        
        
        // =====================================================================
        
        // Force an update via Settings.withUpdateUnchangedRecords(UpdateUnchangedRecords)  
        // by default, UpdateUnchangedRecords is NEVER
        DSLContext derivedCtx = ctx.configuration().derive(new Settings()
                .withUpdateUnchangedRecords(UpdateUnchangedRecords.SET_NON_PRIMARY_KEY_TO_RECORD_VALUES)).dsl();
        derivedCtx.attach(sr); // or, sr.attach(derivedCtx.configuration());        
        sr.update();        
        System.out.println("The updated record is:\n " + sr);                     
    }
    
    public void mergeRecord() {
                
        SaleRecord srLoaded = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(1L))
                .fetchSingle();                
        srLoaded.setFiscalYear(2005);
        srLoaded.changed(SALE.SALE_, true); // this field is just marked as changed, so it will be rendered in SQL
        
        SaleRecord srNew = new SaleRecord();
        srNew.setFiscalYear(2000);
        srNew.setSale(100.25);
        srNew.setEmployeeNumber(1370L);
        
        ctx.attach(srNew);
        
        // *srLoaded* will be updated
        srLoaded.merge();
        
        // *srNew* will be inserted
        srNew.merge();
        
        // =====================================================================
        
        SaleRecord sr = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(1L))
                .fetchSingle();    
        sr.setFiscalYear(2000);
        sr.setSale(8888.88);
        sr.setTrend("UP");
        
        // since TREND is not part of merge() this will not merge anything
        sr.merge(SALE.FISCAL_YEAR, SALE.SALE_); 
        
        // this merge TREND as well
        sr.changed(SALE.FISCAL_YEAR, true); // if we don't mark this field as changed it will be ignored even if is mentioned in the following merge()
        sr.changed(SALE.SALE_, true);       // if we don't mark this field as changed it will be ignored even if is mentioned in the following merge()
        sr.merge(SALE.FISCAL_YEAR, SALE.SALE_, SALE.TREND); 
        
        // =====================================================================
        
        // Force an update via Settings.withUpdateUnchangedRecords(UpdateUnchangedRecords)  
        // by default, UpdateUnchangedRecords is NEVER
        DSLContext derivedCtx = ctx.configuration().derive(new Settings()
                .withUpdateUnchangedRecords(UpdateUnchangedRecords.SET_NON_PRIMARY_KEY_TO_RECORD_VALUES)).dsl();
        derivedCtx.attach(sr); // or, sr.attach(derivedCtx.configuration());        
        sr.merge();        
        System.out.println("The merged record is:\n " + sr);             
    }
    
    public void storeRecord() {
        
        SaleRecord srLoaded = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(1L))
                .fetchSingle();                
        srLoaded.setFiscalYear(2005);
        srLoaded.changed(SALE.SALE_, true); // this field is just marked as changed, so it will be rendered in SQL
        
        SaleRecord srNew = new SaleRecord();
        srNew.setFiscalYear(2000);
        srNew.setSale(100.25);
        srNew.setEmployeeNumber(1370L);
        
        ctx.attach(srNew);
        
        // *srLoaded* will be updated (jOOQ decide to execute an update)
        srLoaded.store();
        
        // *srNew* will be inserted (jOOQ decide to execute an insert)
        srNew.store();                        
        
        // =====================================================================
        
        // Force an update via Settings.withUpdateUnchangedRecords(UpdateUnchangedRecords)  
        // by default, UpdateUnchangedRecords is NEVER
        DSLContext derivedCtx = ctx.configuration().derive(new Settings()
                .withUpdateUnchangedRecords(UpdateUnchangedRecords.SET_NON_PRIMARY_KEY_TO_RECORD_VALUES)).dsl();
        derivedCtx.attach(srNew); // or, sr.attach(derivedCtx.configuration());        
        srNew.store();        
        System.out.println("The stored record is:\n " + srNew);         
    }
    
    public void storeRecordAfterUpdatePrimaryKey() {
        
        SaleRecord sr = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(1L))
                .fetchSingle();      
                
        sr.setSaleId((long) (Math.random() * 999999999L));
        sr.setFiscalYear(2005);        
        
        // Because the primary key was modified this executes an INSERT
        sr.store();
        
        // Forcing an UPDATE can be done via Settings.isUpdatablePrimaryKeys() 
        // By default, isUpdatablePrimaryKeys() return false
        sr.setSaleId((long) (Math.random() * 999999999L));
        sr.setFiscalYear(2007);        
        DSLContext derivedCtx = ctx.configuration().derive(new Settings()
                .withUpdatablePrimaryKeys(true)).dsl();
        derivedCtx.attach(sr); // or, sr.attach(derivedCtx.configuration());        
        sr.store();        
        
        System.out.println("The stored record is:\n " + sr);                    
    }
}
