package com.classicmodels.pojo;

import java.io.Serializable;
import java.time.LocalDate;

public class CustomerAndOrder implements Serializable {

    private static final long serialVersionUID = 1;

    private String customerName;
    private LocalDate orderDate;
    
    public CustomerAndOrder() {
    }

    public CustomerAndOrder(String customerName, LocalDate orderDate) {
        this.customerName = customerName;
        this.orderDate = orderDate;        
    }

    public CustomerAndOrder(CustomerAndOrder value) {
        this.customerName = value.customerName;
        this.orderDate = value.orderDate;    
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
    
    @Override
    public String toString() {
        return "CustomerAndOrder{" + "customerName=" + customerName
                + ", orderDate=" + orderDate + '}';
    }

}