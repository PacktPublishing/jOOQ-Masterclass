package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleSale;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static jooq.generated.Sequences.SALE_SEQ;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.BankTransactionRecord;
import jooq.generated.tables.records.SaleRecord;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void batchInsertStatements() {

        // batch inserts in a table having auto-generated primary key (several queries)
        int[] result11 = ctx.batch(
                ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(2005, 1370L, 1282.64, 1, 0.0),
                ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(2004, 1370L, 3938.24, 1, 0.0),
                ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(2004, 1370L, 4676.14, 1, 0.0),
                ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(2003, 1166L, 2223.0, 1, 0.0),
                ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(2004, 1166L, 4531.35, 1, 0.0),
                ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(2004, 1166L, 6751.33, 1, 0.0)
        ).execute();

        System.out.println("EXAMPLE 1.1.1: " + Arrays.toString(result11));
        
        // using nextval()
        int[] result12 = ctx.batch(
                ctx.insertInto(SALE, SALE.SALE_ID.coerce(BigInteger.class), SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(SALE_SEQ.nextval(), val(2005), val(1370L), val(1282.64), val(1), val(0.0)),
                ctx.insertInto(SALE, SALE.SALE_ID.coerce(BigInteger.class), SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(SALE_SEQ.nextval(), val(2004), val(1370L), val(3938.24), val(1), val(0.0)),
                ctx.insertInto(SALE, SALE.SALE_ID.coerce(BigInteger.class), SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(SALE_SEQ.nextval(), val(2004), val(1370L), val(4676.14), val(1), val(0.0)) 
        ).execute();

        System.out.println("EXAMPLE 1.1.2: " + Arrays.toString(result12));
        
        // pre-fetch IDs
        var ids = ctx.fetch(SALE_SEQ.nextvals(3));
        int[] result13 = ctx.batch(
                ctx.insertInto(SALE, SALE.SALE_ID.coerce(BigInteger.class), SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(ids.get(0).value1(), 2005, 1370L, 1282.64, 1, 0.0),
                ctx.insertInto(SALE, SALE.SALE_ID.coerce(BigInteger.class), SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(ids.get(1).value1(), 2004, 1370L, 3938.24, 1, 0.0),
                ctx.insertInto(SALE, SALE.SALE_ID.coerce(BigInteger.class), SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(ids.get(2).value1(), 2004, 1370L, 4676.14, 1, 0.0) 
        ).execute();

        System.out.println("EXAMPLE 1.1.3: " + Arrays.toString(result13));

        // batch inserts (single query) PreparedStatement
        int[] result21 = ctx.batch(
                ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values((Integer) null, null, null, null, null))
                .bind(2005, 1370L, 1282.64, 1, 0.0)
                .bind(2004, 1370L, 3938.24, 1, 0.0)
                .bind(2004, 1370L, 4676.14, 1, 0.0)
                .bind(2003, 1166L, 2223.0, 1, 0.0)
                .bind(2004, 1166L, 4531.35, 1, 0.0)
                .bind(2004, 1166L, 6751.33, 1, 0.0)
                .execute();

        System.out.println("EXAMPLE 1.2.1: " + Arrays.toString(result21));

        // batch inserts (single query) Statement
        int[] result22 = ctx.configuration().derive(
                new Settings().withStatementType(StatementType.STATIC_STATEMENT))
                .dsl().batch(
                        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                                .values((Integer) null, null, null, null, null))
                .bind(2005, 1370L, 1282.64, 1, 0.0)
                .bind(2004, 1370L, 3938.24, 1, 0.0)
                .bind(2004, 1370L, 4676.14, 1, 0.0)
                .bind(2003, 1166L, 2223.0, 1, 0.0)
                .bind(2004, 1166L, 4531.35, 1, 0.0)
                .bind(2004, 1166L, 6751.33, 1, 0.0)
                .execute();

        System.out.println("EXAMPLE 1.2.2: " + Arrays.toString(result22));

        // batch inserts in a table having manually assigned primary key
        int[] result3 = ctx.batch(
                ctx.insertInto(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.LAST_NAME, EMPLOYEE.FIRST_NAME, EMPLOYEE.EXTENSION,
                        EMPLOYEE.EMAIL, EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY, EMPLOYEE.JOB_TITLE)
                        .values(10L, "Toga", "Alison", "x3332", "talison@classicmodelcars.com", "1", 110000, "VP Sales")
                        .onDuplicateKeyIgnore(),
                ctx.insertInto(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.LAST_NAME, EMPLOYEE.FIRST_NAME, EMPLOYEE.EXTENSION,
                        EMPLOYEE.EMAIL, EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY, EMPLOYEE.JOB_TITLE)
                        .values(11L, "Marius", "Pologa", "x5332", "mpologa@classicmodelcars.com", "3", 50000, "Sales Rep")
                        .onDuplicateKeyIgnore(),
                ctx.insertInto(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.LAST_NAME, EMPLOYEE.FIRST_NAME, EMPLOYEE.EXTENSION,
                        EMPLOYEE.EMAIL, EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY, EMPLOYEE.JOB_TITLE)
                        .values(12L, "Ana", "Arica", "x5111", "aarica@classicmodelcars.com", "3", 50000, "Sales Rep")
                        .onDuplicateKeyIgnore()
        ).execute();

        System.out.println("EXAMPLE 1.3: " + Arrays.toString(result3));
    }

    public void batchInsertRecords1() {

        // Records batch inserts (single query, bind values)
        SaleRecord sr11 = new SaleRecord(null, 2005, 1223.23, 1370L, null, null, null, 1, 0.0, null);
        SaleRecord sr12 = new SaleRecord(null, 2004, 543.33, 1166L, null, null, null, 1, 0.0, null);
        SaleRecord sr13 = new SaleRecord(null, 2005, 9022.21, 1370L, null, null, null, 1, 0.0, null);
        SaleRecord sr14 = new SaleRecord(null, 2003, 4333.22, 1504L, null, null, null, 1, 0.0, null);
        SaleRecord sr15 = new SaleRecord(null, 2003, 8002.22, 1504L, null, null, null, 1, 0.0, null);
        
        sr11.changed(SALE.SALE_ID, false);
        sr12.changed(SALE.SALE_ID, false);
        sr13.changed(SALE.SALE_ID, false);
        sr14.changed(SALE.SALE_ID, false);
        sr15.changed(SALE.SALE_ID, false);

        List<SaleRecord> sales1 = List.of(sr15, sr12, sr13, sr14, sr11);

        // There is a single batch since the generated SQL with bind variables is the same for sr11-sr15.
        // The order of records is preserved.
        int[] result1 = ctx.batchInsert(sales1)
                // or, .batchInsert(sr15, sr12, sr13, sr14, sr11)
                .execute();

        System.out.println("EXAMPLE 2.1: " + Arrays.toString(result1));

        // Records batch inserts (multiple query, inlined values)
        // Order of records is always preserved entirely
        SaleRecord sr21 = new SaleRecord(null, 2005, 1223.23, 1370L, null, null, null, 1, 0.0, null);
        SaleRecord sr22 = new SaleRecord(null, 2004, 543.33, 1166L, null, null, null, 1, 0.0, null);
        SaleRecord sr23 = new SaleRecord(null, 2005, 9022.21, 1370L, null, null, null, 1, 0.0, null);
        SaleRecord sr24 = new SaleRecord(null, 2003, 4333.22, 1504L, null, null, null, 1, 0.0, null);
        SaleRecord sr25 = new SaleRecord(null, 2003, 8002.22, 1504L, null, null, null, 1, 0.0, null);
        
        sr21.changed(SALE.SALE_ID, false);
        sr22.changed(SALE.SALE_ID, false);
        sr23.changed(SALE.SALE_ID, false);
        sr24.changed(SALE.SALE_ID, false);
        sr25.changed(SALE.SALE_ID, false);

        List<SaleRecord> sales2 = List.of(sr25, sr22, sr23, sr24, sr21);

        int[] result2 = ctx.configuration().derive(
                new Settings().withStatementType(StatementType.STATIC_STATEMENT))
                .dsl().batchInsert(sales2)
                // or, .batchInsert(sr25, sr22, sr23, sr24, sr21)
                .execute();

        System.out.println("EXAMPLE 2.2: " + Arrays.toString(result2));
    }

    public void batchInsertRecords2() {

        SaleRecord sr1 = new SaleRecord(null, 2005, 1223.23, 1370L, null, null, null, 1, 0.0, null);
        SaleRecord sr2 = new SaleRecord(null, 2004, 543.33, 1166L, null, null, null, 1, 0.0, null);
        SaleRecord sr3 = new SaleRecord(null, 2005, 9022.21, null, null, null, null, 1, 0.0, null);
        BankTransactionRecord bt1 = new BankTransactionRecord(
                null, "N/A", "N/A", BigDecimal.ZERO, LocalDateTime.now(), 114L, "NP603840", "VisaElectron", "N/A");
        SaleRecord sr4 = new SaleRecord(null, 2003, 4333.22, 1504L, null, null, null, 1, 0.0, null);
        SaleRecord sr5 = new SaleRecord(null, 2003, 8002.22, 1504L, null, null, null, 1, 0.0, "UP");
        BankTransactionRecord bt2 = new BankTransactionRecord(
                null, "N/A", "N/A", BigDecimal.ZERO, LocalDateTime.now(), 114L, "NP603840", "VisaElectron", "N/A");

        sr1.changed(SALE.SALE_ID, false);
        sr2.changed(SALE.SALE_ID, false);
        sr3.changed(SALE.SALE_ID, false);
        sr4.changed(SALE.SALE_ID, false);
        sr5.changed(SALE.SALE_ID, false);
        
        // There are two batches, one for SaleRecord and one for BankTransactionRecord.
        // The order of records is not preserved (check the log).
        int[] result = ctx.batchInsert(bt1, sr1, sr2, sr3, sr4, sr5, bt2)
                .execute();

        System.out.println("EXAMPLE 3: " + Arrays.toString(result));
    }

    public void batchInsertRecords3() {

        SaleRecord sr1 = new SaleRecord();
        sr1.setFiscalYear(2005);
        sr1.setSale(1223.23);
        sr1.setEmployeeNumber(1370L);
        sr1.setFiscalMonth(1);
        sr1.setRevenueGrowth(0.0);
        sr1.setTrend("UP");

        SaleRecord sr2 = new SaleRecord();
        sr2.setFiscalYear(2005);
        sr2.setSale(9022.21);
        sr2.setFiscalMonth(1);
        sr2.setRevenueGrowth(0.0);

        SaleRecord sr3 = new SaleRecord();
        sr3.setFiscalYear(2003);
        sr3.setSale(8002.22);
        sr3.setEmployeeNumber(1504L);
        sr3.setFiscalMonth(1);
        sr3.setRevenueGrowth(0.0);

        // There are three batches, one for each SaleRecord because the generated SQL with bind variables is not the same.
        // The order of records is preserved.
        int[] result = ctx.batchInsert(sr3, sr2, sr1)
                .execute();

        System.out.println("EXAMPLE 4: " + Arrays.toString(result));
    }

    // batch collection of Objects    
    public void batchInsertCollectionOfObjects() {

        List<SimpleSale> sales = List.of(
                new SimpleSale(2005, 1370L, 1282.64, 1, 0.0),
                new SimpleSale(2004, 1370L, 3938.24, 1, 0.0),
                new SimpleSale(2004, 1370L, 4676.14, 1, 0.0)
        );

        BatchBindStep batch = ctx.batch(
                ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values((Integer) null, null, null, null, null)
        );

        sales.forEach(s -> batch.bind(s.getFiscalYear(), s.getEmployeeNumber(), s.getSale()));
        batch.execute();
    }

    public void forceNumberOfBatches() {

        // by default the generated INSERT has 3 placeholders
        // insert into `classicmodels`.`sale` (`fiscal_year`, `sale`, `trend`) values (?, ?, ?)
        SaleRecord sr1 = new SaleRecord();
        sr1.setSaleId(pk());
        sr1.setFiscalYear(2003);
        sr1.setTrend("UP");
        sr1.setSale(34493.22);
        sr1.setFiscalMonth(1);
        sr1.setRevenueGrowth(0.0);

        // by default the generated INSERT has 4 placeholders
        // insert into `classicmodels`.`sale` (`fiscal_year`, `sale`, `employee_number`, `hot`) values (?, ?, ?, ?)
        SaleRecord sr2 = new SaleRecord();
        sr2.setSaleId(pk());
        sr2.setEmployeeNumber(1370L);
        sr2.setSale(4522.34);
        sr2.setFiscalYear(2005);
        sr2.setHot("1");
        sr2.setFiscalMonth(1);
        sr2.setRevenueGrowth(0.0);

        // in this context, there will be 2 batches, but we can force a single batch if
        // we force a insert having the same string, and for this we can
        // enforce all INSERT statements to be the same by 
        // seting all changed flags of each individual record to true
        sr1.changed(true);
        sr2.changed(true);

        // a single batch is executed having this INSERT
        // insert into `classicmodels`.`sale` (`sale_id`, `fiscal_year`, `sale`, `employee_number`, `hot`, `rate`, `vat`, `trend`) values (?, ?, ?, ?, ?, ?, ?, ?)
        ctx.batchInsert(sr1, sr2).execute();
    }

    private Long pk() {

        return (long) (Math.random() * 99999999);
    }
}
