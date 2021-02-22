package com.classicmodels.pojo;

import java.io.Serializable;

public class SimpleSale implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer fiscalYear;
    private Long employeeNumber;
    private Double sale;    

    public SimpleSale(Integer fiscalYear, Long employeeNumber, Double sale) {
        this.fiscalYear = fiscalYear;
        this.employeeNumber = employeeNumber;
        this.sale = sale;
    }        

    public Integer getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(Integer fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public Long getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(Long employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public Double getSale() {
        return sale;
    }

    public void setSale(Double sale) {
        this.sale = sale;
    }        
}
