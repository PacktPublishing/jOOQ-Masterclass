package com.classicmodels.pojo;

import java.io.Serializable;

public class CustomerPojo implements Serializable {

    private static final long serialVersionUID = 1;

    private String city;
    private String country;   
    private String fullName;        

    private CustomerPojo() {
    }

    public CustomerPojo(String city, String country, String fullName) {
        this.city = city;
        this.country = country;
        this.fullName = fullName;
    }

    public CustomerPojo(CustomerPojo value) {
        this.city = value.city;
        this.country = value.country;
        this.fullName = value.fullName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "CustomerPojo{" + "city=" + city 
                + ", country=" + country + ", fullName=" + fullName + '}';
    }
}