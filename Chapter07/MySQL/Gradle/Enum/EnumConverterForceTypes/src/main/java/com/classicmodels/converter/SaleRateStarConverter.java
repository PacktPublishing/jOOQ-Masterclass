package com.classicmodels.converter;

import com.classicmodels.enums.StarType;
import org.jooq.Converter;

public class SaleRateStarConverter implements Converter<String, StarType> {
    
    public final static SaleRateStarConverter SALE_RATE_STAR_CONVERTER = new SaleRateStarConverter();

    @Override
    public StarType from(String t) {

        if (t != null) {

            return switch (t) {
                case "SILVER" ->
                    StarType.THREE_STARS;
                case "GOLD" ->
                    StarType.FOUR_STARS;
                case "PLATINUM" ->
                    StarType.FIVE_STARS;
                default ->
                    throw new IllegalArgumentException("Invalid value");
            };
        }

        return null;
    }

    @Override
    public String to(StarType u) {

        if (u != null) {

            return switch (u) {
                case THREE_STARS ->
                    "SILVER";
                case FOUR_STARS ->
                    "GOLD";
                case FIVE_STARS ->
                    "PLATINUM";
                default ->
                    throw new IllegalArgumentException("Invalid value");
            };
        }

        return null;
    }

    @Override
    public Class<String> fromType() {
        return String.class;
    }

    @Override
    public Class<StarType> toType() {
        return StarType.class;
    }

}
