package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.Objects;

public class SimpleBCustomerDetail implements Serializable {

    private static final long serialVersionUID = 1;
    
    private String addressLineFirst;
    private String state;
    private String city;
    
    private SimpleBCustomer customer;

    public SimpleBCustomerDetail(String addressLineFirst, String state, String city) {
        this.addressLineFirst = addressLineFirst;
        this.state = state;
        this.city = city;
    }        

    public SimpleBCustomerDetail(String addressLineFirst, String state, 
            String city, SimpleBCustomer customer) {
        this.addressLineFirst = addressLineFirst;
        this.state = state;
        this.city = city;
        this.customer = customer;
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

    public SimpleBCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(SimpleBCustomer customer) {
        this.customer = customer;
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
        
        final SimpleBCustomerDetail other = (SimpleBCustomerDetail) obj;
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
        return "SimpleBCustomerDetail{" + "addressLineFirst=" + addressLineFirst 
                + ", state=" + state + ", city=" + city + '}';
    }               
}