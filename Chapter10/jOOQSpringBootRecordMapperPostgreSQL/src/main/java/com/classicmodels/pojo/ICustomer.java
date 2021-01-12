package com.classicmodels.pojo;

import java.io.Serializable;

public interface ICustomer extends Serializable {
        
    public String getCustomerName();
    public void setCustomerName(String value);
    
    /**
     * Load data from another generated Record/POJO implementing the common interface ICustomer
     */
    public void from(ICustomer from);

    /**
     * Copy data into another generated Record/POJO implementing the common interface ICustomer
     */
    public <E extends ICustomer> E into(E into);
}
