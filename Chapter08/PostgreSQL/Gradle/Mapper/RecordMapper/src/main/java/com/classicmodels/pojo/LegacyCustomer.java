package com.classicmodels.pojo;

import java.math.BigDecimal;

public final class LegacyCustomer {

    private final String customerName;
    private final String customerPhone;
    private final BigDecimal creditLimit;

    private LegacyCustomer(CustomerBuilder builder) {
        this.customerName = builder.customerName;
        this.customerPhone = builder.customerPhone;
        this.creditLimit = builder.creditLimit;
    }

    public static CustomerBuilder getBuilder(String customerName) {
        return new LegacyCustomer.CustomerBuilder(customerName);
    }

    public static final class CustomerBuilder {

        private String customerName;
        private String customerPhone;
        private BigDecimal creditLimit;

        public CustomerBuilder(String customerName) {
            this.customerName = customerName;
        }

        public CustomerBuilder customerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public CustomerBuilder customerPhone(String customerPhone) {
            this.customerPhone = customerPhone;
            return this;
        }

        public CustomerBuilder creditLimit(BigDecimal creditLimit) {
            this.creditLimit = creditLimit;
            return this;
        }

        public LegacyCustomer build() {
            return new LegacyCustomer(this);
        }
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    @Override
    public String toString() {
        return "LegacyCustomer{" + "customerName=" + customerName 
                + ", customerPhone=" + customerPhone + ", creditLimit=" + creditLimit + '}';
    }        
}
