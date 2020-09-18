package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.Objects;

public class OrderDetailDTO implements Serializable {

    private static final long serialVersionUID = 1;
        
    private Long productId;
    private Integer quantityOrdered;
    private Float priceEach;
    private ProductDTO product;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    

    public Integer getQuantityOrdered() {
        return quantityOrdered;
    }

    public void setQuantityOrdered(Integer quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    public Float getPriceEach() {
        return priceEach;
    }

    public void setPriceEach(Float priceEach) {
        this.priceEach = priceEach;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }        

    @Override
    public int hashCode() {
        
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.productId);
        hash = 97 * hash + Objects.hashCode(this.quantityOrdered);
        hash = 97 * hash + Objects.hashCode(this.priceEach);
        
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
        
        final OrderDetailDTO other = (OrderDetailDTO) obj;
        if (!Objects.equals(this.productId, other.productId)) {
            return false;
        }

        if (!Objects.equals(this.quantityOrdered, other.quantityOrdered)) {
            return false;
        }
        
        if (!Objects.equals(this.priceEach, other.priceEach)) {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString() {
        return "OrderDetailDTO{" + "productId=" + productId 
                + ", quantityOrdered=" + quantityOrdered 
                + ", priceEach=" + priceEach + ", product=" + product + '}';
    }
                   
}