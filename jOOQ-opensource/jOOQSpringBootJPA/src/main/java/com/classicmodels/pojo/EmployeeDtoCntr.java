package com.classicmodels.pojo;

import java.io.Serializable;

public class EmployeeDtoCntr implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String firstName;
    private final String lastName;
    private final Integer salary;
    private final String leastSalary;

    public EmployeeDtoCntr(String firstName, String lastName, Integer salary, String leastSalary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.leastSalary = leastSalary;
    }   
    
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getSalary() {
        return salary;
    }

    public String getLeastSalary() {
        return leastSalary;
    }

    @Override
    public String toString() {
        return "EmployeeDtoCntr{" + "firstName=" + firstName 
                + ", lastName=" + lastName 
                + ", salary=" + salary 
                + ", leastSalary=" + leastSalary + '}';
    }
    
}