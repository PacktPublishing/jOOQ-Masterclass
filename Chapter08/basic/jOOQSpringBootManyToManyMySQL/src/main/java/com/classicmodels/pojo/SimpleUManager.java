package com.classicmodels.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimpleUManager implements Serializable {

    private static final long serialVersionUID = 1;
    
    private Long managerId;    
    private String managerName;
       
    private List<SimpleUOffice> offices = new ArrayList<>();

    public SimpleUManager(Long managerId, String managerName) {
        this.managerId = managerId;
        this.managerName = managerName;
    }      
    
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

    public List<SimpleUOffice> getOffices() {
        return offices;
    }

    public void setOffices(List<SimpleUOffice> offices) {
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

        final SimpleUManager other = (SimpleUManager) obj;
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
        return "SimpleUManager{" + "managerId=" + managerId + ", managerName=" + managerName + '}';
    }    
}
