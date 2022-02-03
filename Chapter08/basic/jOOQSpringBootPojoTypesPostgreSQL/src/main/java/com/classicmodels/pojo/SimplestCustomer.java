package com.classicmodels.pojo;

import java.io.Serializable;

public class SimplestCustomer implements Serializable {

    private static final long serialVersionUID = 1L;
    
    String customerName;
    String customerPhone;

    @Override
    public String toString() {
        return "SimplestCustomer{" + "customerName=" + customerName 
                + ", customerPhone=" + customerPhone + '}';
    }        
}
