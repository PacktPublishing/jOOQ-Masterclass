package com.classicmodels.pojo;

import java.io.Serializable;
import java.time.LocalDate;

public class Order implements Serializable {

    private static final long serialVersionUID = 1;

    private Long orderId;
    private LocalDate orderDate;
    private LocalDate requiredDate;
    private LocalDate shippedDate;
    private String comments;
    private Long customerNumber;

    private Order() {
    }

    public Order(Long orderId, LocalDate orderDate, LocalDate requiredDate,
            LocalDate shippedDate, String comments, Long customerNumber) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.comments = comments;
        this.customerNumber = customerNumber;
    }

    public Order(Order value) {
        this.orderId = value.orderId;
        this.orderDate = value.orderDate;
        this.requiredDate = value.requiredDate;
        this.shippedDate = value.shippedDate;
        this.comments = value.comments;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Long customerNumber) {
        this.customerNumber = customerNumber;
    }

    @Override
    public String toString() {
        return "Order{" + "orderId=" + orderId + ", orderDate=" + orderDate
                + ", requiredDate=" + requiredDate + ", shippedDate=" + shippedDate
                + ", comments=" + comments + ", customerNumber=" + customerNumber + '}';
    }
}
