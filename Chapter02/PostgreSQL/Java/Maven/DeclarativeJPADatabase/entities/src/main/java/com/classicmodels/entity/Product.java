package com.classicmodels.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(length = 70, nullable=false)
    private String productName;
    
    @Column(length = 10, nullable=false)
    private String productScale;
    
    @Column(length = 50, nullable=false)
    private String productVendor;
    
    @Lob
    @Column(nullable=false)
    private String productDescription;
    
    @Column(nullable=false)
    private Short quantityInStock;
    
    @Column(precision = 10, scale = 2, nullable=false)
    private Float buyPrice;
    
    @Column(precision = 10, scale = 2, nullable=false)
    private Float msrp;
    
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "product", orphanRemoval = true)
    private List<Orderdetail> orderdetails = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_line")
    private Productline productLine;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Productline getProductLine() {
        return productLine;
    }

    public void setProductLine(Productline productLine) {
        this.productLine = productLine;
    }   

    public String getProductScale() {
        return productScale;
    }

    public void setProductScale(String productScale) {
        this.productScale = productScale;
    }

    public String getProductVendor() {
        return productVendor;
    }

    public void setProductVendor(String productVendor) {
        this.productVendor = productVendor;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Short getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Short quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public Float getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Float buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Float getMsrp() {
        return msrp;
    }

    public void setMsrp(Float msrp) {
        this.msrp = msrp;
    }

    public List<Orderdetail> getOrderdetails() {
        return orderdetails;
    }

    public void setOrderdetails(List<Orderdetail> orderdetails) {
        this.orderdetails = orderdetails;
    }
    
    @Override
    public int hashCode() {
        return 2021;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        return productId != null && productId.equals(((Product) obj).productId);
    }

}
