package com.classicmodels.repository;

import java.util.List;

public interface JooqRepository<T, ID> {  
    
    public List<T> findLimitedTo(int value);
}
