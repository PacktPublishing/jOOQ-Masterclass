package com.classicmodels.pojo;

import java.io.Serializable;

public class OfficePart implements Serializable {

    private static final long serialVersionUID = 1L;

    private String city;
    private String country;

    public OfficePart() {
    }

    public OfficePart(OfficePart value) {
        this.city = value.city;
        this.country = value.country;
    }

    public OfficePart(String city, String country) {
        this.city = city;
        this.country = country;
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

}
