package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.Objects;

public class SimpleCustomer implements Serializable {

    private static final long serialVersionUID = 1;
   
    private String customerName;   

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public int hashCode() {
        
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.customerName);
        
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
        
        return true;
    }

    @Override
    public String toString() {
        return "Customer{" + "customerName=" + customerName + '}';
    }
        
}
