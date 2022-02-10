package com.classicmodels.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class SimpleOrderdetail implements Serializable {

    private static final long serialVersionUID = 1;
    
    private Integer quantityOrdered;
    private BigDecimal priceEach;

    public SimpleOrderdetail(Integer quantityOrdered, BigDecimal priceEach) {
        this.quantityOrdered = quantityOrdered;
        this.priceEach = priceEach;
    }        

    public Integer getQuantityOrdered() {
        return quantityOrdered;
    }

    public void setQuantityOrdered(Integer quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    public BigDecimal getPriceEach() {
        return priceEach;
    }

    public void setPriceEach(BigDecimal priceEach) {
        this.priceEach = priceEach;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.quantityOrdered);
        hash = 29 * hash + Objects.hashCode(this.priceEach);
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
        final SimpleOrderdetail other = (SimpleOrderdetail) obj;
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
        return "Orderdetail{" + "quantityOrdered=" + quantityOrdered + ", priceEach=" + priceEach + '}';
    }        
}
