package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.Objects;

public class SimpleProduct implements Serializable {

    private static final long serialVersionUID = 1;
    
    private String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public int hashCode() {
        
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.productName);
        
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
        if (!Objects.equals(this.productName, other.productName)) {
            return false;
        }
        
        return true;
    }        

    @Override
    public String toString() {
        return "Product{" + "productName=" + productName + '}';
    }        
    
}
