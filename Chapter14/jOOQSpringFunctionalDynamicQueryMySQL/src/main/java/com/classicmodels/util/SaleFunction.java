package com.classicmodels.util;

@FunctionalInterface
public interface SaleFunction<Sale, Condition> {
    
    Condition apply(Sale s);
}
