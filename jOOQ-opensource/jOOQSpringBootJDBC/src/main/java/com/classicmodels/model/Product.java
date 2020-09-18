package com.classicmodels.model;

import java.io.Serializable;
import org.springframework.data.annotation.Id;

public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long productId;
    private String productName;
    private String productLine;
    private String productScale;
    private String productVendor;
    private String productDescription;
    private Short quantityInStock;
    private Float buyPrice;
    private Float msrp;     

    @Override
    public String toString() {
        return "Product{" + "productId=" + productId + ", productName=" + productName 
                + ", productLine=" + productLine + ", productScale=" + productScale 
                + ", productVendor=" + productVendor + ", productDescription=" + productDescription 
                + ", quantityInStock=" + quantityInStock + ", buyPrice=" + buyPrice 
                + ", msrp=" + msrp + '}';
    }
        
}
