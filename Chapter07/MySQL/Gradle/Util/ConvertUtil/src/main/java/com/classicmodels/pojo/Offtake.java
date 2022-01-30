package com.classicmodels.pojo;

public class Offtake {

    private final Integer fiscalYear ;
    private final Double sale;
    private final Long employeeNumber;

    public Offtake(Integer fiscalYear, Double sale, Long employeeNumber) {
        this.fiscalYear = fiscalYear;
        this.sale = sale;
        this.employeeNumber = employeeNumber;
    }

    @Override
    public String toString() {
        return "Offtake{" + "fiscalYear=" + fiscalYear + ", "
                + "sale=" + sale + ", employeeNumber=" + employeeNumber + '}';
    }      
}