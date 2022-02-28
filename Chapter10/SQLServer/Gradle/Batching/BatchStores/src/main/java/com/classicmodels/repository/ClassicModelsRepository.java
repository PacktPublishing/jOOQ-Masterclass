package com.classicmodels.repository;

import java.util.Arrays;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.SaleRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void batchStoresSimple() {

        // execute an INSERT
        SaleRecord sr1 = new SaleRecord();
        sr1.setFiscalYear(2005);
        sr1.setSale(1223.23);
        sr1.setEmployeeNumber(1370L);
        sr1.setFiscalMonth(1);
        sr1.setRevenueGrowth(0.0);

        // execute an UPDATE (if you modify the primary key that an INSERT is executed
        SaleRecord sr2 = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(2L))
                .fetchOne();

        if (sr2 != null) {
            sr2.setFiscalYear(2006);

            int[] result1 = ctx.batchStore(sr1, sr2)
                .execute();

            System.out.println("EXAMPLE 1.1: " + Arrays.toString(result1));
        }
        
        // update all records and add a few more
        Result<SaleRecord> sales = ctx.selectFrom(SALE).fetch();
        
        // update all sales
        sales.forEach(sale -> {
            sale.setTrend("UP");
        });
        
        // add more new sales
        SaleRecord srn1 = new SaleRecord();
        srn1.setFiscalYear(2004);
        srn1.setSale(1223.23);
        srn1.setEmployeeNumber(1370L);
        srn1.setFiscalMonth(1);
        srn1.setRevenueGrowth(0.0);
        sales.add(srn1);
        
        SaleRecord srn2 = new SaleRecord();
        srn2.setFiscalYear(2002);
        srn2.setSale(1243.25);
        srn2.setEmployeeNumber(1504L);
        srn2.setFiscalMonth(1);
        srn2.setRevenueGrowth(0.0);
        sales.add(srn2);
        
        SaleRecord srn3 = new SaleRecord();
        srn3.setFiscalYear(2003);
        srn3.setSale(3323.26);
        srn3.setEmployeeNumber(1504L);
        srn3.setFiscalMonth(1);
        srn3.setRevenueGrowth(0.0);
        sales.add(srn3);
        
        int[] result2 = ctx.batchStore(sales)
                .execute();

        System.out.println("EXAMPLE 1.2: " + Arrays.toString(result2));        
    }

    public void batchStoresPreparedStatement1() {

        SaleRecord i1 = new SaleRecord();
        i1.setFiscalYear(2005);
        i1.setSale(1223.23);
        i1.setEmployeeNumber(1370L);
        i1.setFiscalMonth(1);
        i1.setRevenueGrowth(0.0);
        SaleRecord i2 = new SaleRecord();
        i2.setFiscalYear(2005);
        i2.setSale(9022.21);
        i2.setEmployeeNumber(1166L);
        i2.setFiscalMonth(1);
        i2.setRevenueGrowth(0.0);
        SaleRecord i3 = new SaleRecord();
        i3.setFiscalYear(2003);
        i3.setSale(8002.22);
        i3.setEmployeeNumber(1504L);
        i3.setFiscalMonth(1);
        i3.setRevenueGrowth(0.0);
        SaleRecord i4 = new SaleRecord();
        i4.setFiscalYear(2003);
        i4.setSale(8002.22);
        i4.setEmployeeNumber(1611L);
        i4.setFiscalMonth(1);
        i4.setRevenueGrowth(0.0);

        SaleRecord u1 = ctx.selectFrom(SALE).where(SALE.SALE_ID.eq(1L)).fetchSingle();
        u1.setFiscalYear(2010);
        SaleRecord u2 = ctx.selectFrom(SALE).where(SALE.SALE_ID.eq(2L)).fetchSingle();
        u2.setFiscalYear(2011);
        SaleRecord u3 = ctx.selectFrom(SALE).where(SALE.SALE_ID.eq(3L)).fetchSingle();
        u3.setFiscalYear(2012);
        SaleRecord u4 = ctx.selectFrom(SALE).where(SALE.SALE_ID.eq(4L)).fetchSingle();
        u4.setFiscalYear(2013);

        // There will just 2 batches, 1 batch for INSERTs and 1 batch for UPDATEs because
        // the generated SQL with bind variables is the same for all INSERTs, and the same for all UPDATEs.
        // The order of records is perserved exactly since:
        // INSERTs are executed in order i1, i2, i3, i4
        // UPDATEs are executed in order u1, u2, u3, u4
        int[] result = ctx.batchStore(i1, u1, i2, u2, i3, u3, i4, u4)
                .execute();

        System.out.println("EXAMPLE 2: " + Arrays.toString(result));
    }

    public void batchStoresPreparedStatement2() {

        SaleRecord i1 = new SaleRecord();
        i1.setFiscalYear(2005);
        i1.setSale(1223.23);
        i1.setEmployeeNumber(1370L);        
        i1.setFiscalMonth(1);
        i1.setRevenueGrowth(0.0);
        SaleRecord u1 = ctx.selectFrom(SALE).where(SALE.SALE_ID.eq(1L)).fetchSingle();
        u1.setFiscalYear(2006);
        SaleRecord i2 = new SaleRecord();
        i2.setFiscalYear(2005);
        i2.setSale(9022.21);
        i2.setEmployeeNumber(1166L);        
        i2.setFiscalMonth(1);
        i2.setRevenueGrowth(0.0);
        SaleRecord u2 = ctx.selectFrom(SALE).where(SALE.SALE_ID.eq(2L)).fetchSingle();
        u2.setFiscalYear(2007);
        SaleRecord i3 = new SaleRecord();
        i3.setFiscalYear(2003);
        i3.setSale(8002.22);
        i3.setEmployeeNumber(1504L);        
        i3.setFiscalMonth(1);
        i3.setRevenueGrowth(0.0);
        SaleRecord u3 = ctx.selectFrom(SALE).where(SALE.SALE_ID.eq(3L)).fetchSingle();
        u3.setFiscalYear(2008);
        SaleRecord i4 = new SaleRecord();
        i4.setFiscalYear(2003);
        i4.setSale(8002.22);
        i4.setEmployeeNumber(1611L);
        i4.setFiscalMonth(1);
        i4.setRevenueGrowth(0.0);
        SaleRecord u4 = ctx.selectFrom(SALE).where(SALE.SALE_ID.eq(4L)).fetchSingle();
        u4.setFiscalYear(2009);

        // There will just 2 batches, 1 batch for INSERTs and 1 batch for UPDATEs because
        // the generated SQL with bind variables is the same for all INSERTs, and the same for all UPDATEs.
        // The order of records is not perserved since:
        // INSERTs are executed in order i1, i2, i3, i4
        // UPDATEs are executed in order u1, u2, u3, u4
        int[] result = ctx.batchStore(i1, u1, i2, u2, i3, u3, i4, u4)
                .execute();

        System.out.println("EXAMPLE 3: " + Arrays.toString(result));
    }

    public void batchStoresPreparedStatement3() {

        SaleRecord i1 = new SaleRecord();
        i1.setFiscalYear(2005);
        i1.setSale(1223.23);
        i1.setEmployeeNumber(1370L);        
        i1.setFiscalMonth(1);
        i1.setRevenueGrowth(0.0);
        SaleRecord u1 = ctx.selectFrom(SALE).where(SALE.SALE_ID.eq(1L)).fetchSingle();
        u1.setFiscalYear(2016);
        SaleRecord i2 = new SaleRecord();
        i2.setFiscalYear(2005);
        i2.setSale(9022.21);
        i2.setEmployeeNumber(1166L);        
        i2.setFiscalMonth(1);
        i2.setRevenueGrowth(0.0);
        SaleRecord u2 = ctx.selectFrom(SALE).where(SALE.SALE_ID.eq(2L)).fetchSingle();
        u2.setTrend("CONSTANT");
        SaleRecord i3 = new SaleRecord();
        i3.setFiscalYear(2003);
        i3.setSale(8002.22);
        i3.setEmployeeNumber(1504L);        
        i3.setFiscalMonth(1);
        i3.setRevenueGrowth(0.0);
        SaleRecord u3 = ctx.selectFrom(SALE).where(SALE.SALE_ID.eq(3L)).fetchSingle();
        u3.setSale(0.0);
        SaleRecord i4 = new SaleRecord();
        i4.setFiscalYear(2003);
        i4.setSale(8002.22);
        i4.setEmployeeNumber(1611L);
        i4.setFiscalMonth(1);
        i4.setRevenueGrowth(0.0);
        SaleRecord u4 = ctx.selectFrom(SALE).where(SALE.SALE_ID.eq(4L)).fetchSingle();
        u4.setFiscalYear(2017);

        // There will just 4 batches, 1 batch for INSERTs and 3 batch for UPDATEs because
        // The generated SQL with bind variables is the same for all INSERTs.
        // The generated SQL with bind variables is the same for UPDATEs u1 and u4, so they form 1 batch.
        // The generated SQL with bind variables is not the same for UPDATEs u2 and u3, so they form 1 batch each.
        // The order of records is not perserved since:
        // INSERTs are executed in order i1, i2, i3, i4
        // UPDATEs are executed in order u1, u4, u2, u3
        int[] result = ctx.batchStore(i1, u1, i2, u2, i3, u3, i4, u4)
                .execute();

        System.out.println("EXAMPLE 4: " + Arrays.toString(result));
    }

    public void batchStoresPreparedStatement4() {

        SaleRecord i1 = new SaleRecord();        
        i1.setFiscalYear(2005);
        i1.setSale(1223.23);
        i1.setEmployeeNumber(1370L);
        i1.setTrend("UP");
        i1.setFiscalMonth(1);
        i1.setRevenueGrowth(0.0);
        SaleRecord u1 = ctx.selectFrom(SALE).where(SALE.SALE_ID.eq(1L)).fetchSingle();
        u1.setFiscalYear(2018);
        SaleRecord i2 = new SaleRecord();        
        i2.setFiscalYear(2005);
        i2.setSale(9022.21);
        i2.setFiscalMonth(1);
        i2.setRevenueGrowth(0.0);
        SaleRecord u2 = ctx.selectFrom(SALE).where(SALE.SALE_ID.eq(2L)).fetchSingle();
        u2.setTrend("DOWN");
        SaleRecord i3 = new SaleRecord();        
        i3.setFiscalYear(2003);
        i3.setSale(8002.22);
        i3.setEmployeeNumber(1504L);
        i3.setFiscalMonth(1);
        i3.setRevenueGrowth(0.0);
        SaleRecord u3 = ctx.selectFrom(SALE).where(SALE.SALE_ID.eq(3L)).fetchSingle();
        u3.setSale(10000.0);
        SaleRecord i4 = new SaleRecord();        
        i4.setFiscalYear(2003);
        i4.setSale(8002.22);
        i4.setEmployeeNumber(1611L);
        i4.setHot(true);
        i4.setFiscalMonth(1);
        i4.setRevenueGrowth(0.0);
        SaleRecord u4 = ctx.selectFrom(SALE).where(SALE.SALE_ID.eq(4L)).fetchSingle();
        u4.setEmployeeNumber(1165L);

        // This is the worst-case scenario, there will just 8 batches, 1 batch for each INSERT and each UPDATE
        // The generated SQL with bind variables different for all statements.
        // The order of records is perserved since:
        // Statements are processed as: i1, u1, i2, u2, i3, u3, i4, u4
        int[] result = ctx.batchStore(i1, u1, i2, u2, i3, u3, i4, u4)
                .execute();

        System.out.println("EXAMPLE 5: " + Arrays.toString(result));
    }

    public void batchStoresStaticStatement() {

        SaleRecord i1 = new SaleRecord();
        i1.setFiscalYear(2005);
        i1.setSale(1223.23);
        i1.setEmployeeNumber(1370L);
        i1.setFiscalMonth(1);
        i1.setRevenueGrowth(0.0);
        SaleRecord i2 = new SaleRecord();
        i2.setFiscalYear(2005);
        i2.setSale(9022.21);
        i2.setEmployeeNumber(1166L);
        i2.setFiscalMonth(1);
        i2.setRevenueGrowth(0.0);
        SaleRecord i3 = new SaleRecord();
        i3.setFiscalYear(2003);
        i3.setSale(8002.22);
        i3.setEmployeeNumber(1504L);
        i3.setFiscalMonth(1);
        i3.setRevenueGrowth(0.0);
        SaleRecord i4 = new SaleRecord();
        i4.setFiscalYear(2003);
        i4.setSale(8002.22);
        i4.setEmployeeNumber(1611L);
        i4.setFiscalMonth(1);
        i4.setRevenueGrowth(0.0);

        SaleRecord u1 = ctx.selectFrom(SALE).where(SALE.SALE_ID.eq(1L)).fetchSingle();
        u1.setFiscalYear(2010);
        SaleRecord u2 = ctx.selectFrom(SALE).where(SALE.SALE_ID.eq(2L)).fetchSingle();
        u2.setFiscalYear(2011);
        SaleRecord u3 = ctx.selectFrom(SALE).where(SALE.SALE_ID.eq(3L)).fetchSingle();
        u3.setFiscalYear(2012);
        SaleRecord u4 = ctx.selectFrom(SALE).where(SALE.SALE_ID.eq(4L)).fetchSingle();
        u4.setFiscalYear(2013);

        // The order of records is perserved exactly
        // jOOQ can guarantee that only a single batch statement is serialised to the database
        int[] result = ctx.configuration().derive(
                new Settings().withStatementType(StatementType.STATIC_STATEMENT))
                .dsl().batchStore(i1, u1, i2, u2, i3, u3, i4, u4)
                .execute();

        System.out.println("EXAMPLE 6: " + Arrays.toString(result));
    }    
}
