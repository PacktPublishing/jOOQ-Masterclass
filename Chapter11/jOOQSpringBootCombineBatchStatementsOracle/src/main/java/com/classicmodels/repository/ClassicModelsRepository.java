package com.classicmodels.repository;

import java.math.BigInteger;
import java.util.Arrays;
import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.conf.Settings;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void combineBatch() {

        // combine batch
        int[] result = ctx.configuration().derive(
                new Settings().withBatchSize(3))
                .dsl().batch(
                        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_).values(BigInteger.valueOf(2005), 1370L, 1282.64),
                        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_).values(BigInteger.valueOf(2004), 1370L, 3938.24),
                        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_).values(BigInteger.valueOf(2004), 1370L, 4676.14),
                        ctx.update(EMPLOYEE)
                                .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(1_000))
                                .where(EMPLOYEE.SALARY.between(BigInteger.valueOf(100_000), BigInteger.valueOf(120_000))),
                        ctx.update(EMPLOYEE)
                                .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(5_000))
                                .where(EMPLOYEE.SALARY.between(BigInteger.valueOf(65_000), BigInteger.valueOf(80_000))),
                        ctx.update(EMPLOYEE)
                                .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(10_000))
                                .where(EMPLOYEE.SALARY.between(BigInteger.valueOf(55_000), BigInteger.valueOf(60_000))),
                        ctx.deleteFrom(BANK_TRANSACTION).where(BANK_TRANSACTION.TRANSACTION_ID.eq(1L)),
                        ctx.deleteFrom(BANK_TRANSACTION).where(BANK_TRANSACTION.TRANSACTION_ID.eq(2L)),
                        ctx.deleteFrom(BANK_TRANSACTION).where(BANK_TRANSACTION.TRANSACTION_ID.eq(3L)),
                        ctx.deleteFrom(BANK_TRANSACTION).where(BANK_TRANSACTION.TRANSACTION_ID.eq(4L))
                ).execute();

        System.out.println("EXAMPLE 1: " + Arrays.toString(result));
    }
}
