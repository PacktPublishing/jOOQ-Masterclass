package com.classicmodels.pojo;

import java.io.Serializable;
import java.time.LocalDate;

public class SimpleOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long       orderId;
    private LocalDate  orderDate;
    private LocalDate  requiredDate;
    private LocalDate  shippedDate;

    public SimpleOrder() {}
    
    public SimpleOrder(Long orderId, LocalDate orderDate, LocalDate requiredDate, LocalDate shippedDate) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(LocalDate requiredDate) {
        this.requiredDate = requiredDate;
    }

    public LocalDate getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(LocalDate shippedDate) {
        this.shippedDate = shippedDate;
    }

    @Override
    public String toString() {
        return "SimpleOrder{" + "orderId=" + orderId + ", orderDate=" 
                + orderDate + ", requiredDate=" + requiredDate 
                + ", shippedDate=" + shippedDate + '}';
    }           
}
