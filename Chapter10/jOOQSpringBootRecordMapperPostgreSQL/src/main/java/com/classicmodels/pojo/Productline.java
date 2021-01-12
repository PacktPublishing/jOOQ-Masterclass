package com.classicmodels.pojo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import jooq.generated.tables.pojos.Product;

public class Productline implements Serializable {

    private static final long serialVersionUID = 1L;

    private String productLine;
    private Long code;
    private String textDescription;
    private String htmlDescription;
    private byte[] image;
    private LocalDate createdOn;
    
    public Productline() {}
    
    public Productline(String productLine, Long code, String textDescription, 
            String htmlDescription, byte[] image, LocalDate createdOn) {
        this.productLine = productLine;
        this.code = code;
        this.textDescription = textDescription;
        this.htmlDescription = htmlDescription;
        this.image = image;
        this.createdOn = createdOn;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.productLine);
        hash = 29 * hash + Objects.hashCode(this.code);
        hash = 29 * hash + Objects.hashCode(this.createdOn);
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
        final Productline other = (Productline) obj;
        if (!Objects.equals(this.productLine, other.productLine)) {
            return false;
        }
        if (!Objects.equals(this.code, other.code)) {
            return false;
        }
        if (!Objects.equals(this.createdOn, other.createdOn)) {
            return false;
        }
        return true;
    }

    
    
    @Override
    public String toString() {
        return "Productline{" + "productLine=" + productLine + ", code=" + code 
                + ", createdOn=" + createdOn +  '}';
    }   
}