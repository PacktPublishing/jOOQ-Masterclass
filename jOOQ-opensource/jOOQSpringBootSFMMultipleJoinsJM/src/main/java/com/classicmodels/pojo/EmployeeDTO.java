package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.simpleflatmapper.map.annotation.Key;

public class EmployeeDTO implements Serializable {

    private static final long serialVersionUID = 1;

    @Key
    private Long employeeNumber;
    private String firstName;
    private String lastName;
    private Set<CustomerDTO> customers = new HashSet<>();
    private Set<SaleDTO> sales = new HashSet<>();

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

    public Set<CustomerDTO> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<CustomerDTO> customers) {
        this.customers = customers;
    }

    public Set<SaleDTO> getSales() {
        return sales;
    }

    public void setSales(Set<SaleDTO> sales) {
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
        
        final EmployeeDTO other = (EmployeeDTO) obj;
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
        return "EmployeeDTO{" + "employeeNumber=" + employeeNumber 
                + ", firstName=" + firstName + ", lastName=" + lastName 
                + ", customers=" + customers + ", sales=" + sales + '}';
    }
   
}
