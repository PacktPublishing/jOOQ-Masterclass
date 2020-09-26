package com.classicmodels.pojo;

import java.io.Serializable;

public class ProductPojo implements Serializable {

    private static final long serialVersionUID = 1;

    private Long productId;
    private String productName;
    private String productLine;
    private String productVendor;
    private Float buyPrice;

    private ProductPojo() {
    }

    public ProductPojo(Long productId, String productName,
            String productLine, String productVendor, Float buyPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productLine = productLine;
        this.productVendor = productVendor;
        this.buyPrice = buyPrice;
    }

    public ProductPojo(ProductPojo value) {
        this.productId = value.productId;
        this.productName = value.productName;
        this.productLine = value.productLine;
        this.productVendor = value.productVendor;
        this.buyPrice = value.buyPrice;
    }

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

    public String getProductLine() {
        return productLine;
    }

    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }    
    
    public String getProductVendor() {
        return productVendor;
    }

    public void setProductVendor(String productVendor) {
        this.productVendor = productVendor;
    }

    public Float getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Float buyPrice) {
        this.buyPrice = buyPrice;
    }

    @Override
    public String toString() {
        return "ProductPojo{" + "productId=" + productId 
                + ", productName=" + productName 
                + ", productLine=" + productLine 
                + ", productVendor=" + productVendor + ", buyPrice=" + buyPrice + '}';
    }
   
}