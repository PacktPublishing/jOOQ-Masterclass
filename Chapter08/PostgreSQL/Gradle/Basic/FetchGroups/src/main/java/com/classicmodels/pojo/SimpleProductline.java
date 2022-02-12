package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.Objects;

public class SimpleProductline implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public Long code;
    public String productLine;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.code);
        hash = 67 * hash + Objects.hashCode(this.productLine);
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
        final SimpleProductline other = (SimpleProductline) obj;
        if (!Objects.equals(this.productLine, other.productLine)) {
            return false;
        }
        if (!Objects.equals(this.code, other.code)) {
            return false;
        }
        return true;
    }        

    @Override
    public String toString() {
        return "SimpleProductline{" + "code=" + code + ", productLine=" + productLine + '}';
    }   
}
