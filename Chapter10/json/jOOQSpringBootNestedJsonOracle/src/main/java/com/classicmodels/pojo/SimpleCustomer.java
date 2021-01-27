package com.classicmodels.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class SimpleCustomer implements Serializable {

    private static final long serialVersionUID = 1;
    
    private String customerName;
    private BigDecimal creditLimit;
    private List<SimplePayment> payments;   
    private SimpleCustomerDetail details;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public List<SimplePayment> getPayments() {
        return payments;
    }

    public void setPayments(List<SimplePayment> payments) {
        this.payments = payments;
    }

    public SimpleCustomerDetail getDetails() {
        return details;
    }

    public void setDetails(SimpleCustomerDetail details) {
        this.details = details;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.customerName);
        hash = 53 * hash + Objects.hashCode(this.creditLimit);
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
        final SimpleCustomer other = (SimpleCustomer) obj;
        if (!Objects.equals(this.customerName, other.customerName)) {
            return false;
        }
        if (!Objects.equals(this.creditLimit, other.creditLimit)) {
            return false;
        }
        return true;
    }
        
    @Override
    public String toString() {
        return "SimpleCustomer{" + "customerName=" + customerName 
                + ", creditLimit=" + creditLimit + ", payments=" + payments + ", details=" + details + '}';
    }        
}
