package com.classicmodels.pojo;

import java.io.Serializable;
import jooq.generated.tables.pojos.JooqProduct;

public class ProductPart extends JooqProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    private String productStatus;
    private String productConsumption;

    public ProductPart() {
    }

    public ProductPart(ProductPart value) {
        this.productStatus = value.productStatus;
        this.productConsumption = value.productConsumption;
    }

    public ProductPart(String productStatus, String productConsumption) {
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