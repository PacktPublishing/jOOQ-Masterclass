package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.Objects;

public class SimpleDepartment implements Serializable {

    private static final long serialVersionUID = 1;
    
    private String departmentName;
    private String departmentPhone;

    public SimpleDepartment(String departmentName, String departmentPhone) {
        this.departmentName = departmentName;
        this.departmentPhone = departmentPhone;
    }        

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentPhone() {
        return departmentPhone;
    }

    public void setDepartmentPhone(String departmentPhone) {
        this.departmentPhone = departmentPhone;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.departmentName);
        hash = 89 * hash + Objects.hashCode(this.departmentPhone);
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
        final SimpleDepartment other = (SimpleDepartment) obj;
        if (!Objects.equals(this.departmentName, other.departmentName)) {
            return false;
        }
        if (!Objects.equals(this.departmentPhone, other.departmentPhone)) {
            return false;
        }
        return true;
    }    
    
    @Override
    public String toString() {
        return "SimpleDepartment{" + "departmentName=" + departmentName 
                + ", departmentPhone=" + departmentPhone + '}';
    }   
}
