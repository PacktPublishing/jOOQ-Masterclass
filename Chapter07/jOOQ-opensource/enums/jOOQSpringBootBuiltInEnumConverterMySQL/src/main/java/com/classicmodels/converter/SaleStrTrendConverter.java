package com.classicmodels.converter;

import com.classicmodels.enums.TrendType;
import org.jooq.DataType;
import org.jooq.impl.EnumConverter;
import static org.jooq.impl.SQLDataType.VARCHAR;

public class SaleStrTrendConverter extends EnumConverter<String, TrendType> {

    public final static SaleStrTrendConverter SALE_STR_TREND_CONVERTER
            = new SaleStrTrendConverter();

    public final static DataType<TrendType> SALE_STR_TREND_TYPE
            = VARCHAR.asConvertedDataType(SALE_STR_TREND_CONVERTER);

    public SaleStrTrendConverter() {
        super(String.class, TrendType.class);
    }        
}
