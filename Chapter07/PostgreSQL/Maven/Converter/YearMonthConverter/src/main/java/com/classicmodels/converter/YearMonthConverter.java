package com.classicmodels.converter;

import java.time.YearMonth;
import java.time.temporal.ChronoField;
import org.jooq.Converter;
import org.jooq.DataType;
import static org.jooq.impl.SQLDataType.INTEGER;

public class YearMonthConverter implements Converter<Integer, YearMonth> {

    public static final Converter<Integer, YearMonth> INTEGER_YEARMONTH_CONVERTER
            = new YearMonthConverter();

    public static final Converter<Integer[], YearMonth[]> INTEGER_YEARMONTH_ARR_CONVERTER
            = INTEGER_YEARMONTH_CONVERTER.forArrays();

    public static final DataType<YearMonth> YEARMONTH
            = INTEGER.asConvertedDataType(INTEGER_YEARMONTH_CONVERTER);

    @Override
    public YearMonth from(Integer t) {

        if (t != null) {
            
            return YearMonth.of(1970, 1).with(ChronoField.PROLEPTIC_MONTH, t);
        }

        return null;
    }

    @Override
    public Integer to(YearMonth u) {

        if (u != null) {

            return (int) u.getLong(ChronoField.PROLEPTIC_MONTH);
        }

        return null;
    }

    @Override
    public Class<Integer> fromType() {
        return Integer.class;
    }

    @Override
    public Class<YearMonth> toType() {
        return YearMonth.class;
    }

}
