package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.Objects;

public class SimpleSale implements Serializable {

    private static final long serialVersionUID = 1;
    
    private Integer fiscalYear;
    private Double sale;

    public Integer getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(Integer fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public Double getSale() {
        return sale;
    }

    public void setSale(Double sale) {
        this.sale = sale;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.fiscalYear);
        hash = 53 * hash + Objects.hashCode(this.sale);
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
        final SimpleSale other = (SimpleSale) obj;
        if (!Objects.equals(this.fiscalYear, other.fiscalYear)) {
            return false;
        }
        if (!Objects.equals(this.sale, other.sale)) {
            return false;
        }
        return true;
    }
        
    @Override
    public String toString() {
        return "SimpleSale{" + "fiscalYear=" + fiscalYear + ", sale=" + sale + '}';
    }        
}
