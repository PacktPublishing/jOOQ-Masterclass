package com.classicmodels.repository;

import com.classicmodels.enums.StarType;
import com.classicmodels.enums.TrendType;
import java.util.List;
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

        // no explicit converter
        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH,
                SALE.EMPLOYEE_NUMBER, SALE.RATE)
                .values(2005, 56444.32, 1, 0.0, 1370L, StarType.FIVE_STARS)
                .execute();

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
    }

    public void fetchSale() {

        List<StarType> rates = ctx.select(SALE.RATE)
                .from(SALE)
                .where(SALE.RATE.isNotNull())
                .fetch(SALE.RATE);

        System.out.println("Rates: " + rates);

        // convert from RateType to StarType via explicit call of the converter
        List<StarType> stars = ctx.select(SALE.RATE)
                .from(SALE)
                .where(SALE.RATE.isNotNull())
                .fetch(SALE.RATE);

        System.out.println("Stars: " + stars);

        // convert from VatType to Integer via explicit call of the converter
        List<Integer> ints = ctx.select(SALE.VAT)
                .from(SALE)
                .where(SALE.VAT.isNotNull())
                .fetch(SALE.VAT);

        System.out.println("Stars as integers: " + ints);
        
        // convert from Object to TrendType via explicit call of the converter
        List<TrendType> trends = ctx.select(SALE.TREND)
                .from(SALE)
                .where(SALE.TREND.isNotNull())
                .fetch(SALE.TREND);
        
        System.out.println("Trends: " + trends);
    }    
}
