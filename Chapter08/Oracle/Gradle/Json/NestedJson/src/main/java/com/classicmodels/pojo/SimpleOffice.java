package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SimpleOffice implements Serializable {

    private static final long serialVersionUID = 1;
    
    private String officeCode;
    private String officeCity;
    private String officeCountry;
    private List<SimpleDepartment> departments;
    private List<SimpleEmployee> employees;
    private List<SimpleManager> managers;

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getOfficeCity() {
        return officeCity;
    }

    public void setOfficeCity(String officeCity) {
        this.officeCity = officeCity;
    }

    public String getOfficeCountry() {
        return officeCountry;
    }

    public void setOfficeCountry(String officeCountry) {
        this.officeCountry = officeCountry;
    }

    public List<SimpleDepartment> getDepartments() {
        return departments;
    }

    public void setDepartments(List<SimpleDepartment> departments) {
        this.departments = departments;
    }

    public List<SimpleEmployee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<SimpleEmployee> employees) {
        this.employees = employees;
    }

    public List<SimpleManager> getManagers() {
        return managers;
    }

    public void setManagers(List<SimpleManager> managers) {
        this.managers = managers;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.officeCode);
        hash = 67 * hash + Objects.hashCode(this.officeCity);
        hash = 67 * hash + Objects.hashCode(this.officeCountry);
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
        final SimpleOffice other = (SimpleOffice) obj;
        if (!Objects.equals(this.officeCode, other.officeCode)) {
            return false;
        }
        if (!Objects.equals(this.officeCity, other.officeCity)) {
            return false;
        }
        if (!Objects.equals(this.officeCountry, other.officeCountry)) {
            return false;
        }
        return true;
    }
        
    @Override
    public String toString() {
        return "SimpleOffice{" + "officeCode=" + officeCode + ", officeCity=" + officeCity 
                + ", officeCountry=" + officeCountry + ", departments=" + departments 
                + ", employees=" + employees + ", managers=" + managers + '}';
    }        
}
