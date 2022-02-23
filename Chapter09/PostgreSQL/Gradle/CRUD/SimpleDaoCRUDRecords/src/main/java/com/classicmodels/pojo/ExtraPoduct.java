package com.classicmodels.pojo;

import java.io.Serializable;
import jooq.generated.tables.pojos.Product;

public class ExtraPoduct extends Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private String productStatus;
    private String productConsumption;

    public ExtraPoduct() {
    }

    public ExtraPoduct(ExtraPoduct value) {
        this.productStatus = value.productStatus;
        this.productConsumption = value.productConsumption;
    }

    public ExtraPoduct(String productStatus, String productConsumption) {
        this.productStatus = productStatus;
        this.productConsumption = productConsumption;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public String getProductConsumption() {
        return productConsumption;
    }

    public void setProductConsumption(String productConsumption) {
        this.productConsumption = productConsumption;
    }
     
}