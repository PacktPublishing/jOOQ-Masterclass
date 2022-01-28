package com.classicmodels.converter;

import jooq.generated.enums.VatType;
import org.jooq.Converter;

public class SaleVatIntConverter implements Converter<VatType, Integer> {

    public final static SaleVatIntConverter SALE_VAT_INT_CONVERTER = new SaleVatIntConverter();

    @Override
    public Integer from(VatType t) {

        if (t != null) {

            return switch (t) {
                case NONE ->
                    0;
                case MIN ->
                    5;
                case MAX ->
                    19;
            };
        }

        return null;
    }

    @Override
    public VatType to(Integer u) {

        if (u != null) {

            return switch (u) {
                case 0 ->
                    VatType.NONE;
                case 5 ->
                    VatType.MIN;
                case 19 ->
                    VatType.MAX;
                default ->
                    throw new IllegalArgumentException("Invalid number");
            };
        }

        return null;
    }

    @Override
    public Class<VatType> fromType() {
        return VatType.class;
    }

    @Override
    public Class<Integer> toType() {
        return Integer.class;
    }

}
