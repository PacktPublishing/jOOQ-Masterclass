package com.classicmodels.repository;

import java.util.Arrays;
import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
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
        int[] result = ctx.batch(
                        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(2005, 1370L, 1282.64, 1, 0.0),
                        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(2004, 1370L, 3938.24, 1, 0.0),
                        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(2004, 1370L, 4676.14, 1, 0.0),
                        
                        ctx.update(EMPLOYEE)
                                .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(1_000))
                                .where(EMPLOYEE.SALARY.between(100_000, 120_000)),
                        ctx.update(EMPLOYEE)
                                .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(5_000))
                                .where(EMPLOYEE.SALARY.between(65_000, 80_000)),
                        ctx.update(EMPLOYEE)
                                .set(EMPLOYEE.SALARY, EMPLOYEE.SALARY.plus(10_000))
                                .where(EMPLOYEE.SALARY.between(55_000, 60_000)),
                        
                        ctx.deleteFrom(BANK_TRANSACTION).where(BANK_TRANSACTION.TRANSACTION_ID.eq(1L)),
                        ctx.deleteFrom(BANK_TRANSACTION).where(BANK_TRANSACTION.TRANSACTION_ID.eq(2L)),
                        ctx.deleteFrom(BANK_TRANSACTION).where(BANK_TRANSACTION.TRANSACTION_ID.eq(3L)),
                        ctx.deleteFrom(BANK_TRANSACTION).where(BANK_TRANSACTION.TRANSACTION_ID.eq(4L))
                ).execute();

        System.out.println("EXAMPLE: " + Arrays.toString(result));

    }
}