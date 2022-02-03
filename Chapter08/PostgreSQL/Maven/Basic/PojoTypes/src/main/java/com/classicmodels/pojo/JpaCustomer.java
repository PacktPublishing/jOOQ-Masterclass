package com.classicmodels.pojo;

import jakarta.persistence.Column;
import java.io.Serializable;
import java.time.YearMonth;

public class JpaCustomer implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Column(name = "customer_name") 
    public String cn;
    
    @Column(name = "first_buy_date") 
    public YearMonth ym;      

    @Override
    public String toString() {
        return "JpaCustomer{" + "cn=" + cn + ", ym=" + ym + '}';
    }        
}
