package com.classicmodels.pojo;

import java.io.Serializable;

public class Office implements Serializable {

    private static final long serialVersionUID = 1;

    private String officeCode;
    private String addressLineFirst;
    private String addressLineSecond;
    private String city;
    private String country;
    private String phone;
    private String postalCode;
    private String state;
    private String territory;

    private Office() {
    }

    public Office(String officeCode, String addressLineFirst, String addressLineSecond,
            String city, String country, String phone, String postalCode, String state, String territory) {
        this.officeCode = officeCode;
        this.addressLineFirst = addressLineFirst;
        this.addressLineSecond = addressLineSecond;
        this.city = city;
        this.country = country;
        this.phone = phone;
        this.postalCode = postalCode;
        this.state = state;
        this.territory = territory;
    }

    public Office(Office value) {
        this.officeCode = value.officeCode;
        this.addressLineFirst = value.addressLineFirst;
        this.addressLineSecond = value.addressLineSecond;
        this.city = value.city;
        this.country = value.country;
        this.phone = value.phone;
        this.postalCode = value.postalCode;
        this.state = value.state;
        this.territory = value.territory;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getAddressLineFirst() {
        return addressLineFirst;
    }

    public void setAddressLineFirst(String addressLineFirst) {
        this.addressLineFirst = addressLineFirst;
    }

    public String getAddressLineSecond() {
        return addressLineSecond;
    }

    public void setAddressLineSecond(String addressLineSecond) {
        this.addressLineSecond = addressLineSecond;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }

    @Override
    public String toString() {
        return "Office{" + "officeCode=" + officeCode + ", addressLineFirst=" + addressLineFirst 
                + ", addressLineSecond=" + addressLineSecond + ", city=" + city 
                + ", country=" + country + ", phone=" + phone + ", postalCode=" + postalCode 
                + ", state=" + state + ", territory=" + territory + '}';
    }   
}