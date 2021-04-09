package com.classicmodels.repository;

import jooq.generated.tables.Sale;
import org.jooq.Condition;

public interface SalePredicate {
    
    Condition apply(Sale s);
}
