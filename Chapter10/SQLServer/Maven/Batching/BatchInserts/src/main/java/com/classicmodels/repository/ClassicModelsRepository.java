package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleSale;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.BankTransactionRecord;
import jooq.generated.tables.records.SaleRecord;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
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

    public void batchInsertStatements() {

        // batch inserts in a table having auto-generated primary key (several queries)
        int[] result1 = ctx.batch(
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

        System.out.println("EXAMPLE 1.1: " + Arrays.toString(result1));

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

        int[] result4 = ctx.batch(
                ctx.query("SET IDENTITY_INSERT [sale] ON"),
                ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(pk(), 2005, 1370L, 1282.64, 1, 0.0),
                ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(pk(), 2004, 1370L, 3938.24, 1, 0.0),
                ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(pk(), 2004, 1370L, 4676.14, 1, 0.0),
                ctx.query("SET IDENTITY_INSERT [sale] OFF")
        ).execute();

        System.out.println("EXAMPLE 1.4: " + Arrays.toString(result4));
    }

    public void batchInsertRecords1() {

        // Records batch inserts (single query, bind values)
        SaleRecord sr11 = new SaleRecord();
        SaleRecord sr12 = new SaleRecord();
        SaleRecord sr13 = new SaleRecord();
        SaleRecord sr14 = new SaleRecord();
        SaleRecord sr15 = new SaleRecord();

        sr11.setFiscalYear(2005);
        sr11.setSale(1223.23);
        sr11.setEmployeeNumber(1370L);
        sr11.setFiscalMonth(1);
        sr11.setRevenueGrowth(0.0);
        sr12.setFiscalYear(2004);
        sr12.setSale(543.33);
        sr12.setEmployeeNumber(1166L);
        sr12.setFiscalMonth(1);
        sr12.setRevenueGrowth(0.0);
        sr13.setFiscalYear(2005);
        sr13.setSale(9022.21);
        sr13.setEmployeeNumber(1370L);
        sr13.setFiscalMonth(1);
        sr13.setRevenueGrowth(0.0);
        sr14.setFiscalYear(2003);
        sr14.setSale(4333.22);
        sr14.setEmployeeNumber(1504L);
        sr14.setFiscalMonth(1);
        sr14.setRevenueGrowth(0.0);
        sr15.setFiscalYear(2003);
        sr15.setSale(8002.22);
        sr15.setEmployeeNumber(1504L);
        sr15.setFiscalMonth(1);
        sr15.setRevenueGrowth(0.0);

        List<SaleRecord> sales1 = List.of(sr15, sr12, sr13, sr14, sr11);

        // There is a single batch since the generated SQL with bind variables is the same for sr11-sr15.
        // The order of records is preserved.                
        int[] result1 = ctx.batchInsert(sales1)
                // or, .batchInsert(sr15, sr12, sr13, sr14, sr11)
                .execute();

        System.out.println("EXAMPLE 2.1: " + Arrays.toString(result1));

        // Records batch inserts (multiple query, inlined values)
        SaleRecord sr21 = new SaleRecord();
        SaleRecord sr22 = new SaleRecord();
        SaleRecord sr23 = new SaleRecord();
        SaleRecord sr24 = new SaleRecord();
        SaleRecord sr25 = new SaleRecord();

        sr21.setFiscalYear(2005);
        sr21.setSale(1223.23);
        sr21.setEmployeeNumber(1370L);
        sr21.setFiscalMonth(1);
        sr21.setRevenueGrowth(0.0);
        sr22.setFiscalYear(2004);
        sr22.setSale(543.33);
        sr22.setEmployeeNumber(1166L);
        sr22.setFiscalMonth(1);
        sr22.setRevenueGrowth(0.0);
        sr23.setFiscalYear(2005);
        sr23.setSale(9022.21);
        sr23.setEmployeeNumber(1370L);
        sr23.setFiscalMonth(1);
        sr23.setRevenueGrowth(0.0);
        sr24.setFiscalYear(2003);
        sr24.setSale(4333.22);
        sr24.setEmployeeNumber(1504L);
        sr24.setFiscalMonth(1);
        sr24.setRevenueGrowth(0.0);
        sr25.setFiscalYear(2003);
        sr25.setSale(8002.22);
        sr25.setEmployeeNumber(1504L);
        sr25.setFiscalMonth(1);
        sr25.setRevenueGrowth(0.0);

        List<SaleRecord> sales2 = List.of(sr25, sr22, sr23, sr24, sr21);

        // There is a single batch since the generated SQL with bind variables is the same for sr11-sr15.        
        // Order of records is always preserved entirely
        int[] result2 = ctx.configuration().derive(new Settings()
                .withStatementType(StatementType.STATIC_STATEMENT)
                .withRenderOutputForSQLServerReturningClause(Boolean.FALSE))
                .dsl().batchInsert(sales2)
                // or, .batchInsert(sr25, sr22, sr23, sr24, sr21)
                .execute();

        System.out.println("EXAMPLE 2.2: " + Arrays.toString(result2));
    }

    public void batchInsertRecords2() {

        SaleRecord sr1 = new SaleRecord();
        SaleRecord sr2 = new SaleRecord();
        SaleRecord sr3 = new SaleRecord();
        SaleRecord sr4 = new SaleRecord();
        SaleRecord sr5 = new SaleRecord();

        sr1.setFiscalYear(2005);
        sr1.setSale(1223.23);
        sr1.setEmployeeNumber(1370L);
        sr1.setFiscalMonth(1);
        sr1.setRevenueGrowth(0.0);
        sr2.setFiscalYear(2004);
        sr2.setSale(543.33);
        sr2.setEmployeeNumber(1166L);
        sr2.setFiscalMonth(1);
        sr2.setRevenueGrowth(0.0);
        sr3.setFiscalYear(2005);
        sr3.setSale(9022.21);
        sr3.setEmployeeNumber(1504L);
        sr3.setFiscalMonth(1);
        sr3.setRevenueGrowth(0.0);
        sr4.setFiscalYear(2003);
        sr4.setSale(4333.22);
        sr4.setEmployeeNumber(1504L);
        sr4.setFiscalMonth(1);
        sr4.setRevenueGrowth(0.0);
        sr5.setFiscalYear(2003);
        sr5.setSale(8002.22);
        sr5.setEmployeeNumber(1504L);
        sr5.setFiscalMonth(1);
        sr5.setRevenueGrowth(0.0);

        BankTransactionRecord bt1 = new BankTransactionRecord();
        BankTransactionRecord bt2 = new BankTransactionRecord();

        bt1.setBankName("");
        bt1.setBankIban("");
        bt1.setTransferAmount(BigDecimal.ZERO);
        bt1.setCachingDate(LocalDateTime.now());
        bt1.setCustomerNumber(114L);
        bt1.setCheckNumber("NP603840");
        bt1.setStatus("");
        bt1.setCardType("");
        bt2.setBankName("");
        bt2.setBankIban("");
        bt2.setTransferAmount(BigDecimal.ZERO);
        bt2.setCachingDate(LocalDateTime.now());
        bt2.setCustomerNumber(114L);
        bt2.setCheckNumber("NP603840");
        bt2.setStatus("");
        bt2.setCardType("");

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
        sr1.setTrend("UP");
        sr1.setFiscalMonth(1);
        sr1.setRevenueGrowth(0.0);

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

        sales.forEach(s -> batch.bind(s.getFiscalYear(), s.getEmployeeNumber(), 
                s.getSale(), s.getFiscalMonth(), s.getRevenueGrowth()));
        batch.execute();
    }

    public void forceNumberOfBatches() {

        // by default the generated INSERT has 3 placeholders
        // insert into `classicmodels`.`sale` (`fiscal_year`, `sale`, `trend`, `fiscal_month`, `revenue_growth`) values (?, ?, ?, ?, ?)
        SaleRecord sr1 = new SaleRecord();
        sr1.setFiscalYear(2003);
        sr1.setTrend("UP");
        sr1.setSale(34493.22);
        sr1.setFiscalMonth(1);
        sr1.setRevenueGrowth(0.0);

        // by default the generated INSERT has 4 placeholders
        // insert into `classicmodels`.`sale` (`fiscal_year`, `sale`, `employee_number`, `hot`, `fiscal_month`, `revenue_growth`) values (?, ?, ?, ?, ?, ?)
        SaleRecord sr2 = new SaleRecord();        
        sr2.setEmployeeNumber(1370L);
        sr2.setSale(4522.34);
        sr2.setFiscalYear(2005);
        sr2.setHot(true);
        sr2.setFiscalMonth(1);
        sr2.setRevenueGrowth(0.0);

        // in this context, there will be 2 batches, but we can force a single batch if
        // we force a insert having the same string, and for this we can
        // enforce all INSERT statements to be the same by 
        // seting all changed flags of each individual record to true
        sr1.changed(true);
        sr2.changed(true);
        sr1.changed(0, false);
        sr2.changed(0, false);

        // a single batch is executed having this INSERT
        // insert into `classicmodels`.`sale` (`sale_id`, `fiscal_year`, `sale`, `employee_number`, `hot`, `rate`, `vat`, `trend`, `fiscal_month`, `revenue_growth`) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        ctx.batchInsert(sr1, sr2).execute();
    }
    
    private long pk() {
        
        return (long) (Math.random() * 99999999);
    }
}