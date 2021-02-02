package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.Objects;

public class SimpleUCustomer implements Serializable {

    private static final long serialVersionUID = 1;

    private String customerName;
    private String phone;
    private Float creditLimit;
    
    private SimpleUCustomerDetail detail;

    public SimpleUCustomer(String customerName, String phone, Float creditLimit) {
        this.customerName = customerName;
        this.phone = phone;
        this.creditLimit = creditLimit;
    }   
    
    public SimpleUCustomer(String customerName, String phone, 
            Float creditLimit, SimpleUCustomerDetail detail) {
        this.customerName = customerName;
        this.phone = phone;
        this.creditLimit = creditLimit;
        this.detail = detail;
    }        

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

    public SimpleUCustomerDetail getDetail() {
        return detail;
    }

    public void setDetail(SimpleUCustomerDetail detail) {
        this.detail = detail;
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
        
        final SimpleUCustomer other = (SimpleUCustomer) obj;
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
        return "SimpleUCustomer{" + "customerName=" + customerName 
                + ", phone=" + phone + ", creditLimit=" + creditLimit + '}';
    }   
        
}