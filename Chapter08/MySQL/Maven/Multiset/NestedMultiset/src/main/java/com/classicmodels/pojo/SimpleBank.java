package com.classicmodels.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class SimpleBank implements Serializable {

    private static final long serialVersionUID = 1;
    
    private String bankName;
    private BigDecimal transferAmount;

    public SimpleBank(String bankName, BigDecimal transferAmount) {
        this.bankName = bankName;
        this.transferAmount = transferAmount;
    }        

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.bankName);
        hash = 29 * hash + Objects.hashCode(this.transferAmount);
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
        final SimpleBank other = (SimpleBank) obj;
        if (!Objects.equals(this.bankName, other.bankName)) {
            return false;
        }
        if (!Objects.equals(this.transferAmount, other.transferAmount)) {
            return false;
        }
        return true;
    }        

    @Override
    public String toString() {
        return "SimpleBank{" + "bankName=" + bankName + ", transferAmount=" + transferAmount + '}';
    }   
}

