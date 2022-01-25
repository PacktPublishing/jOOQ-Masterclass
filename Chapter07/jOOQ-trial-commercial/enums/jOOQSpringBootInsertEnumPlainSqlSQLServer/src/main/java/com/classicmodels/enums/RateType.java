package com.classicmodels.enums;

import org.jooq.EnumType;

public enum RateType implements EnumType {

    SILVER, GOLD, PLATINUM;

    @Override
    public String getLiteral() {
        return this.name();
    }    

    @Override
    public String getName() {
        return "rate_type";
    }
}
