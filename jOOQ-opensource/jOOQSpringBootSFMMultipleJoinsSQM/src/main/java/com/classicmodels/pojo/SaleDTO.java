package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.Objects;

public class SaleDTO implements Serializable {

    private static final long serialVersionUID = 1;
    
    private Float sale;

    public Float getSale() {
        return sale;
    }

    public void setSale(Float sale) {
        this.sale = sale;
    }

    @Override
    public int hashCode() {
        
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.sale);
        
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
        
        final SaleDTO other = (SaleDTO) obj;
        if (!Objects.equals(this.sale, other.sale)) {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString() {
        return "SaleDTO{" + "sale=" + sale + '}';
    }
    
}
