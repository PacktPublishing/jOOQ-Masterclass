package com.classicmodels.converter;

import java.time.YearMonth;
import org.jooq.Converter;

public class YearMonthConverter implements Converter<Integer, YearMonth> {

    @Override
    public YearMonth from(Integer t) {

        if (t != null) {
            return YearMonth.of(t / 100, t % 100);
        }

        return null;
    }

    @Override
    public Integer to(YearMonth u) {

        if (u != null) {

            return (u.getYear() * 100)
                    + u.getMonth().getValue();
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
