package com.classicmodels.repository;

import com.classicmodels.converter.YearMonthConverter;
import java.time.YearMonth;
import java.util.List;
import static jooq.generated.tables.Productlinedetail.PRODUCTLINEDETAIL;
import org.jooq.Converter;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.SQLDataType.INTEGER;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public static final Converter<?, YearMonth> YEARMONTH_INTEGER_CONVERTER
            = INTEGER.asConvertedDataType(new YearMonthConverter()).getConverter();
    
    public static final DataType<YearMonth> YEARMONTH_INTEGER_TYPE
            = INTEGER.asConvertedDataType(new YearMonthConverter());

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Transactional
    public void q() {

        List<YearMonth> x
                = ctx.select(PRODUCTLINEDETAIL.LINE_TYPE).from(PRODUCTLINEDETAIL)
                        .fetch(0, YEARMONTH_INTEGER_CONVERTER);

        System.out.println("z=" + x);

        Field<Integer> value = DSL.val(YearMonth.of(2020, 20), PRODUCTLINEDETAIL.LINE_TYPE);
        YEARMONTH_INTEGER_TYPE.getConverter().to(YearMonth.of(2020, 20));
        
        //String productLine, Long code, String lineCapacity, Integer lineType
        ctx.insertInto(PRODUCTLINEDETAIL, PRODUCTLINEDETAIL.PRODUCT_LINE,
                PRODUCTLINEDETAIL.CODE, PRODUCTLINEDETAIL.LINE_CAPACITY, 
                PRODUCTLINEDETAIL.LINE_TYPE)
                .values(val("Classic Cars"),val(453435L),val("4FG5"),value)
                .execute();
    }
}
