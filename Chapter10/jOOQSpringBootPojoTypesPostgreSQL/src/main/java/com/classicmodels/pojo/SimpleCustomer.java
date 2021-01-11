package com.classicmodels.pojo;

import java.io.Serializable;
import java.time.YearMonth;

public class SimpleCustomer implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public String customerName;
    public YearMonth ym;

    @Override
    public String toString() {
        return "SimpleCustomer{" + "customerName=" + customerName + ", ym=" + ym + '}';
    }        
}
