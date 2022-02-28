package com.classicmodels.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// This code result in:
// Cannot insert duplicate key in object 'dbo.sale'. The duplicate key value is (1).
@Repository
public class ClassicModelsRepository {

    private static final int BATCH_SIZE = 3;

    private final DSLContext ctx;
    private final List queries;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
        this.queries = new ArrayList<>();
        
        this.queries.add(ctx.query("SET IDENTITY_INSERT [sale] ON"));
        this.queries.add(ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(1L, 2000, 1370L, 1282.64, 1, 0.0));
        this.queries.add(ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(2L, 2001, 1370L, 3938.24, 1, 0.0));
        this.queries.add(ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(3L, 2002, 1370L, 4676.14, 1, 0.0));
        this.queries.add(ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(4L, 2003, 1504L, 1222.64, 1, 0.0));
        this.queries.add(ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(5L, 2004, 1504L, 1938.24, 1, 0.0));
        this.queries.add(ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(6L, 2005, 1504L, 4446.14, 1, 0.0));
        this.queries.add(ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(7L, 2006, 1611L, 5748.24, 1, 0.0));
        // contains a duplicate key that causes rollback
        this.queries.add(ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH).values(1L, 2007, 1611L, 2216.14, 1, 0.0));
        this.queries.add(ctx.query("SET IDENTITY_INSERT [sale] OFF"));
    }

    // Use a single transaction for all batches - an issue rollbacks all batches    
    @Transactional
    public void batchQueriesInOneTransaction() {

        int i = 0;
        while ((i + BATCH_SIZE) <= queries.size()) {

            int[] result = ctx.batch(
                    queries.subList(i, i + BATCH_SIZE)
            ).execute();

            i = i + BATCH_SIZE;

            System.out.println("Result: " + Arrays.toString(result));
        }

        int[] result = ctx.batch(
                queries.subList(i, queries.size())
        ).execute();

        System.out.println("Result: " + Arrays.toString(result));
    }

    @Transactional
    public void batchQueriesInTransactionPerBatch(List queries) {

        if (queries != null) {
            
            queries.add(0, ctx.query("SET IDENTITY_INSERT [sale] ON"));
            queries.add(ctx.query("SET IDENTITY_INSERT [sale] OFF"));        

        int[] result = ctx.batch(queries).execute();
        System.out.println("Result: " + Arrays.toString(result));        
        }        
    }

    @Transactional
    public void cleanSaleTable() {

        ctx.deleteFrom(SALE).execute();
    }
}
