package com.classicmodels.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PaymentId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name="customer_number")
    private Long customerNumber;

    @Column(length = 50)
    private String checkNumber;
    
    public PaymentId() {}

    public PaymentId(Long customerNumber, String checkNumber) {
        this.customerNumber = customerNumber;
        this.checkNumber = checkNumber;
    }        

    public Long getCustomerNumber() {
        return customerNumber;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.customerNumber);
        hash = 97 * hash + Objects.hashCode(this.checkNumber);
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
        final PaymentId other = (PaymentId) obj;
        if (!Objects.equals(this.checkNumber, other.checkNumber)) {
            return false;
        }
        if (!Objects.equals(this.customerNumber, other.customerNumber)) {
            return false;
        }
        return true;
    }
        
}
