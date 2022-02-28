package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleSale;
import java.util.Arrays;
import java.util.List;
import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
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

    public void batchUpdateStatements() {

        // batch updates (several queries)
        int[] result1 = ctx.batch(
                ctx.update(EMPLOYEE)
                        .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(1_000))
                        .where(EMPLOYEE.SALARY.between(100_000, 120_000)),
                ctx.update(EMPLOYEE)
                        .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(5_000))
                        .where(EMPLOYEE.SALARY.between(65_000, 80_000)),
                ctx.update(EMPLOYEE)
                        .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(10_000))
                        .where(EMPLOYEE.SALARY.between(55_000, 60_000)),
                ctx.update(EMPLOYEE)
                        .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(15_000))
                        .where(EMPLOYEE.SALARY.between(50_000, 50_000))) // or simply, eq(50_000)
                .execute();

        System.out.println("EXAMPLE 1.1: " + Arrays.toString(result1));

        // batch updates (single query) PreparedStatement
        int[] result21 = ctx.batch(
                ctx.update(EMPLOYEE)
                        .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus((Integer) null))
                        .where(EMPLOYEE.SALARY.between((Integer) null, (Integer) null)))
                .bind(1_000, 100_000, 120_000)
                .bind(5_000, 65_000, 80_000)
                .bind(10_000, 55_000, 60_000)
                .bind(15_000, 50_000, 50_000)
                .execute();

        System.out.println("EXAMPLE 1.2.1: " + Arrays.toString(result21));

        // batch updates (single query) Statement - this is like EXAMPLE 1.1
        int[] result22 = ctx.configuration().derive(
                new Settings().withStatementType(StatementType.STATIC_STATEMENT))
                .dsl().batch(
                        ctx.update(EMPLOYEE)
                                .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus((Integer) null))
                                .where(EMPLOYEE.SALARY.between((Integer) null, (Integer) null)))
                .bind(1_000, 100_000, 120_000)
                .bind(5_000, 65_000, 80_000)
                .bind(10_000, 55_000, 60_000)
                .bind(15_000, 50_000, 50_000)
                .execute();

        System.out.println("EXAMPLE 1.2.2: " + Arrays.toString(result22));
    }

    public void batchUpdateRecords1() {

        List<SaleRecord> sales = ctx.selectFrom(SALE)
                .orderBy(SALE.SALE_ID)
                .limit(3)
                .fetch();

        sales.get(0).setTrend("UP");
        sales.get(0).setFiscalYear(2004);
        sales.get(1).setTrend("CONSTANT");
        sales.get(1).setFiscalYear(2007);
        sales.get(2).setTrend("DOWN");
        sales.get(2).setFiscalYear(2003);

        // There is a single batch since the generated SQL with bind variables is the same for sr1-sr5.
        // The order of records is preserved.
        
        // Records batch updates (single query, bind values)
        int[] result = ctx.batchUpdate(sales) // or, .batchUpdate(sales.get(0), sales.get(1), sales.get(2))
                .execute();
        
        // Records batch updates (multiple query, inlined values)
        // Order of records is always preserved entirely
        // ctx.configuration().derive(
        //        new Settings().withStatementType(StatementType.STATIC_STATEMENT))
        //        .dsl().batchUpdate(sales) ...

        System.out.println("EXAMPLE 2: " + Arrays.toString(result));
    }

    public void batchUpdateRecords2() {

        List<SaleRecord> sales = ctx.selectFrom(SALE)
                .orderBy(SALE.SALE_ID)
                .limit(3)
                .fetch();

        List<BankTransactionRecord> trans = ctx.selectFrom(BANK_TRANSACTION)
                .orderBy(BANK_TRANSACTION.TRANSACTION_ID)
                .limit(2)
                .fetch();

        sales.get(0).setTrend("CONSTANT");
        sales.get(0).setFiscalYear(2010);
        sales.get(1).setTrend("DOWN");
        sales.get(1).setFiscalYear(2011);
        sales.get(2).setTrend("UP");
        sales.get(2).setFiscalYear(2012);

        trans.get(0).setBankName("Transavia Bank");
        trans.get(1).setBankName("N/A");

        // There are two batches, one for SaleRecord and one for BankTransactionRecord.
        // The order of records is not preserved (check the log).
        int[] result = ctx.batchUpdate(trans.get(1), sales.get(0), sales.get(2), trans.get(0), sales.get(1))
                .execute();

        System.out.println("EXAMPLE 3: " + Arrays.toString(result));
    }

    public void batchUpdateRecords3() {

        List<SaleRecord> sales = ctx.selectFrom(SALE)
                .orderBy(SALE.SALE_ID)
                .limit(3)
                .fetch();

        sales.get(0).setTrend("CONSTANT");
        sales.get(0).setFiscalYear(2008);
        sales.get(1).setSale(5664.2);
        sales.get(2).setFiscalYear(2009);
        sales.get(2).setEmployeeNumber(1504L);

        // There are three batches, one for each SaleRecord because the generated SQL with bind variables is not the same.
        // The order of records is preserved.
        int[] result = ctx.batchUpdate(sales) // or, .batchUpdate(sales.get(0), sales.get(1), sales.get(2))
                .execute();

        System.out.println("EXAMPLE 4: " + Arrays.toString(result));
    }

    // batch collection of Objects
    public void batchUpdateCollectionOfObjects() {

        List<SimpleSale> sales = List.of(
                new SimpleSale(2005, 1370L, 1282.64),
                new SimpleSale(2004, 1370L, 3938.24),
                new SimpleSale(2004, 1370L, 4676.14)
        );

        BatchBindStep batch = ctx.batch(
                ctx.update(SALE)
                        .set(SALE.FISCAL_YEAR, (Integer) null)
                        .set(SALE.EMPLOYEE_NUMBER, (Long) null)
                        .set(SALE.SALE_, (Double) null)
        );

        sales.forEach(s -> batch.bind(s.getFiscalYear(), s.getEmployeeNumber(), s.getSale()));
        batch.execute();
    }
}
