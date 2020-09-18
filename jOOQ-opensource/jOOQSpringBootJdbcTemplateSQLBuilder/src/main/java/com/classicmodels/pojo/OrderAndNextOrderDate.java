package com.classicmodels.pojo;

import java.io.Serializable;
import java.time.LocalDate;

public class OrderAndNextOrderDate implements Serializable {

    private static final long serialVersionUID = 1;

    private String customerName;
    private LocalDate orderDate;
    private LocalDate nextOrderDate;

    public OrderAndNextOrderDate() {
    }

    public OrderAndNextOrderDate(String customerName, LocalDate orderDate, LocalDate nextOrderDate) {
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.nextOrderDate = nextOrderDate;
    }

    public OrderAndNextOrderDate(OrderAndNextOrderDate value) {
        this.customerName = value.customerName;
        this.orderDate = value.orderDate;
        this.nextOrderDate = value.nextOrderDate;
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

    public LocalDate getNextOrderDate() {
        return nextOrderDate;
    }

    public void setNextOrderDate(LocalDate nextOrderDate) {
        this.nextOrderDate = nextOrderDate;
    }

    @Override
    public String toString() {
        return "OrderAndNextOrderDate{" + "customerName=" + customerName
                + ", orderDate=" + orderDate + ", nextOrderDate=" + nextOrderDate + '}';
    }

}
