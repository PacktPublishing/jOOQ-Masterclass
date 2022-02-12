package com.classicmodels.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

public class SimpleCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long customerNumber;
    private String customerName;
    private String contactLastName;
    private String contactFirstName;
    private String phone;
    private Long salesRepEmployeeNumber;
    private BigDecimal creditLimit;
    private Integer firstBuyDate;
    
    public SimpleCustomer() {};

    public SimpleCustomer(Long customerNumber, String customerName, String contactLastName, 
            String contactFirstName, String phone, Long salesRepEmployeeNumber, BigDecimal creditLimit, 
            Integer firstBuyDate) {
        this.customerNumber = customerNumber;
        this.customerName = customerName;
        this.contactLastName = contactLastName;
        this.contactFirstName = contactFirstName;
        this.phone = phone;
        this.salesRepEmployeeNumber = salesRepEmployeeNumber;
        this.creditLimit = creditLimit;
        this.firstBuyDate = firstBuyDate;
    }

    public Long getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Long customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    public String getContactFirstName() {
        return contactFirstName;
    }

    public void setContactFirstName(String contactFirstName) {
        this.contactFirstName = contactFirstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getSalesRepEmployeeNumber() {
        return salesRepEmployeeNumber;
    }

    public void setSalesRepEmployeeNumber(Long salesRepEmployeeNumber) {
        this.salesRepEmployeeNumber = salesRepEmployeeNumber;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Integer getFirstBuyDate() {
        return firstBuyDate;
    }

    public void setFirstBuyDate(Integer firstBuyDate) {
        this.firstBuyDate = firstBuyDate;
    }
        
    @Override
    public String toString() {
        return "SimpleCustomer{" + "customerNumber=" + customerNumber + ", customerName=" + customerName 
                + ", contactLastName=" + contactLastName + ", contactFirstName=" + contactFirstName 
                + ", phone=" + phone + ", salesRepEmployeeNumber=" + salesRepEmployeeNumber 
                + ", creditLimit=" + creditLimit + ", firstBuyDate=" + firstBuyDate + '}';
    }   
}
