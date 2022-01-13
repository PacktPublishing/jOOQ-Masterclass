package com.classicmodels.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

public class SalePart implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long saleId;
    private BigDecimal sale;

    public SalePart() {
    }

    public SalePart(SalePart value) {
        this.saleId = value.saleId;
        this.sale = value.sale;
    }

    public SalePart(Long saleId, BigDecimal sale) {
        this.saleId = saleId;
        this.sale = sale;
    }    
    
    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public BigDecimal getSale() {
        return sale;
    }

    public void setSale(BigDecimal sale) {
        this.sale = sale;
    }

   
}
