package com.classicmodels.converter;

import com.classicmodels.enums.TrendType;
import org.jooq.Converter;
import org.jooq.DataType;
import static org.jooq.impl.SQLDataType.VARCHAR;

public class SaleStrTrendConverter implements Converter<String, TrendType> {

    public final static SaleStrTrendConverter SALE_STR_TREND_CONVERTER
            = new SaleStrTrendConverter();

    public final static DataType<TrendType> SALE_STR_TREND_TYPE
            = VARCHAR.asConvertedDataType(SALE_STR_TREND_CONVERTER);

    @Override
    public TrendType from(String u) {

        if (u != null) {

            if (u.equals("UP")) {
                return TrendType.UP;
            } else if (u.equals("DOWN")) {
                return TrendType.DOWN;
            } else if (u.equals("CONSTANT")) {
                return TrendType.CONSTANT;
            }
        }

        return null;
    }

    @Override
    public String to(TrendType t) {

        if (t != null) {

            return switch (t) {
                case UP ->
                    "UP";
                case DOWN ->
                    "DOWN";
                case CONSTANT ->
                    "CONSTANT";
            };
        }

        return null;
    }

    @Override
    public Class<String> fromType() {
        return String.class;
    }

    @Override
    public Class<TrendType> toType() {
        return TrendType.class;
    }

}
