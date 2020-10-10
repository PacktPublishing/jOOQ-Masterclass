package com.classicmodels.pojo;

import java.io.Serializable;
import java.time.LocalDate;

public class CustomerCachingDate implements Serializable {

    private static final long serialVersionUID = 1;

    private String customerName;
    private LocalDate cachingDate;
    private LocalDate nextCachingDate;

    public CustomerCachingDate() {
    }

    public CustomerCachingDate(String customerName, LocalDate cachingDate, LocalDate nextCachingDate) {
        this.customerName = customerName;
        this.cachingDate = cachingDate;
        this.nextCachingDate = nextCachingDate;
    }

    public CustomerCachingDate(CustomerCachingDate value) {
        this.customerName = value.customerName;
        this.cachingDate = value.cachingDate;
        this.nextCachingDate = value.nextCachingDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getCachingDate() {
        return cachingDate;
    }

    public void setCachingDate(LocalDate cachingDate) {
        this.cachingDate = cachingDate;
    }

    public LocalDate getNextCachingDate() {
        return nextCachingDate;
    }

    public void setNextCachingDate(LocalDate nextCachingDate) {
        this.nextCachingDate = nextCachingDate;
    }

    @Override
    public String toString() {
        return "CustomerCachingDate{" + "customerName=" + customerName 
                + ", cachingDate=" + cachingDate + ", nextCachingDate=" + nextCachingDate + '}';
    }    

}