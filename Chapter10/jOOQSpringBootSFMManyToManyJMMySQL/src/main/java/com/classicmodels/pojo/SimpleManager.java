package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import org.simpleflatmapper.map.annotation.Key;

public class SimpleManager implements Serializable {

    private static final long serialVersionUID = 1;

    @Key
    private Long managerId;
    
    private String managerName;
    private List<SimpleOffice> offices;

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public List<SimpleOffice> getOffices() {
        return offices;
    }

    public void setOffices(List<SimpleOffice> offices) {
        this.offices = offices;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.managerId);
        hash = 83 * hash + Objects.hashCode(this.managerName);
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

        final SimpleManager other = (SimpleManager) obj;
        if (!Objects.equals(this.managerName, other.managerName)) {
            return false;
        }

        if (!Objects.equals(this.managerId, other.managerId)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Manager{" + "managerId=" + managerId 
                + ", managerName=" + managerName + ", offices=" + offices + '}';
    }

}
