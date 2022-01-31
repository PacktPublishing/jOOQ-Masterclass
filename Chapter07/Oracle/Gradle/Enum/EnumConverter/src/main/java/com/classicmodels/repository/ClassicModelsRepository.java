package com.classicmodels.repository;

import static com.classicmodels.converter.SaleVatIntConverter.SALE_VAT_INT_CONVERTER;
import static com.classicmodels.converter.SaleRateStarConverter.SALE_RATE_STAR_CONVERTER;
import static com.classicmodels.converter.SaleStrTrendConverter.SALE_STR_TREND_CONVERTER;
import static com.classicmodels.converter.SaleStrTrendConverter.SALE_STR_TREND_TYPE;
import com.classicmodels.enums.StarType;
import com.classicmodels.enums.TrendType;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.val;
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
                .values(2005, 56444.32, 1, 0.0, 1370L, "PLATINUM")
                .execute();

        // use SALE_RATE_STAR_CONVERTER
        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH,
                SALE.EMPLOYEE_NUMBER, SALE.RATE)
                .values(2005, 56444.32, 1, 0.0, 1370L, SALE_RATE_STAR_CONVERTER.to(StarType.FIVE_STARS))
                .execute();

        // use SALE_VAT_INT_CONVERTER
        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH,
                SALE.EMPLOYEE_NUMBER, SALE.VAT)
                .values(2005, 56444.32, 1, 0.0, 1370L, SALE_VAT_INT_CONVERTER.to(19))
                .execute();
        
        // use SALE_STR_TREND_CONVERTER
        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH,
                SALE.EMPLOYEE_NUMBER, SALE.TREND)
                .values(2005, 56444.32, 1, 0.0, 1370L, SALE_STR_TREND_CONVERTER.to(TrendType.UP))
                .execute();      
        
        // use SALE_STR_TREND_TYPE
        ctx.insertInto(SALE)
                .values(ThreadLocalRandom.current().nextInt(10000, 20000), // random PK
                        2005, 56444.32, 1370L, 0, 
                        SALE_RATE_STAR_CONVERTER.to(StarType.FIVE_STARS),
                        SALE_VAT_INT_CONVERTER.to(19), 1, 0.0,
                        val(TrendType.UP, SALE_STR_TREND_TYPE))
                .execute();
    }

    public void fetchSale() {

        List<String> rates = ctx.select(SALE.RATE)
                .from(SALE)
                .where(SALE.RATE.isNotNull())
                .fetch(SALE.RATE);

        System.out.println("Rates: " + rates);

        // convert from RateType to StarType via explicit call of the converter
        List<StarType> stars = ctx.select(SALE.RATE)
                .from(SALE)
                .where(SALE.RATE.isNotNull())
                .fetch(SALE.RATE, SALE_RATE_STAR_CONVERTER);

        System.out.println("Stars: " + stars);

        // convert from VatType to Integer via explicit call of the converter
        List<Integer> vats = ctx.select(SALE.VAT)
                .from(SALE)
                .where(SALE.VAT.isNotNull())
                .fetch(SALE.VAT, SALE_VAT_INT_CONVERTER);

        System.out.println("Vats: " + vats);
        
        // convert from String to TrendType via explicit call of the converter
        List<TrendType> trends1 = ctx.select(SALE.TREND)
                .from(SALE)
                .where(SALE.TREND.isNotNull())
                .fetch(SALE.TREND, SALE_STR_TREND_CONVERTER);
        
        System.out.println("Trends(1): " + trends1);
        
        // explicit mapping, no converter needed
        List<TrendType> trends2 = ctx.select(SALE.TREND)
                .from(SALE)
                .where(SALE.TREND.isNotNull())
                .fetch()
                .map(rs -> TrendType.valueOf(rs.getValue(SALE.TREND)));
        
        System.out.println("Trends(2): " + trends2);
    }    
}