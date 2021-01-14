package com.classicmodels.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimpleBOffice implements Serializable {

    private static final long serialVersionUID = 1;
   
    private String officeCode;
    
    private String state;
    private String city;

    @JsonBackReference
    private List<SimpleBManager> managers = new ArrayList<>();

    public SimpleBOffice(String officeCode, String state, String city) {
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

    public List<SimpleBManager> getManagers() {
        return managers;
    }

    public void setManagers(List<SimpleBManager> managers) {
        this.managers = managers;
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
        final SimpleBOffice other = (SimpleBOffice) obj;
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
        return "SimpleBOffice{" + "officeCode=" + officeCode 
                + ", state=" + state + ", city=" + city + '}';
    }   
}
