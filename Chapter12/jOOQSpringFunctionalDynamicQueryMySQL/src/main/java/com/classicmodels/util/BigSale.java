package com.classicmodels.util;

import jooq.generated.tables.Sale;
import org.jooq.Condition;

public class BigSale implements SaleFunction<Sale, Condition> {

    @Override
    public Condition apply(Sale s) {
        return s.SALE_.gt(6000d);
    }
}
