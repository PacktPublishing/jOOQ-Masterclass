package com.classicmodels.pojo;

import java.io.Serializable;

public class EmployeeData implements Serializable {

    private static final long serialVersionUID = 1;

    private Long employeeNumber;    
    private int salary;
    private EmployeeName employeeName;

    private EmployeeData() {
    }

    public EmployeeData(Long employeeNumber, int salary, EmployeeName employeeName) {
        this.employeeNumber = employeeNumber;
        this.salary = salary;
        this.employeeName = employeeName;
    }

    public Long getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(Long employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public EmployeeName getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(EmployeeName employeeName) {
        this.employeeName = employeeName;
    }

    @Override
    public String toString() {
        return "EmployeeData{" + "employeeNumber=" + employeeNumber 
                + ", salary=" + salary + ", employeeName=" + employeeName + '}';
    }    
}
