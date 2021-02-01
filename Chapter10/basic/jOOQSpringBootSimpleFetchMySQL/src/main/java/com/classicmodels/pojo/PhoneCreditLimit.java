package com.classicmodels.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

public class PhoneCreditLimit implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public String phone;
    public BigDecimal creditLimit;

    @Override
    public String toString() {
        return "PhoneCreditLimit{" + "customerPhone=" + phone 
                + ", creditLimit=" + creditLimit + '}';
    }            
}