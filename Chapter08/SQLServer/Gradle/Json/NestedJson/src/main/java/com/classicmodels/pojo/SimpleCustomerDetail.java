package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.Objects;

public class SimpleCustomerDetail implements Serializable {

    private static final long serialVersionUID = 1;
    
    private String city;
    private String addressLineFirst;
    private String state;               

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.city);
        hash = 89 * hash + Objects.hashCode(this.addressLineFirst);
        hash = 89 * hash + Objects.hashCode(this.state);
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
        if (!Objects.equals(this.city, other.city)) {
            return false;
        }
        if (!Objects.equals(this.addressLineFirst, other.addressLineFirst)) {
            return false;
        }
        if (!Objects.equals(this.state, other.state)) {
            return false;
        }
        return true;
    }
        
    @Override
    public String toString() {
        return "SimpleCustomerDetail{" + "city=" + city + ", addressLineFirst=" 
                + addressLineFirst + ", state=" + state + '}';
    }            
}
