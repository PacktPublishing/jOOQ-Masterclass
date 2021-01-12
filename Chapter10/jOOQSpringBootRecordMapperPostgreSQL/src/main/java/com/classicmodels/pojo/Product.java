package com.classicmodels.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long productId;
    private String productName;
    private String productLine;
    private String productScale;
    private String productVendor;
    private String productDescription;
    private Short quantityInStock;
    private BigDecimal buyPrice;
    private BigDecimal msrp;
    private Object specs;
    
    public Product() {}

    public Product(Long productId, String productName, String productLine, String productScale, 
            String productVendor, String productDescription, Short quantityInStock, 
            BigDecimal buyPrice, BigDecimal msrp, Object specs) {
        this.productId = productId;
        this.productName = productName;
        this.productLine = productLine;
        this.productScale = productScale;
        this.productVendor = productVendor;
        this.productDescription = productDescription;
        this.quantityInStock = quantityInStock;
        this.buyPrice = buyPrice;
        this.msrp = msrp;
        this.specs = specs;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.productId);
        hash = 73 * hash + Objects.hashCode(this.productName);
        hash = 73 * hash + Objects.hashCode(this.productLine);
        hash = 73 * hash + Objects.hashCode(this.productScale);
        hash = 73 * hash + Objects.hashCode(this.productVendor);
        hash = 73 * hash + Objects.hashCode(this.productDescription);
        hash = 73 * hash + Objects.hashCode(this.quantityInStock);
        hash = 73 * hash + Objects.hashCode(this.buyPrice);
        hash = 73 * hash + Objects.hashCode(this.msrp);
        hash = 73 * hash + Objects.hashCode(this.specs);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Product other = (Product) obj;
        if (!Objects.equals(this.productName, other.productName)) {
            return false;
        }
        if (!Objects.equals(this.productLine, other.productLine)) {
            return false;
        }
        if (!Objects.equals(this.productScale, other.productScale)) {
            return false;
        }
        if (!Objects.equals(this.productVendor, other.productVendor)) {
            return false;
        }
        if (!Objects.equals(this.productDescription, other.productDescription)) {
            return false;
        }
        if (!Objects.equals(this.productId, other.productId)) {
            return false;
        }
        if (!Objects.equals(this.quantityInStock, other.quantityInStock)) {
            return false;
        }
        if (!Objects.equals(this.buyPrice, other.buyPrice)) {
            return false;
        }
        if (!Objects.equals(this.msrp, other.msrp)) {
            return false;
        }
        if (!Objects.equals(this.specs, other.specs)) {
            return false;
        }
        return true;
    }    

    @Override
    public String toString() {
        return "Product{" + "productId=" + productId + ", productName=" + productName 
                + ", productLine=" + productLine + ", productScale=" + productScale 
                + ", productVendor=" + productVendor + ", quantityInStock=" + quantityInStock 
                + ", buyPrice=" + buyPrice + '}';
    }   
}