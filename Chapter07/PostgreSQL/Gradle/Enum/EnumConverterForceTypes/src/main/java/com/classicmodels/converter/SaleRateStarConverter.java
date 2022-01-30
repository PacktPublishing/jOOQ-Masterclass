package com.classicmodels.converter;

import com.classicmodels.enums.StarType;
import jooq.generated.enums.RateType;
import static jooq.generated.enums.RateType.GOLD;
import static jooq.generated.enums.RateType.PLATINUM;
import static jooq.generated.enums.RateType.SILVER;
import org.jooq.Converter;

public class SaleRateStarConverter implements Converter<RateType, StarType> {
    
    public final static SaleRateStarConverter SALE_RATE_STAR_CONVERTER = new SaleRateStarConverter();

    @Override
    public StarType from(RateType t) {

        if (t != null) {

            return switch (t) {
                case SILVER ->
                    StarType.THREE_STARS;
                case GOLD ->
                    StarType.FOUR_STARS;
                case PLATINUM ->
                    StarType.FIVE_STARS;
                default ->
                    throw new IllegalArgumentException("Invalid value");
            };
        }

        return null;
    }

    @Override
    public RateType to(StarType u) {

        if (u != null) {

            return switch (u) {
                case THREE_STARS ->
                    RateType.SILVER;
                case FOUR_STARS ->
                    RateType.GOLD;
                case FIVE_STARS ->
                    RateType.PLATINUM;
                default ->
                    throw new IllegalArgumentException("Invalid value");
            };
        }

        return null;
    }

    @Override
    public Class<RateType> fromType() {
        return RateType.class;
    }

    @Override
    public Class<StarType> toType() {
        return StarType.class;
    }

}
