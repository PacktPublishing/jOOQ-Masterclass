package com.classicmodels.pojo;

import java.io.Serializable;
import java.time.LocalDate;

public class SimpleOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public LocalDate orderDate;
    public String status;

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
        
    @Override
    public String toString() {
        return "SimpleOrder{" + "orderDate=" + orderDate + ", status=" + status + '}';
    }          
}
