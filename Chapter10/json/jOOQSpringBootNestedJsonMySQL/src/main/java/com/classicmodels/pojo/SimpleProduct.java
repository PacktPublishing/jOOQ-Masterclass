package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SimpleProduct implements Serializable {

    private static final long serialVersionUID = 1;
    
    private String productName;
    private String productVendor;
    private Short quantityInStock;
    private List<SimpleOrderdetail> orderdetail;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductVendor() {
        return productVendor;
    }

    public void setProductVendor(String productVendor) {
        this.productVendor = productVendor;
    }

    public Short getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Short quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public List<SimpleOrderdetail> getOrderdetail() {
        return orderdetail;
    }

    public void setOrderdetail(List<SimpleOrderdetail> orderdetail) {
        this.orderdetail = orderdetail;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.productName);
        hash = 37 * hash + Objects.hashCode(this.productVendor);
        hash = 37 * hash + Objects.hashCode(this.quantityInStock);
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
        final SimpleProduct other = (SimpleProduct) obj;
        if (!Objects.equals(this.productName, other.productName)) {
            return false;
        }
        if (!Objects.equals(this.productVendor, other.productVendor)) {
            return false;
        }
        if (!Objects.equals(this.quantityInStock, other.quantityInStock)) {
            return false;
        }
        return true;
    }        

    @Override
    public String toString() {
        return "Product{" + "productName=" + productName + ", productVendor=" + productVendor 
                + ", quantityInStock=" + quantityInStock + ", orderdetail=" + orderdetail + '}';
    }       
}
