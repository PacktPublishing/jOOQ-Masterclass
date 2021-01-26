package com.classicmodels.pojo;

import java.math.BigDecimal;

public class SimpleBank {
    
    public String bankName;
    public BigDecimal transferAmount;

    @Override
    public String toString() {
        return "SimpleBank{" + "bankName=" + bankName + ", transferAmount=" + transferAmount + '}';
    }   
}

