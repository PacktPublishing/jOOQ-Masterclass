package com.classicmodels.converter;

import static com.classicmodels.converter.YearMonthConverter.YEARMONTH;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import org.jooq.Converter;
import org.jooq.DataType;

public class YearMonthToDateConverter implements Converter<Date, YearMonth> {

    public static final Converter<Date, YearMonth> DATE_YEARMONTH_CONVERTER
            = new YearMonthToDateConverter();

    public static final Converter<Date[], YearMonth[]> DATE_YEARMONTH_ARR_CONVERTER
            = DATE_YEARMONTH_CONVERTER.forArrays();

    public static final DataType<Date> DATE
            = YEARMONTH.asConvertedDataType(DATE_YEARMONTH_CONVERTER.inverse());

    @Override
    public YearMonth from(Date t) {

        if (t != null) {
            return YearMonth.from(t.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }

        return null;
    }

    @Override
    public Date to(YearMonth u) {

        if (u != null) {
            return Date.from(u.atDay(1)
                    .atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        return null;
    }

    @Override
    public Class<Date> fromType() {
        return Date.class;
    }

    @Override
    public Class<YearMonth> toType() {
        return YearMonth.class;
    }

}
