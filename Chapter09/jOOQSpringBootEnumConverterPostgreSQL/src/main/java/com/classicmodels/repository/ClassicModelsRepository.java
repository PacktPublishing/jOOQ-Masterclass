package com.classicmodels.repository;

import static com.classicmodels.converter.RateIntConverter.RATE_INT_CONVERTER;
import static com.classicmodels.converter.RateStarConverter.RATE_STAR_CONVERTER;
import com.classicmodels.enums.StarType;
import java.util.List;
import jooq.generated.enums.RateType;
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
        
        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.RATE)
                .values(2005, 56444.32, 1370L, RateType.PLATINUM)
                .execute();
        
        // convert from StarType to RateType via explicit call of the converter
        // if you want the converter to be applied automatically simply activate 
        // the <forcedType/> section in pom.xml and remove the explicit usage of RATE_STAR_CONVERTER
        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.RATE)
                .values(2005, 56444.32, 1370L, RATE_STAR_CONVERTER.to(StarType.FIVE_STARS))
                .execute();
        
        // convert from Integer to RateType via explicit call of the converter
        // if you want the converter to be applied automatically simply activate 
        // the <forcedType/> section in pom.xml and remove the explicit usage of RATE_INT_CONVERTER
        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.RATE)
                .values(2005, 56444.32, 1370L, RATE_INT_CONVERTER.to(1000))
                .execute();
    }

    public void fetchSale() {

        List<RateType> rates = ctx.select(SALE.RATE)
                .from(SALE)
                .where(SALE.RATE.isNotNull())
                .fetch(SALE.RATE);

        System.out.println("Rates: " + rates);
        
        // convert from RateType to StarType via explicit call of the converter
        List<StarType> stars = ctx.select(SALE.RATE)
                .from(SALE)
                .where(SALE.RATE.isNotNull())
                .fetch(SALE.RATE, RATE_STAR_CONVERTER);
        
        System.out.println("Stars: " + stars);
        
        // convert from RateType to Integer via explicit call of the converter
        List<Integer> ints = ctx.select(SALE.RATE)
                .from(SALE)
                .where(SALE.RATE.isNotNull())
                .fetch(SALE.RATE, RATE_INT_CONVERTER);
        
        System.out.println("Stars as integers: " + ints);
    }
}
