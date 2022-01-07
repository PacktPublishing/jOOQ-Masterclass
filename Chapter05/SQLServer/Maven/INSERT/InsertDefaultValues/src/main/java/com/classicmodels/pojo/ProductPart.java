package com.classicmodels.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductPart implements Serializable {

    private static final long serialVersionUID = 1L;

    private String productName;
    private String productLine;
    private Long code;
    private String productVendor;
    private BigDecimal buyPrice;
    private BigDecimal msrp;
    
    public ProductPart(ProductPart value) {
        this.productName = value.productName;
        this.productLine = value.productLine;
        this.code = value.code;
        this.productVendor = value.productVendor;
        this.buyPrice = value.buyPrice;
        this.msrp = value.msrp;
    }
    
    public ProductPart(Long code) {
        this.code = code;
    }

    public ProductPart(String productName, String productLine, Long code, 
            String productVendor, BigDecimal buyPrice, BigDecimal msrp) {
        this.productName = productName;
        this.productLine = productLine;
        this.code = code;
        this.productVendor = productVendor;
        this.buyPrice = buyPrice;
        this.msrp = msrp;
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

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }   
    
    public String getProductVendor() {
        return productVendor;
    }

    public void setProductVendor(String productVendor) {
        this.productVendor = productVendor;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public BigDecimal getMsrp() {
        return msrp;
    }

    public void setMsrp(BigDecimal msrp) {
        this.msrp = msrp;
    }
}