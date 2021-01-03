package com.classicmodels.repository;

import static com.classicmodels.converter.VatConverter.VAT_CONVERTER;
import com.classicmodels.enums.RateType;
import com.classicmodels.enums.VatType;
import java.util.List;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
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

        // store RateType
        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.RATE)
                .values(2005, 56444.32, 1370L,
                        field("?::\"public\".\"rate_type\"", RateType.PLATINUM.name()))
                .execute();

        // rely on VatConverter
        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.EMPLOYEE_NUMBER, SALE.VAT)
                .values(2005, 56444.32, 1370L, 
                        field("?::\"public\".\"vat_type\"", VAT_CONVERTER.to(VatType.MAX)))
                .execute();
    }

    public void fetchSale() {

        List<RateType> rates = ctx.select(SALE.RATE.coerce(String.class))
                .from(SALE)
                .where(SALE.RATE.isNotNull())
                .fetch(SALE.RATE, RateType.class);

        System.out.println("Rates: " + rates);

        List<VatType> vats = ctx.select(SALE.VAT)
                .from(SALE)
                .where(SALE.VAT.isNotNull())
                .fetch(SALE.VAT, VAT_CONVERTER);

        System.out.println("Vats: " + vats);
    }
}