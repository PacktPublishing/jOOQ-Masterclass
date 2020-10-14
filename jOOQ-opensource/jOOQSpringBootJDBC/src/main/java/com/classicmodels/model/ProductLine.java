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

    public String getHtmlDescription() {
        return htmlDescription;
    }

    public void setHtmlDescription(String htmlDescription) {
        this.htmlDescription = htmlDescription;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
        
    @Override
    public String toString() {
        return "ProductLine{" + "productLine=" + productLine + ", textDescription=" 
                + textDescription + ", htmlDescription=" + htmlDescription 
                + ", image=" + image + ", products=" + products + '}';
    }        
}
