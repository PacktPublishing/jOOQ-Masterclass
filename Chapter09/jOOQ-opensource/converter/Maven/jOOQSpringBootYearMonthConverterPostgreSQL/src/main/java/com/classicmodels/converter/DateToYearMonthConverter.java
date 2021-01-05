package com.classicmodels.converter;

import static com.classicmodels.converter.YearMonthConverter.YEARMONTH;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import org.jooq.Converter;
import org.jooq.DataType;

public class DateToYearMonthConverter implements Converter<YearMonth, Date> {

    public static final Converter<YearMonth, Date> YEARMONTH_DATE_CONVERTER
            = new DateToYearMonthConverter();

    public static final Converter<YearMonth[], Date[]> YEARMONTH_DATE_ARR_CONVERTER
            = YEARMONTH_DATE_CONVERTER.forArrays();

    public static final DataType<Date> DATE
            = YEARMONTH.asConvertedDataType(YEARMONTH_DATE_CONVERTER);
    
    @Override
    public Date from(YearMonth t) {

        if (t != null) {
            return Date.from(t.atDay(1)
                    .atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        return null;
    }

    @Override
    public YearMonth to(Date u) {

        if (u != null) {
            return YearMonth.from(u.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }

        return null;
    }

    @Override
    public Class<YearMonth> fromType() {
        return YearMonth.class;
    }

    @Override
    public Class<Date> toType() {
        return Date.class;
    }

}
