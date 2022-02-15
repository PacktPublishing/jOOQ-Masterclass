package com.classicmodels.pojo;

import java.io.Serializable;

public class FlatProductline implements Serializable {

    private static final long serialVersionUID = 1L;

    private String productLine;
    private Long code;
    private String productName;    
    private String productVendor;    
    private Integer quantityInStock;
    
    public FlatProductline() {}

    public FlatProductline(String productLine, Long code, String productName, 
            String productVendor, Integer quantityInStock) {
        this.productLine = productLine;
        this.code = code;
        this.productName = productName;
        this.productVendor = productVendor;
        this.quantityInStock = quantityInStock;
    }

    @Override
    public String toString() {
        return "FlatProductline{" + "productLine=" + productLine + ", code=" + code 
                + ", productName=" + productName + ", productVendor=" + productVendor 
                + ", quantityInStock=" + quantityInStock + '}';
    }       
}