package com.classicmodels.util;

import jooq.generated.tables.Sale;
import org.jooq.Condition;

public class Up2004Sales implements SaleFunction<Sale, Condition> {

    @Override
    public Condition apply(Sale s) {

        return s.FISCAL_YEAR.eq(2004).and(s.TREND.eq("UP"));
    }
}
