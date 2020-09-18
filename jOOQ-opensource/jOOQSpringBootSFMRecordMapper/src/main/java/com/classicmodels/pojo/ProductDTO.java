package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.Objects;

public class ProductDTO implements Serializable {

    private static final long serialVersionUID = 1;
    
    private Long productId;
    private String productName;
    private String productVendor;
    private Short quantityInStock;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
        
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductVendor() {
        return productVendor;
    }

    public void setProductVendor(String productVendor) {
        this.productVendor = productVendor;
    }

    public Short getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Short quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    @Override
    public int hashCode() {
        
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.productName);
        hash = 23 * hash + Objects.hashCode(this.productVendor);
        hash = 23 * hash + Objects.hashCode(this.quantityInStock);
        
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
        
        final ProductDTO other = (ProductDTO) obj;
        if (!Objects.equals(this.productName, other.productName)) {
            return false;
        }
        
        if (!Objects.equals(this.productVendor, other.productVendor)) {
            return false;
        }
        
        if (!Objects.equals(this.quantityInStock, other.quantityInStock)) {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString() {
        return "ProductDTO{" + "productName=" + productName 
                + ", productVendor=" + productVendor 
                + ", quantityInStock=" + quantityInStock + '}';
    }
       
}