package com.classicmodels.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("productline")
public class ProductLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String productLine;
    private String textDescription;
    private String htmlDescription;
    private byte[] image;
    
    @MappedCollection
    Set<Product> products = new HashSet<>();               

    @Override
    public String toString() {
        return "ProductLine{" + "productLine=" + productLine + ", textDescription=" 
                + textDescription + ", htmlDescription=" + htmlDescription 
                + ", image=" + image + ", products=" + products + '}';
    }        
}
