package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SimpleEmployee implements Serializable {

    private static final long serialVersionUID = 1;
    
    private String employeeFirstName;
    private String employeeLastName;
    private Integer employeeSalary;
    private List<SimpleSale> sales; 

    public SimpleEmployee(String employeeFirstName, String employeeLastName, Integer employeeSalary, List<SimpleSale> sales) {
        this.employeeFirstName = employeeFirstName;
        this.employeeLastName = employeeLastName;
        this.employeeSalary = employeeSalary;
        this.sales = sales;
    }
        
    public String getEmployeeFirstName() {
        return employeeFirstName;
    }

    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName = employeeFirstName;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }

    public Integer getEmployeeSalary() {
        return employeeSalary;
    }

    public void setEmployeeSalary(Integer employeeSalary) {
        this.employeeSalary = employeeSalary;
    }

    public List<SimpleSale> getSales() {
        return sales;
    }

    public void setSales(List<SimpleSale> sales) {
        this.sales = sales;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.employeeFirstName);
        hash = 41 * hash + Objects.hashCode(this.employeeLastName);
        hash = 41 * hash + Objects.hashCode(this.employeeSalary);
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
        if (!Objects.equals(this.employeeFirstName, other.employeeFirstName)) {
            return false;
        }
        if (!Objects.equals(this.employeeLastName, other.employeeLastName)) {
            return false;
        }
        if (!Objects.equals(this.employeeSalary, other.employeeSalary)) {
            return false;
        }
        return true;
    }
        
    @Override
    public String toString() {
        return "SimpleEmployee{" + "employeeFirstName=" + employeeFirstName 
                + ", employeeLastName=" + employeeLastName + ", employeeSalary=" 
                + employeeSalary + ", sales=" + sales + '}';
    }        
}
