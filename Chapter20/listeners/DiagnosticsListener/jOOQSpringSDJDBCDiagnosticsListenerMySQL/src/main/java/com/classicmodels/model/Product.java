package com.classicmodels.model;

import java.io.Serializable;
import java.util.Objects;
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

    public String getProductLine() {
        return productLine;
    }

    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }

    public String getProductScale() {
        return productScale;
    }

    public void setProductScale(String productScale) {
        this.productScale = productScale;
    }

    public String getProductVendor() {
        return productVendor;
    }

    public void setProductVendor(String productVendor) {
        this.productVendor = productVendor;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Short getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Short quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public Float getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Float buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Float getMsrp() {
        return msrp;
    }

    public void setMsrp(Float msrp) {
        this.msrp = msrp;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.productId);
        hash = 47 * hash + Objects.hashCode(this.productName);
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
        
        if (!Objects.equals(this.productId, other.productId)) {
            return false;
        }
        
        return true;
    }
                
    @Override
    public String toString() {
        return "Product{" + "productId=" + productId + ", productName=" + productName 
                + ", productLine=" + productLine + ", productScale=" + productScale 
                + ", productVendor=" + productVendor + ", productDescription=" + productDescription 
                + ", quantityInStock=" + quantityInStock + ", buyPrice=" + buyPrice 
                + ", msrp=" + msrp + '}';
    }
        
}
