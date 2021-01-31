package com.classicmodels.pojo;

import java.io.Serializable;
import jooq.generated.embeddables.pojos.OfficeFullAddress;

public class SimpleOffice implements Serializable {

    private static final long serialVersionUID = 1L;

    private String officeCode;
    private OfficeFullAddress officeFullAddress;

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public OfficeFullAddress getOfficeFullAddress() {
        return officeFullAddress;
    }

    public void setOfficeFullAddress(OfficeFullAddress officeFullAddress) {
        this.officeFullAddress = officeFullAddress;
    }

    @Override
    public String toString() {
        return "SimpleOffice{" + "officeCode=" + officeCode + ", officeFullAddress=" + officeFullAddress + '}';
    }
}
