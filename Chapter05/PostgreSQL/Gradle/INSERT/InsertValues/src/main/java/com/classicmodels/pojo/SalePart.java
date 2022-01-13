package com.classicmodels.pojo;

import java.io.Serializable;

public class SalePart implements Serializable {

    private static final long serialVersionUID = 1L;

    private Double sale;
    private Long employeeNumber;

    public SalePart() {
    }

    public SalePart(SalePart value) {
        this.sale = value.sale;
        this.employeeNumber = value.employeeNumber;
    }

    public SalePart(Double sale, Long employeeNumber) {
        this.sale = sale;
        this.employeeNumber = employeeNumber;
    }

    public Double getSale() {
        return sale;
    }

    public void setSale(Double sale) {
        this.sale = sale;
    }

    public Long getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(Long employeeNumber) {
        this.employeeNumber = employeeNumber;
    }   
}
