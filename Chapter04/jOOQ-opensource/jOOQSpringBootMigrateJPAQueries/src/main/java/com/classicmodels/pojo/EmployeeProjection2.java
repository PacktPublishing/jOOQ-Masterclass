package com.classicmodels.pojo;

import java.io.Serializable;

public interface EmployeeProjection2 extends Serializable {

    public String getFirstName();

    public String getLastName();

    public Integer getSalary();
   
    public void setFirstName(String value);

    public void setLastName(String value);

    public void setSalary(Integer value);   
}
