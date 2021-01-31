package com.classicmodels.pojo;

import com.classicmodels.embeddable.pojo.ImmutableOfficeFullAddress;

public final class ImmutableOffice {
    
    private final String officeCode;
    private final ImmutableOfficeFullAddress officeFullAddress;

    public ImmutableOffice(String officeCode, ImmutableOfficeFullAddress officeFullAddress) {
        this.officeCode = officeCode;
        this.officeFullAddress = officeFullAddress;
    }        

    public String getOfficeCode() {
        return officeCode;
    }
    
    public ImmutableOfficeFullAddress getOfficeFullAddress() {
        return officeFullAddress;
    }

    @Override
    public String toString() {
        return "ImmutableOffice{" + "officeCode=" + officeCode 
                + ", officeFullAddress=" + officeFullAddress + '}';
    }
}
