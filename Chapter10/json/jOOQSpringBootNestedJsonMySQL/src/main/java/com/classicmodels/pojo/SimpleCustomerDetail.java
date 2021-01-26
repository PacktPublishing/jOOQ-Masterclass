package com.classicmodels.pojo;

public class SimpleCustomerDetail {
    
    public String city;
    public String addressLineFirst;
    public String state;

    @Override
    public String toString() {
        return "SimpleCustomerDetail{" + "city=" + city + ", addressLineFirst=" 
                + addressLineFirst + ", state=" + state + '}';
    }            
}
