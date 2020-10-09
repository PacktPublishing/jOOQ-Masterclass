package com.classicmodels.pojo;

import java.io.Serializable;
import java.time.LocalDate;

public class DelayedPayment implements Serializable {

    private static final long serialVersionUID = 1;

    private String customerName;
    private LocalDate paymentDate;
    private Float invoiceAmount;
    private LocalDate cachingDate;

    public DelayedPayment() {
    }

    public DelayedPayment(String customerName, LocalDate paymentDate,
            Float invoiceAmount, LocalDate cachingDate) {
        this.customerName = customerName;
        this.paymentDate = paymentDate;
        this.invoiceAmount = invoiceAmount;
        this.cachingDate = cachingDate;
    }

    public DelayedPayment(DelayedPayment value) {
        this.customerName = value.customerName;
        this.paymentDate = value.paymentDate;
        this.cachingDate = value.cachingDate;
        this.invoiceAmount = value.invoiceAmount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Float getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(Float invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public LocalDate getCachingDate() {
        return cachingDate;
    }

    public void setCachingDate(LocalDate cachingDate) {
        this.cachingDate = cachingDate;
    }

    @Override
    public String toString() {
        return "DelayedPayment{" + "customerName=" + customerName 
                + ", paymentDate=" + paymentDate + ", invoiceAmount=" 
                + invoiceAmount + ", cachingDate=" + cachingDate + '}';
    }
    
}
