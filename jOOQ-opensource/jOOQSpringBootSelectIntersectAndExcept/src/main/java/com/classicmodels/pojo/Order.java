package com.classicmodels.pojo;

import java.io.Serializable;
import java.time.LocalDate;

public class Order implements Serializable {

    private static final long serialVersionUID = 1;

    private Long orderId;
    private LocalDate orderDate;   
    private LocalDate shippedDate;    
    private LocalDate requiredDate;    
    private Long customerNumber;

    private Order() {
    }

    public Order(Long orderId, LocalDate orderDate,
            LocalDate shippedDate, LocalDate requiredDate, Long customerNumber) {
        this.orderId = orderId;
        this.orderDate = orderDate;       
        this.shippedDate = shippedDate;        
        this.requiredDate = requiredDate;
        this.customerNumber = customerNumber;
    }

    public Order(Order value) {
        this.orderId = value.orderId;
        this.orderDate = value.orderDate;        
        this.shippedDate = value.shippedDate;        
        this.requiredDate = value.requiredDate;
        this.customerNumber = value.customerNumber;
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
   
    public LocalDate getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(LocalDate shippedDate) {
        this.shippedDate = shippedDate;
    }   

    public LocalDate getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(LocalDate requiredDate) {
        this.requiredDate = requiredDate;
    }   
    
    public Long getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Long customerNumber) {
        this.customerNumber = customerNumber;
    }

    @Override
    public String toString() {
        return "Order{" + "orderId=" + orderId 
                + ", orderDate=" + orderDate + ", shippedDate=" + shippedDate 
                + ", requiredDate=" + requiredDate + ", customerNumber=" + customerNumber + '}';
    }       
}