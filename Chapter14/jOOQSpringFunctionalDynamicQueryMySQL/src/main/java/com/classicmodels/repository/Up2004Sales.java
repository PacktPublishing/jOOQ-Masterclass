package com.classicmodels.repository;

import jooq.generated.tables.Sale;
import org.jooq.Condition;

public class Up2004Sales implements SalePredicate {

    @Override
    public Condition apply(Sale s) {

        return s.FISCAL_YEAR.eq(2004).and(s.TREND.eq("UP"));
    }

}
