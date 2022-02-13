package com.classicmodels.pojo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class SimplePayment implements Serializable {

    private static final long serialVersionUID = 1;

    private Long customerNumber;
    private BigDecimal invoiceAmount;
       
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime cachingDate;
    
    private List<SimpleBank> transactions;

    public Long getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Long customerNumber) {
        this.customerNumber = customerNumber;
    }  

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public LocalDateTime getCachingDate() {
        return cachingDate;
    }

    public void setCachingDate(LocalDateTime cachingDate) {
        this.cachingDate = cachingDate;
    }

    public List<SimpleBank> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<SimpleBank> transactions) {
        this.transactions = transactions;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.customerNumber);
        hash = 83 * hash + Objects.hashCode(this.invoiceAmount);
        hash = 83 * hash + Objects.hashCode(this.cachingDate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimplePayment other = (SimplePayment) obj;
        if (!Objects.equals(this.customerNumber, other.customerNumber)) {
            return false;
        }
        if (!Objects.equals(this.invoiceAmount, other.invoiceAmount)) {
            return false;
        }
        if (!Objects.equals(this.cachingDate, other.cachingDate)) {
            return false;
        }
        return true;
    }
        
    @Override
    public String toString() {
        return "SimplePayment{" + "customerNumber=" + customerNumber
                + ", invoiceAmount=" + invoiceAmount + ", cachingDate=" + cachingDate
                + ", transactions=" + transactions + '}';
    }
}
