package com.classicmodels.pojo;

import java.math.BigDecimal;
import java.util.List;

public class SimpleCustomer {
    
    public String customerName;
    public BigDecimal creditLimit;
    public List<SimplePayment> payments;   
    public SimpleCustomerDetail details;

    @Override
    public String toString() {
        return "SimpleCustomer{" + "customerName=" + customerName 
                + ", creditLimit=" + creditLimit + ", payments=" + payments + ", details=" + details + '}';
    }        
}
