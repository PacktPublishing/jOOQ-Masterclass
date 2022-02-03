package com.classicmodels.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class SimpleProduct implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public Long productId;
    public BigDecimal buyPrice;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.productId);
        hash = 59 * hash + Objects.hashCode(this.buyPrice);
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
        final SimpleProduct other = (SimpleProduct) obj;
        if (!Objects.equals(this.productId, other.productId)) {
            return false;
        }
        if (!Objects.equals(this.buyPrice, other.buyPrice)) {
            return false;
        }
        return true;
    }
        
    @Override
    public String toString() {
        return "SimpleProduct{" + "productId=" + productId + ", buyPrice=" + buyPrice + '}';
    }   
}
