package com.classicmodels.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

public class PhoneCreditLimit implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public String phone;
    public BigDecimal creditLimit;
    
    public PhoneCreditLimit() {};

    public PhoneCreditLimit(String phone, BigDecimal creditLimit) {
        this.phone = phone;
        this.creditLimit = creditLimit;
    }        

    @Override
    public String toString() {
        return "PhoneCreditLimit{" + "customerPhone=" + phone 
                + ", creditLimit=" + creditLimit + '}';
    }            
}