package com.classicmodels.enums;

import org.jooq.EnumType;
import org.jooq.Schema;

public enum RateType implements EnumType {

    SILVER, GOLD, PLATINUM;

    @Override
    public String getLiteral() {
        return this.name();
    }

    @Override
    public String getName() {
        return "rate";
    }
}
