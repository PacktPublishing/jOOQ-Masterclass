package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.Objects;

public class SimpleCustomer implements Serializable {

    private static final long serialVersionUID = 1;

    private String customerName;
    private String phone;
    private Float creditLimit;
    private SimpleCustomerDetail details;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Float getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Float creditLimit) {
        this.creditLimit = creditLimit;
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
        hash = 37 * hash + Objects.hashCode(this.customerName);
        hash = 37 * hash + Objects.hashCode(this.phone);
        hash = 37 * hash + Objects.hashCode(this.creditLimit);
        
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
        
        if (!Objects.equals(this.phone, other.phone)) {
            return false;
        }
        
        if (!Objects.equals(this.creditLimit, other.creditLimit)) {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString() {
        return "Customer{" + "customerName=" + customerName 
                + ", phone=" + phone + ", creditLimit=" + creditLimit + ", details=" + details + '}';
    }
        
}