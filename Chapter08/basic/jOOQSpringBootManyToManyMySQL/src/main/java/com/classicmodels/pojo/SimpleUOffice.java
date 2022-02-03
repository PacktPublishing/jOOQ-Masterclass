package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.Objects;

public class SimpleUOffice implements Serializable {

    private static final long serialVersionUID = 1;
   
    private String officeCode;
    
    private String state;
    private String city;
  
    public SimpleUOffice(String officeCode, String state, String city) {
        this.officeCode = officeCode;
        this.state = state;
        this.city = city;
    }        

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
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
        hash = 73 * hash + Objects.hashCode(this.officeCode);
        hash = 73 * hash + Objects.hashCode(this.state);
        hash = 73 * hash + Objects.hashCode(this.city);
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
        final SimpleUOffice other = (SimpleUOffice) obj;
        if (!Objects.equals(this.officeCode, other.officeCode)) {
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
        return "SimpleUOffice{" + "officeCode=" + officeCode 
                + ", state=" + state + ", city=" + city + '}';
    }   
}