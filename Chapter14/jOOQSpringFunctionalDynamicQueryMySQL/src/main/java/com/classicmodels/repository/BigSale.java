package com.classicmodels.repository;

import jooq.generated.tables.Sale;
import org.jooq.Condition;

public class BigSale implements SalePredicate {

    @Override
    public Condition apply(Sale s) {
        return s.SALE_.gt(4000d);
    }
}
