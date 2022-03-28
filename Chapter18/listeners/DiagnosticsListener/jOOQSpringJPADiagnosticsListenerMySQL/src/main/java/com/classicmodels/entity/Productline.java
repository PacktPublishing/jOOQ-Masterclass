package com.classicmodels.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

@Entity
public class Productline implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 50)
    private String productLine;
    
    @Column(nullable=false)
    private Long code;
    
    @Column(length = 4000)
    private String textDescription;

    @Lob
    private String htmlDescription;

    @Lob
    private byte[] image;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "productLine", orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
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

}
