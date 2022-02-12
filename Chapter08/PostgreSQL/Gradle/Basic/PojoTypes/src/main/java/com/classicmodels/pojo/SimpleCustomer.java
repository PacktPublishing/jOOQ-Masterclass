package com.classicmodels.pojo;

import java.io.Serializable;
import java.time.YearMonth;
import java.util.Objects;

public class SimpleCustomer implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public String customerName;
    public YearMonth ym;

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.customerName);
        hash = 89 * hash + Objects.hashCode(this.ym);
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
        final SimpleCustomer other = (SimpleCustomer) obj;
        if (!Objects.equals(this.customerName, other.customerName)) {
            return false;
        }
        if (!Objects.equals(this.ym, other.ym)) {
            return false;
        }
        return true;
    }
        
    @Override
    public String toString() {
        return "SimpleCustomer{" + "customerName=" + customerName + ", ym=" + ym + '}';
    }        
}
