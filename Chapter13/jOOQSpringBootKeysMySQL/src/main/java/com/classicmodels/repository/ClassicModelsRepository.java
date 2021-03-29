package com.classicmodels.repository;

import static jooq.generated.tables.Productline.PRODUCTLINE;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.SaleRecord;
import org.jooq.DSLContext;
import org.jooq.conf.Settings;
import static org.jooq.impl.DSL.row;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    /* Primary keys and updatable records */        
    
    @Transactional
    public void suppressPrimaryKeyReturnOnUpdatableRecord() {

        // Insert without returning the generated primary key
        DSLContext derivedCtx = ctx.configuration().derive(new Settings()
                .withReturnIdentityOnUpdatableRecord(false)).dsl();

        SaleRecord srNoReturnId = derivedCtx.newRecord(SALE);

        srNoReturnId.setFiscalYear(2021);
        srNoReturnId.setSale(4500.25);
        srNoReturnId.setEmployeeNumber(1504L);

        srNoReturnId.insert();

        System.out.println("The inserted record ID (should be null): " + srNoReturnId.getSaleId());       
    }

    @Transactional
    public void updatePrimaryKeyOnUpdatableRecord() {

        DSLContext derivedCtx = ctx.configuration().derive(new Settings()
                .withUpdatablePrimaryKeys(true)).dsl();

        SaleRecord sr = derivedCtx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(2L))
                .fetchSingle();

        // Forcing an UPDATE can be done via Settings.isUpdatablePrimaryKeys() 
        // By default, isUpdatablePrimaryKeys() return false
        sr.setSaleId((long) (Math.random() * 999999999L));
        sr.setFiscalYear(2007);

        sr.store();

        System.out.println("The stored record is (force UPDATE of primary key):\n" + sr);

        // update primary key via query
        ctx.update(SALE)
                .set(SALE.SALE_ID, sr.getSaleId() + 1)
                .where(SALE.SALE_ID.eq(sr.getSaleId()))
                .execute();
    }
    
    /* Insert and return primary key */
    
    @Transactional
    public void insertAndReturnPrimaryKey() {
        
        // Record1<Long>
        var insertedId = ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER)
                .values(2004, 2311.42, 1370L)
                .returningResult(SALE.SALE_ID)
                .fetchOne(); // get directly the long value, .fetchOne().value1();
        
        System.out.println("Inserted ID:\n" + insertedId);
        
        // Result<Record1<Long>>
        var insertedIds = ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER)
                .values(2004, 2311.42, 1370L)
                .values(2003, 900.21, 1504L)
                .values(2005, 1232.2, 1166L)
                .returningResult(SALE.SALE_ID)
                .fetch();
        
        System.out.println("Inserted IDs:\n" + insertedIds);
        
        // use lastID()
        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER)
                .values(2002, 9876.96, 1504L)
                .execute();

        // PAY ATTENTION TO THE FACT THAT, MEANWHILE, A CONCURRENT TRANSACTION CAN MODIFY THE CURRENT VALUE
        // SO, THERE IS NO GUARANTEE THAT THE BELOW FETCHED *lastId* IS THE PRIMARY KEY OF THE PREVIOUS INSERT
                
        // if you cannot provide an identity
        var lastId = ctx.lastID();
        
        System.out.println("Last ID: " + lastId);
    }
    
    /* Compare composed keys */
    
    public void compareComposedPrimaryKey() {

        var result1 = ctx.selectFrom(PRODUCTLINE)
                .where(PRODUCTLINE.PRODUCT_LINE.eq("Classic Cars")
                        .and(PRODUCTLINE.CODE.eq(599302L)))
                .fetchSingle();
        System.out.println("Result 1:\n" + result1);

        var result2 = ctx.selectFrom(PRODUCTLINE)
                .where(row(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.CODE)
                        .eq(row("Classic Cars", 599302L)))
                .fetchSingle();
        System.out.println("Result 2:\n" + result2);
        
        // using in() (similar, you can use notIn(), and so on)
        var result3 = ctx.selectFrom(PRODUCTLINE)
                .where(row(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.CODE)                        
                        .in(row("Classic Cars", 599302L),
                            row("Trains", 123333L),
                            row("Motorcycles", 599302L)))
                .fetch();
        System.out.println("Result 3:\n" + result3);        
    }
}
