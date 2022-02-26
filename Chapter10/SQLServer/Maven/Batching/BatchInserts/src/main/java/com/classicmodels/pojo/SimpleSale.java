package com.classicmodels.pojo;

import java.io.Serializable;

public class SimpleSale implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer fiscalYear;
    private Long employeeNumber;
    private Double sale;    
    private Integer fiscalMonth;
    private Double revenueGrowth;

    public SimpleSale(Integer fiscalYear, Long employeeNumber, Double sale, Integer fiscalMonth, Double revenueGrowth) {
        this.fiscalYear = fiscalYear;
        this.employeeNumber = employeeNumber;
        this.sale = sale;
        this.fiscalMonth = fiscalMonth;
        this.revenueGrowth = revenueGrowth;
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

    public Integer getFiscalMonth() {
        return fiscalMonth;
    }

    public void setFiscalMonth(Integer fiscalMonth) {
        this.fiscalMonth = fiscalMonth;
    }

    public Double getRevenueGrowth() {
        return revenueGrowth;
    }

    public void setRevenueGrowth(Double revenueGrowth) {
        this.revenueGrowth = revenueGrowth;
    }       
}