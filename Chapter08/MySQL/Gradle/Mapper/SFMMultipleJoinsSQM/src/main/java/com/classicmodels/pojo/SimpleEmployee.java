package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class SimpleEmployee implements Serializable {

    private static final long serialVersionUID = 1;

    private Long employeeNumber;
    private String firstName;
    private String lastName;
    
    private Set<SimpleCustomer> customers;
    private Set<SimpleSale> sales;

    public Long getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(Long employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<SimpleCustomer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<SimpleCustomer> customers) {
        this.customers = customers;
    }

    public Set<SimpleSale> getSales() {
        return sales;
    }

    public void setSales(Set<SimpleSale> sales) {
        this.sales = sales;
    }

    @Override
    public int hashCode() {
        
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.employeeNumber);
        hash = 29 * hash + Objects.hashCode(this.firstName);
        hash = 29 * hash + Objects.hashCode(this.lastName);
        
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

        final SimpleEmployee other = (SimpleEmployee) obj;
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }

        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }

        if (!Objects.equals(this.employeeNumber, other.employeeNumber)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Employee{" + "employeeNumber=" + employeeNumber
                + ", firstName=" + firstName + ", lastName=" + lastName
                + ", customers=" + customers + ", sales=" + sales + '}';
    }
}
