package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import org.simpleflatmapper.map.annotation.Key;

public class SimpleCustomer implements Serializable {

    private static final long serialVersionUID = 1;
       
    @Key
    private Long customerNumber;
    private String customerName;  
    
    private Set<SimpleOrder> orders;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Long customerNumber) {
        this.customerNumber = customerNumber;
    }        

    public Set<SimpleOrder> getOrders() {
        return orders;
    }

    public void setOrders(Set<SimpleOrder> orders) {
        this.orders = orders;
    }        

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.customerNumber);
        hash = 17 * hash + Objects.hashCode(this.customerName);
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
        if (!Objects.equals(this.customerNumber, other.customerNumber)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Customer{" + "customerNumber=" + customerNumber + ", customerName=" + customerName + ", orders=" + orders + '}';
    }

    
        
}
