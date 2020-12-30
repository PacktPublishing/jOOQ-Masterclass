package com.classicmodels.repository;

//import static com.classicmodels.converter.SaleRateStarConverter.SALE_RATE_STAR_CONVERTER;
//import static com.classicmodels.converter.SaleStrTrendConverter.SALE_STR_TREND_CONVERTER;
import com.classicmodels.enums.RateType;
//import com.classicmodels.enums.StarType;
import com.classicmodels.enums.TrendType;
import com.classicmodels.enums.VatType;
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

        // rely on <forcedType/> 
        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.RATE)
                .values(2005, 56444.32, 1370L, RateType.PLATINUM)
                .execute();
        
        // rely on SALE_RATE_STAR_CONVERTER to convert from StarType to RateType
         /*ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.RATE)
                .values(2005, 56444.32, 1370L, SALE_RATE_STAR_CONVERTER.to(StarType.FIVE_STARS))
                .execute();*/

        // rely on jOOQ generated VatType
        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.VAT)
                .values(2005, 56444.32, 1370L, VatType.MAX)
                .execute();
        
        // rely on SALE_STR_TREND_CONVERTER
        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.TREND)
                .values(2005, 56444.32, 1370L, TrendType.UP)
                .execute();        
    }

    public void fetchSale() {

        List<RateType> rates = ctx.select(SALE.RATE)
                .from(SALE)
                .where(SALE.RATE.isNotNull())
                .fetch(SALE.RATE);
/*
        System.out.println("Rates: " + rates);
        
        List<StarType> stars = ctx.select(SALE.RATE)
                .from(SALE)
                .where(SALE.RATE.isNotNull())
                .fetch(SALE.RATE, SALE_RATE_STAR_CONVERTER);

        System.out.println("Stars: " + stars);
*/
        List<String> vats = ctx.select(SALE.VAT)
                .from(SALE)
                .where(SALE.VAT.isNotNull())
                .fetch(SALE.VAT, String.class);

        System.out.println("Vats: " + vats);

        List<TrendType> trends = ctx.select(SALE.TREND)
                .from(SALE)
                .where(SALE.TREND.isNotNull())
                .fetch(SALE.TREND);

        System.out.println("Trends: " + trends);
    }
}
