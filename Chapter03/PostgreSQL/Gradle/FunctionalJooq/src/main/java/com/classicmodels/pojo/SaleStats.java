package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.List;

public class SaleStats implements Serializable {

    private static final long serialVersionUID = 1;

    private double totalSale;
    private List<Double> sales;

    private SaleStats() {
    }

    public SaleStats(double totalSale, List<Double> sales) {
        this.totalSale = totalSale;
        this.sales = sales;
    }

    public double getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(double totalSale) {
        this.totalSale = totalSale;
    }

    public List<Double> getSales() {
        return sales;
    }

    public void setSales(List<Double> sales) {
        this.sales = sales;
    }

    @Override
    public String toString() {
        return "SaleStats{" + "totalSale=" + totalSale + ", sales=" + sales + '}';
    }    
}