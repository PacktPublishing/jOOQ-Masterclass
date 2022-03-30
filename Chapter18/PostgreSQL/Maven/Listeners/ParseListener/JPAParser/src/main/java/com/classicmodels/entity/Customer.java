package com.classicmodels.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long customerNumber;

    @Column(length = 50, nullable = false)
    private String customerName;

    @Column(length = 50, nullable = false)
    private String contactLastName;

    @Column(length = 50, nullable = false)
    private String contactFirstName;

    @Column(length = 50, nullable = false)
    private String phone;
    
    @Column(precision = 10, scale = 2)
    private Float creditLimit;
    
    public Long getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Long customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    public String getContactFirstName() {
        return contactFirstName;
    }

    public void setContactFirstName(String contactFirstName) {
        this.contactFirstName = contactFirstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Float getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Float creditLimit) {
        this.creditLimit = creditLimit;
    }
    
    @Override
    public int hashCode() {
        return 2021;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        return customerNumber != null && customerNumber.equals(((Customer) obj).customerNumber);
    }
}
