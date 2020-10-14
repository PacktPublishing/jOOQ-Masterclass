package com.classicmodels.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
    
    @MappedCollection(idColumn="product_line", keyColumn="product_id")
    List<Product> products = new ArrayList<>();               

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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
        
    @Override
    public String toString() {
        return "ProductLine{" + "productLine=" + productLine + ", textDescription=" 
                + textDescription + ", htmlDescription=" + htmlDescription 
                + ", image=" + image + ", products=" + products + '}';
    }        
}
