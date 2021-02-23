package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleSale;
import java.math.BigDecimal;
import java.math.BigInteger;
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
                        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_)
                                .values(BigInteger.valueOf(2005), 1370L, 1282.64),
                        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_)
                                .values(BigInteger.valueOf(2004), 1370L, 3938.24),
                        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_)
                                .values(BigInteger.valueOf(2004), 1370L, 4676.14),
                        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_)
                                .values(BigInteger.valueOf(2003), 1166L, 2223.0),
                        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_)
                                .values(BigInteger.valueOf(2004), 1166L, 4531.35),
                        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_)
                                .values(BigInteger.valueOf(2004), 1166L, 6751.33)
                ).execute();

        System.out.println("EXAMPLE 1.1: " + Arrays.toString(result1));

        // batch inserts (single query) PreparedStatement
        int[] result21 = ctx.batch(
                        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_)
                                .values((BigInteger) null, null, null))
                .bind(2005, 1370L, 1282.64)
                .bind(2004, 1370L, 3938.24)
                .bind(2004, 1370L, 4676.14)
                .bind(2003, 1166L, 2223.0)
                .bind(2004, 1166L, 4531.35)
                .bind(2004, 1166L, 6751.33)
                .execute();

        System.out.println("EXAMPLE 1.2.1: " + Arrays.toString(result21));
        
        // batch inserts (single query) Statement
        int[] result22 = ctx.configuration().derive(
                new Settings().withStatementType(StatementType.STATIC_STATEMENT))
                .dsl().batch(
                        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_)
                                .values((BigInteger) null, null, null))
                .bind(2005, 1370L, 1282.64)
                .bind(2004, 1370L, 3938.24)
                .bind(2004, 1370L, 4676.14)
                .bind(2003, 1166L, 2223.0)
                .bind(2004, 1166L, 4531.35)
                .bind(2004, 1166L, 6751.33)
                .execute();

        System.out.println("EXAMPLE 1.2.2: " + Arrays.toString(result22));
        
        // batch inserts in a table having manually assigned primary key
        int[] result3 = ctx.batch(
                        ctx.insertInto(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.LAST_NAME, EMPLOYEE.FIRST_NAME, EMPLOYEE.EXTENSION,
                                EMPLOYEE.EMAIL, EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY, EMPLOYEE.JOB_TITLE)
                                .values(10L, "Toga", "Alison", "x3332", "talison@classicmodelcars.com", "1", BigInteger.valueOf(110000), "VP Sales")
                                .onDuplicateKeyIgnore(),
                        ctx.insertInto(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.LAST_NAME, EMPLOYEE.FIRST_NAME, EMPLOYEE.EXTENSION,
                                EMPLOYEE.EMAIL, EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY, EMPLOYEE.JOB_TITLE)
                                .values(11L, "Marius", "Pologa", "x5332", "mpologa@classicmodelcars.com", "3", BigInteger.valueOf(50000), "Sales Rep")
                                .onDuplicateKeyIgnore(),
                        ctx.insertInto(EMPLOYEE, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.LAST_NAME, EMPLOYEE.FIRST_NAME, EMPLOYEE.EXTENSION,
                                EMPLOYEE.EMAIL, EMPLOYEE.OFFICE_CODE, EMPLOYEE.SALARY, EMPLOYEE.JOB_TITLE)
                                .values(12L, "Ana", "Arica", "x5111", "aarica@classicmodelcars.com", "3", BigInteger.valueOf(50000), "Sales Rep")
                                .onDuplicateKeyIgnore()
                ).execute();

        System.out.println("EXAMPLE 1.3: " + Arrays.toString(result3));
    }

    public void batchInsertRecords1() {

        SaleRecord sr1 = new SaleRecord(null, BigInteger.valueOf(2005), 1223.23, 1370L, null, null, null, null);
        SaleRecord sr2 = new SaleRecord(null, BigInteger.valueOf(2004), 543.33, 1166L, null, null, null, null);
        SaleRecord sr3 = new SaleRecord(null, BigInteger.valueOf(2005), 9022.21, null, null, null, null, null);
        SaleRecord sr4 = new SaleRecord(null, BigInteger.valueOf(2003), 4333.22, 1504L, null, null, null, null);
        SaleRecord sr5 = new SaleRecord(null, BigInteger.valueOf(2003), 8002.22, 1504L, null, null, null, "UP");

        List<SaleRecord> sales = List.of(sr5, sr2, sr3, sr4, sr1);

        // There is a single batch since the generated SQL with bind variables is the same for sr1-sr5.
        // The order of records is preserved.
        int[] result = ctx.batchInsert(sales)
                // or, .batchInsert(sr5, sr2, sr3, sr4, sr1)
                .execute();

        System.out.println("EXAMPLE 2: " + Arrays.toString(result));
    }

    public void batchInsertRecords2() {

        SaleRecord sr1 = new SaleRecord(null, BigInteger.valueOf(2005), 1223.23, 1370L, null, null, null, null);
        SaleRecord sr2 = new SaleRecord(null, BigInteger.valueOf(2004), 543.33, 1166L, null, null, null, null);
        SaleRecord sr3 = new SaleRecord(null, BigInteger.valueOf(2005), 9022.21, null, null, null, null, null);
        BankTransactionRecord bt1 = new BankTransactionRecord(
                null, "N/A", "N/A", BigDecimal.ZERO, LocalDateTime.now(), 114L, "NP603840", "N/A");
        SaleRecord sr4 = new SaleRecord(null, BigInteger.valueOf(2003), 4333.22, 1504L, null, null, null, null);
        SaleRecord sr5 = new SaleRecord(null, BigInteger.valueOf(2003), 8002.22, 1504L, null, null, null, "UP");
        BankTransactionRecord bt2 = new BankTransactionRecord(
                null, "N/A", "N/A", BigDecimal.ZERO, LocalDateTime.now(), 114L, "NP603840", "N/A");

        // There are two batches, one for SaleRecord and one for BankTransactionRecord.
        // The order of records is not preserved (check the log).
        int[] result = ctx.batchInsert(bt1, sr1, sr2, sr3, sr4, sr5, bt2)
                .execute();

        System.out.println("EXAMPLE 3: " + Arrays.toString(result));
    }

    public void batchInsertRecords3() {

        SaleRecord sr1 = new SaleRecord();
        sr1.setFiscalYear(BigInteger.valueOf(2005));
        sr1.setSale(1223.23);
        sr1.setEmployeeNumber(1370L);
        sr1.setTrend("UP");

        SaleRecord sr2 = new SaleRecord();
        sr2.setFiscalYear(BigInteger.valueOf(2005));
        sr2.setSale(9022.21);

        SaleRecord sr3 = new SaleRecord();
        sr3.setFiscalYear(BigInteger.valueOf(2003));
        sr3.setSale(8002.22);
        sr3.setEmployeeNumber(1504L);

        // There are three batches, one for each SaleRecord because the generated SQL with bind variables is not the same.
        // The order of records is preserved.
        int[] result = ctx.batchInsert(sr3, sr2, sr1)
                .execute();

        System.out.println("EXAMPLE 4: " + Arrays.toString(result));
    }

    // batch collection of Objects    
    public void batchInsertCollectionOfObjects() {

        List<SimpleSale> sales = List.of(
                new SimpleSale(2005, 1370L, 1282.64),
                new SimpleSale(2004, 1370L, 3938.24),
                new SimpleSale(2004, 1370L, 4676.14)
        );

        BatchBindStep batch = ctx.batch(
                ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_)
                        .values((BigInteger) null, null, null)
        );

        sales.forEach(s -> batch.bind(s.getFiscalYear(), s.getEmployeeNumber(), s.getSale()));
        batch.execute();
    }
}