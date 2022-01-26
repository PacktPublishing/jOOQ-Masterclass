package com.classicmodels.repository;

import static com.classicmodels.converter.VatConverter.VATTYPE;
import static com.classicmodels.converter.VatConverter.VAT_CONVERTER;
import com.classicmodels.enums.RateType;
import com.classicmodels.enums.VatType;
import java.util.List;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import static org.jooq.impl.DSL.field;
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

        // store RateType
        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH,
                SALE.EMPLOYEE_NUMBER, SALE.RATE)
                .values(2005, 56444.32, 0, 1.1, 1370L,
                        field("?::\"public\".\"rate_type\"", RateType.PLATINUM.name()))
                .execute();

        // rely on VatConverter
        ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH,
                SALE.EMPLOYEE_NUMBER, SALE.VAT)
                .values(val(2005), val(56444.32), val(1), val(0.0), val(1370L),
                        field("?", jooq.generated.enums.VatType.class, VAT_CONVERTER.to(VatType.MAX)))
                .execute();
    }

    public void fetchSale() {

        List<RateType> rates = ctx.select(SALE.RATE)
                .from(SALE)
                .where(SALE.RATE.isNotNull())
                .fetch(SALE.RATE.coerce(String.class), RateType.class);

        System.out.println("Rates: " + rates);

        List<VatType> vats = ctx.select(SALE.VAT)
                .from(SALE)
                .where(SALE.VAT.isNotNull())
                .fetch(SALE.VAT, VAT_CONVERTER);

        System.out.println("Vats: " + vats);

        Result<Record1<VatType>> maxGood = ctx.select(val("MAX").coerce(VATTYPE)).fetch();
        System.out.println("Result: \n" + maxGood); // MAX

        Result<Record1<VatType>> maxWrong = ctx.select(val("MaX").coerce(VATTYPE)).fetch();
        System.out.println("Result: \n" + maxWrong); // null
    }
}
