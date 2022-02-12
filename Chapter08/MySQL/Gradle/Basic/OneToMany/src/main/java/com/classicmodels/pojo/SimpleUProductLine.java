package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimpleUProductLine implements Serializable {

    private static final long serialVersionUID = 1;
  
    private String productLine;
    private String textDescription;   
    
    private List<SimpleUProduct> products = new ArrayList<>();

    public SimpleUProductLine(String productLine, String textDescription) {
        this.productLine = productLine;
        this.textDescription = textDescription;
    }
        
    public String getProductLine() {
        return productLine;
    }

    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }

    public String getTextDescription() {
        return textDescription;
    }

    public void setTextDescription(String textDescription) {
        this.textDescription = textDescription;
    }

    public List<SimpleUProduct> getProducts() {
        return products;
    }

    public void setProducts(List<SimpleUProduct> products) {
        this.products = products;
    }

    @Override
    public int hashCode() {
        
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.productLine);
        hash = 37 * hash + Objects.hashCode(this.textDescription);
        
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
        
        final SimpleUProductLine other = (SimpleUProductLine) obj;
        if (!Objects.equals(this.productLine, other.productLine)) {
            return false;
        }
        
        if (!Objects.equals(this.textDescription, other.textDescription)) {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString() {
        return "SimpleUProductLine{" + "productLine=" + productLine 
                + ", textDescription=" + textDescription + '}';
    }      
}