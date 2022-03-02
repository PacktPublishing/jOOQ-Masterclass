package com.classicmodels.repository;

import jooq.generated.embeddables.pojos.EmbeddedSalePk;
import jooq.generated.embeddables.records.EmbeddedSalePkRecord;
import static jooq.generated.tables.Sale.SALE;
import static jooq.generated.tables.Token.TOKEN;
import jooq.generated.tables.pojos.Sale;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void fetchSale() {
           
        // Record2<EmbeddedSalePkRecord, Integer>
        var result1 = ctx.select(SALE.SALE_PK, SALE.FISCAL_YEAR)
                .from(SALE)
                .where(SALE.SALE_.gt(5000d))                
                .fetchAny();
        
        System.out.println("EXAMPLE 1:\n");
        System.out.println("PK: " + result1.value1().getSaleId());
        System.out.println("Fiscal year: " + result1.value2());        
        
        // Record2<Double, Integer>
        var result2 = ctx.select(SALE.SALE_, SALE.FISCAL_YEAR)
                .from(SALE)
                .where(SALE.SALE_PK.eq(new EmbeddedSalePkRecord(1L)))                
                .fetchSingle();
        
        System.out.println("EXAMPLE 2:\n");
        System.out.println("Sale: " + result2.value1());
        System.out.println("Fiscal year: " + result1.value2());                
        
        EmbeddedSalePk result3 = ctx.select(SALE.SALE_PK)
                .from(SALE)
                .where(SALE.SALE_.gt(5000d))                
                .fetchAnyInto(EmbeddedSalePk.class);
        
        System.out.println("EXAMPLE 3:\n");
        System.out.println("PK: " + result3.getSaleId());        
        
        Sale result4 = ctx.select(SALE.SALE_PK, SALE.FISCAL_YEAR)
                .from(SALE)
                .where(SALE.SALE_.gt(5000d))                
                .fetchAnyInto(Sale.class);
        
        System.out.println("EXAMPLE 4:\n");
        System.out.println("PK: " + result4.getSaleId());                
        System.out.println("PK: " + result4.getSalePk().getSaleId());                
        System.out.println("Fiscal year: " + result4.getFiscalYear());                
    }
    
    public void fetchToken() {
        
        // Record3<EmbeddedTokenPkRecord, EmbeddedSalePkRecord, Double>
        var result = ctx.select(TOKEN.TOKEN_PK, TOKEN.TOKEN_SALE_FK, TOKEN.AMOUNT)
                .from(TOKEN)
                .where(TOKEN.AMOUNT.gt(1000d))
                .fetchAny();
        
        System.out.println("EXAMPLE 5:\n");
        System.out.println("PK: " + result.value1().getTokenId());
        System.out.println("FK: " + result.value2().getSaleId());
        System.out.println("Amount: " + result.value3());
    }
    
    public void fetchSaleAndToken() {
        
        // Result<Record3<Long, Integer, Double>>
        var result = ctx.select(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR, TOKEN.AMOUNT)
                .from(SALE)
                .join(TOKEN)
                .on(SALE.SALE_PK.eq(TOKEN.TOKEN_SALE_FK))
                .fetch();
        
        System.out.println("EXAMPLE 6:\n");
        System.out.println("Result: " + result);
    }
}