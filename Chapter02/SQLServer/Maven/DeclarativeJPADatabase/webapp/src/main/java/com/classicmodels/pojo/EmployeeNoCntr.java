package com.classicmodels.pojo;

import java.io.Serializable;

public class EmployeeNoCntr implements Serializable {

    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private Integer salary;
    private String leastSalary;
         
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }
            
    public String getLeastSalary() {
        return leastSalary;
    }

    public void setLeastSalary(String leastSalary) {
        this.leastSalary = leastSalary;
    }

    @Override
    public String toString() {
        return "EmployeeDto{" + "firstName=" + firstName 
                + ", lastName=" + lastName 
                + ", salary=" + salary 
                + ", leastSalary=" + leastSalary + '}';
    }       
}