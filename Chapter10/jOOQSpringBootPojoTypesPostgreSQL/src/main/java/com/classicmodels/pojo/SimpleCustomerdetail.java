package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.Objects;

public class SimpleCustomerdetail implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public String city;
    public String country;

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.city);
        hash = 97 * hash + Objects.hashCode(this.country);
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
        final SimpleCustomerdetail other = (SimpleCustomerdetail) obj;
        if (!Objects.equals(this.city, other.city)) {
            return false;
        }
        if (!Objects.equals(this.country, other.country)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SimpleCustomerdetail{" + "city=" + city + ", country=" + country + '}';
    }
   
}
