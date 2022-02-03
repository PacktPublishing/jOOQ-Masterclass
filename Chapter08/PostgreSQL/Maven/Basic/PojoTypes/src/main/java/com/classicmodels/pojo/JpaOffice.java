package com.classicmodels.pojo;

import jakarta.persistence.Column;
import java.io.Serializable;
import jooq.generated.embeddables.pojos.OfficeFullAddress;

public class JpaOffice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "office_code")
    public String oc;

    @Column(name = "office_full_address")
    public OfficeFullAddress ofa; // using the non-JPA generated POJO

    @Override
    public String toString() {
        return "JpaOffice{" + "officeCode=" + oc + ", officeFullAddress=" + ofa + '}';
    }
}
