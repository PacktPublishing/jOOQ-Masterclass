package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SimpleProduct implements Serializable {

    private static final long serialVersionUID = 1;
    
    private String productVendor;
    private Integer quantityInStock;
    private List<SimpleOrderdetail> orderdetail;

    public SimpleProduct(String productVendor, Integer quantityInStock, List<SimpleOrderdetail> orderdetail) {        
        this.productVendor = productVendor;
        this.quantityInStock = quantityInStock;
        this.orderdetail = orderdetail;
    }
        
    public String getProductVendor() {
        return productVendor;
    }

    public void setProductVendor(String productVendor) {
        this.productVendor = productVendor;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
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
        return "Product{" + ", productVendor=" + productVendor 
                + ", quantityInStock=" + quantityInStock + ", orderdetail=" + orderdetail + '}';
    }       
}
