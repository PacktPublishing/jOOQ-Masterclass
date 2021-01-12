package com.classicmodels.embeddable.pojo;

import java.io.Serializable;

public final class ImmutableOfficeFullAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String city;
    private final String addressLineFirst;
    private final String state;
    private final String country;
    private final String territory;

    public ImmutableOfficeFullAddress(String city, String addressLineFirst, String state,
            String country, String territory) {
        this.city = city;
        this.addressLineFirst = addressLineFirst;
        this.state = state;
        this.country = country;
        this.territory = territory;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCity() {
        return city;
    }

    public String getAddressLineFirst() {
        return addressLineFirst;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getTerritory() {
        return territory;
    }

    @Override
    public String toString() {
        return "ImmutableOfficeFullAddress{" + "city=" + city
                + ", addressLineFirst=" + addressLineFirst + ", state=" + state
                + ", country=" + country + ", territory=" + territory + '}';
    }
}
