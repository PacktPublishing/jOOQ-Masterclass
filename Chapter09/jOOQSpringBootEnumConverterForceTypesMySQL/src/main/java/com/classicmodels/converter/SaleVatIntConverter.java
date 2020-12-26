package com.classicmodels.converter;

import org.jooq.Converter;

public class SaleVatIntConverter implements Converter<String, Integer> {

    public final static SaleVatIntConverter SALE_VAT_INT_CONVERTER = new SaleVatIntConverter();

    @Override
    public Integer from(String t) {

        if (t != null) {

            return switch (t) {
                case "NONE" ->
                    0;
                case "MIN" ->
                    5;
                case "MAX" ->
                    19;
                default ->
                    throw new IllegalArgumentException("Invalid value");
            };
        }

        return null;
    }

    @Override
    public String to(Integer u) {

        if (u != null) {

            return switch (u) {
                case 0 ->
                    "NONE";
                case 5 ->
                    "MIN";
                case 19 ->
                    "MAX";
                default ->
                    throw new IllegalArgumentException("Invalid number");
            };
        }

        return null;
    }

    @Override
    public Class<String> fromType() {
        return String.class;
    }

    @Override
    public Class<Integer> toType() {
        return Integer.class;
    }

}
