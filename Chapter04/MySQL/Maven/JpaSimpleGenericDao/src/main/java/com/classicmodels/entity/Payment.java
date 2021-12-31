package com.classicmodels.entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private PaymentId id;

    @Column(nullable=false)
    private LocalDate paymentDate;

    @Column(precision = 10, scale = 2, nullable=false)
    private Float invoiceAmount;
   
    private LocalDate cachingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_number", insertable=false, updatable=false)
    private Customer customer;

    public PaymentId getId() {
        return id;
    }

    public void setId(PaymentId id) {
        this.id = id;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public int hashCode() {
        return 2021;
    }

    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
            return false;
        }
        
        if (this == obj) {
            return true;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        return id != null && id.equals(((Payment) obj).id);
    }
}
