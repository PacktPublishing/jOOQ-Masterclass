package com.classicmodels.converter;


import com.classicmodels.enums.StarType;
import jooq.generated.enums.RateType;
import org.jooq.impl.EnumConverter;

public class SaleRateStarConverter extends EnumConverter<RateType, StarType> {

    public final static SaleRateStarConverter SALE_RATE_STAR_CONVERTER = new SaleRateStarConverter();

    public SaleRateStarConverter() {
        super(RateType.class, StarType.class);
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
            };
        }

        return null;
    }

}
