package com.classicmodels.pojo;

import java.io.Serializable;

public class EmployeePojo implements Serializable {

    private static final long serialVersionUID = 1;

    private Long employeeNumber;
    private String firstName;
    private String jobTitle;
    private Float sumSales;

    private EmployeePojo() {
    }

    public EmployeePojo(Long employeeNumber, String firstName, String jobTitle, Float sumSales) {
        this.employeeNumber = employeeNumber;
        this.firstName = firstName;
        this.jobTitle = jobTitle;
        this.sumSales = sumSales;
    }

    public EmployeePojo(EmployeePojo value) {
        this.employeeNumber = value.employeeNumber;
        this.firstName = value.firstName;
        this.jobTitle = value.jobTitle;
        this.sumSales = value.sumSales;
    }

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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Float getSumSales() {
        return sumSales;
    }

    public void setSumSales(Float sumSales) {
        this.sumSales = sumSales;
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" + "employeeNumber=" + employeeNumber
                + ", firstName=" + firstName + ", jobTitle=" + jobTitle + ", sumSales=" + sumSales + '}';
    }

}
