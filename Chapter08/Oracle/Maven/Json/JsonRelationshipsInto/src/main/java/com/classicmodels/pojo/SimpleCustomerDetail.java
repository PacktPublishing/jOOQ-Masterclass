package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.Objects;

public class SimpleCustomerDetail implements Serializable {

    private static final long serialVersionUID = 1;
        
    private String addressLineFirst;
    private String state;
    private String city;           
    
    public SimpleCustomerDetail() {}
    
    public SimpleCustomerDetail(String addressLineFirst, String state, String city) {
        this.addressLineFirst = addressLineFirst;
        this.state = state;
        this.city = city;
    }         

    public SimpleCustomerDetail(String city, String addressLineFirst) {
        this.addressLineFirst = addressLineFirst;
        this.city = city;
    }
            
    public String getAddressLineFirst() {
        return addressLineFirst;
    }

    public void setAddressLineFirst(String addressLineFirst) {
        this.addressLineFirst = addressLineFirst;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int hashCode() {
        
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.addressLineFirst);
        hash = 41 * hash + Objects.hashCode(this.state);
        hash = 41 * hash + Objects.hashCode(this.city);
        
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
        
        final SimpleCustomerDetail other = (SimpleCustomerDetail) obj;
        if (!Objects.equals(this.addressLineFirst, other.addressLineFirst)) {
            return false;
        }
        
        if (!Objects.equals(this.state, other.state)) {
            return false;
        }
        
        if (!Objects.equals(this.city, other.city)) {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString() {
        return "CustomerDetail{" + "addressLineFirst=" + addressLineFirst 
                + ", state=" + state + ", city=" + city + '}';
    }
           
}