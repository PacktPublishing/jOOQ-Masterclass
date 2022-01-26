package com.classicmodels.enums;

import org.jooq.EnumType;
import org.jooq.Schema;
import static org.jooq.impl.DSL.schema;

public enum RateType implements EnumType {

    SILVER, GOLD, PLATINUM;

    @Override
    public String getLiteral() {
        return this.name();
    }

    @Override
    public Schema getSchema() {
        return schema("public");
    }

    @Override
    public String getName() {
        return "rate_type";
    }
}
