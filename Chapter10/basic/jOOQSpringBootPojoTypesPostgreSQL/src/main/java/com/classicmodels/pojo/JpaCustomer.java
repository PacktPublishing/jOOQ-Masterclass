package com.classicmodels.pojo;

import java.io.Serializable;
import java.time.YearMonth;
import javax.persistence.Column;

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
