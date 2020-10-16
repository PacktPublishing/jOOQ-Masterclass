package com.classicmodels.pojo;

import java.io.Serializable;

public interface EmployeeLeastSalary extends Serializable {

    public String getFirstName();

    public String getLastName();

    public Integer getSalary();

    public String getLeastSalary();
    
    public void setFirstName(String value);

    public void setLastName(String value);

    public void setSalary(Integer value);

    public void setLeastSalary(String value);
}
