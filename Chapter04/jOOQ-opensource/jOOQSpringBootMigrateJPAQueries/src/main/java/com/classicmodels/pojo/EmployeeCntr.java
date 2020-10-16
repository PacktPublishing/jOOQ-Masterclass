package com.classicmodels.pojo;

import java.io.Serializable;

public class EmployeeCntr implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String firstName;
    private final String lastName;
    private final Integer salary;    

    public EmployeeCntr(String firstName, String lastName, Integer salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;    
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
   
    @Override
    public String toString() {
        return "EmployeeDtoCntr{" + "firstName=" + firstName 
                + ", lastName=" + lastName 
                + ", salary=" + salary + '}';
    }
    
}