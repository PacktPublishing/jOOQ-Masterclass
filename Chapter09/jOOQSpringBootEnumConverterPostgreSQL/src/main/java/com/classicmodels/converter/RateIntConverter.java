package com.classicmodels.converter;

import jooq.generated.enums.RateType;
import org.jooq.Converter;

public class RateIntConverter implements Converter<RateType, Integer> {

    public final static RateIntConverter RATE_INT_CONVERTER = new RateIntConverter();

    @Override
    public Integer from(RateType t) {

        if (t != null) {

            return switch (t) {
                case SILVER ->
                    10;
                case GOLD ->
                    100;
                case PLATINUM ->
                    1000;
            };
        }

        return null;
    }

    @Override
    public RateType to(Integer u) {

        if (u != null) {

            return switch (u) {
                case 10 ->
                    RateType.SILVER;
                case 100 ->
                    RateType.GOLD;
                case 1000 ->
                    RateType.PLATINUM;
                default ->
                    throw new IllegalArgumentException("Invalid number");
            };
        }

        return null;
    }

    @Override
    public Class<RateType> fromType() {
        return RateType.class;
    }

    @Override
    public Class<Integer> toType() {
        return Integer.class;
    }

}
