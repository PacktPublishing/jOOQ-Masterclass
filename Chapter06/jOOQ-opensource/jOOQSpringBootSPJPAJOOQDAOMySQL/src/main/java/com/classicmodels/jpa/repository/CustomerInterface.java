package com.classicmodels.jpa.repository;

import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author my
 
 */
public interface CustomerInterface {
    
    public List<jooq.generated.tables.pojos.Customer> fetchRangeOfCustomerNumber(Long lowerInclusive, Long upperInclusive);
    public void insert(jooq.generated.tables.pojos.Customer object);
}
