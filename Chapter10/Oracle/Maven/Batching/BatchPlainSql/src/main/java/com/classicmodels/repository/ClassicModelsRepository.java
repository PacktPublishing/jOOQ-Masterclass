package com.classicmodels.repository;

import java.util.Arrays;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void batchPlainSql() {
        
        int[] resultPlain1 = ctx.queries(
                query("insert into \"CLASSICMODELS\".\"SALE\" (\"FISCAL_YEAR\", \"EMPLOYEE_NUMBER\", \"SALE\", \"FISCAL_MONTH\", \"REVENUE_GROWTH\") values (2005, 1370, 1.28264E3, 1, 0E0)"), 
                query("insert into \"CLASSICMODELS\".\"SALE\" (\"FISCAL_YEAR\", \"EMPLOYEE_NUMBER\", \"SALE\", \"FISCAL_MONTH\", \"REVENUE_GROWTH\") values (2004, 1370, 3.93824E3, 1, 0E0)"), 
                query("insert into \"CLASSICMODELS\".\"SALE\" (\"FISCAL_YEAR\", \"EMPLOYEE_NUMBER\", \"SALE\", \"FISCAL_MONTH\", \"REVENUE_GROWTH\") values (2004, 1370, 4.67614E3, 1, 0E0)")
        ).executeBatch();
        
        System.out.println("Result 1 (plain SQL): " + Arrays.toString(resultPlain1));
        
        int[] resultPlain2 = ctx.batch(
                query("insert into \"CLASSICMODELS\".\"SALE\" (\"FISCAL_YEAR\", \"EMPLOYEE_NUMBER\", \"SALE\", \"FISCAL_MONTH\", \"REVENUE_GROWTH\") values (2005, 1370, 1.28264E3, 1, 0E0)"), 
                query("insert into \"CLASSICMODELS\".\"SALE\" (\"FISCAL_YEAR\", \"EMPLOYEE_NUMBER\", \"SALE\", \"FISCAL_MONTH\", \"REVENUE_GROWTH\") values (2004, 1370, 3.93824E3, 1, 0E0)"), 
                query("insert into \"CLASSICMODELS\".\"SALE\" (\"FISCAL_YEAR\", \"EMPLOYEE_NUMBER\", \"SALE\", \"FISCAL_MONTH\", \"REVENUE_GROWTH\") values (2004, 1370, 4.67614E3, 1, 0E0)")
        ).execute();
        
        System.out.println("Result 2 (plain SQL): " + Arrays.toString(resultPlain2));
        
        // using DSL
        int[] resultDsl = ctx.batch(
                ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(2005, 1370L, 1282.64, 1, 0.0),
                ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(2004, 1370L, 3938.24, 1, 0.0),
                ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(2004, 1370L, 4676.14, 1, 0.0)                
        ).execute();
        
        System.out.println("Result (DSL): " + Arrays.toString(resultDsl));
    }
}
