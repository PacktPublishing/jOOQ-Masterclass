package com.classicmodels.pojos;

import java.io.Serializable;
import java.time.YearMonth;

public class ImmutableCustomer implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private final String customerName;
    private final YearMonth ym;

    public ImmutableCustomer(String customerName, YearMonth ym) {
        this.customerName = customerName;
        this.ym = ym;
    }        

    @Override
    public String toString() {
        return "ImmutableCustomer{" + "customerName=" + customerName + ", ym=" + ym + '}';
    }        
}
