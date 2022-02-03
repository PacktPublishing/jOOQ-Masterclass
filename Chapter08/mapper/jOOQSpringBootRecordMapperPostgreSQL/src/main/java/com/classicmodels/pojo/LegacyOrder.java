package com.classicmodels.pojo;

import java.io.Serializable;

public class LegacyOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long orderId;
    private int orderDay;
    private int orderMonth;
    private int orderYear;
    private String status;
    
    public LegacyOrder() {}

    public LegacyOrder(Long orderId, int orderDay, int orderMonth, int orderYear, String status) {
        this.orderId = orderId;
        this.orderDay = orderDay;
        this.orderMonth = orderMonth;
        this.orderYear = orderYear;
        this.status = status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public int getOrderDay() {
        return orderDay;
    }

    public void setOrderDay(int orderDay) {
        this.orderDay = orderDay;
    }

    public int getOrderMonth() {
        return orderMonth;
    }

    public void setOrderMonth(int orderMonth) {
        this.orderMonth = orderMonth;
    }

    public int getOrderYear() {
        return orderYear;
    }

    public void setOrderYear(int orderYear) {
        this.orderYear = orderYear;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LegacyOrder{" + "orderId=" + orderId + ", orderDay=" + orderDay 
                + ", orderMonth=" + orderMonth + ", orderYear=" + orderYear + ", status=" + status + '}';
    }        
}