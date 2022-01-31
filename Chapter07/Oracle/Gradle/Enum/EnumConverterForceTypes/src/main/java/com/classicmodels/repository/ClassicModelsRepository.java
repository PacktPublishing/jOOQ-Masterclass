package com.classicmodels.repository;

import com.classicmodels.enums.StarType;
import com.classicmodels.enums.TrendType;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import static jooq.generated.tables.Sale.SALE;
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

    @Transactional
    public void insertSale() {
        
        // use SaleRateStarConverter
        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH,
                SALE.EMPLOYEE_NUMBER, SALE.RATE)
                .values(2005, 56444.32, 1, 0.0, 1370L, StarType.FIVE_STARS)
                .execute();

        // use SaleVatIntConverter
        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH,
                SALE.EMPLOYEE_NUMBER, SALE.VAT)
                .values(2005, 56444.32, 1, 0.0, 1370L, 19)
                .execute();
        
        // use SaleStrTrendConverter
        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH,
                SALE.EMPLOYEE_NUMBER, SALE.TREND)
                .values(2005, 56444.32, 1, 0.0, 1370L, TrendType.UP)
                .execute();   
        
        // use all three converters
        ctx.insertInto(SALE)
                .values(ThreadLocalRandom.current().nextInt(10000, 20000), // random PK
                        2005, 56444.32, 1370L, 0, 
                        StarType.FIVE_STARS, 19, 1, 0.0, TrendType.UP)
                .execute();
    }

    public void fetchSale() {        

        List<StarType> stars = ctx.select(SALE.RATE)
                .from(SALE)
                .where(SALE.RATE.isNotNull())
                .fetch(SALE.RATE);

        System.out.println("Stars: " + stars);

        List<Integer> vats = ctx.select(SALE.VAT)
                .from(SALE)
                .where(SALE.VAT.isNotNull())
                .fetch(SALE.VAT);

        System.out.println("Vats: " + vats);
        
        List<TrendType> trends = ctx.select(SALE.TREND)
                .from(SALE)
                .where(SALE.TREND.isNotNull())
                .fetch(SALE.TREND);
        
        System.out.println("Trends: " + trends);
    }    
}
