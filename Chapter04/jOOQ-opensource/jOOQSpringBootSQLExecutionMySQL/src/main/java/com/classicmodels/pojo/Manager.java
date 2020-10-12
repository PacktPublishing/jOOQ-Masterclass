package com.classicmodels.pojo;

import java.io.Serializable;

public class Manager implements Serializable {

    private static final long serialVersionUID = 1;

    private Long managerId;
    private String managerName;

    private Manager() {
    }

    public Manager(Long managerId, String managerName) {
        this.managerId = managerId;
        this.managerName = managerName;
    }

    public Manager(Manager value) {
        this.managerId = value.managerId;
        this.managerName = value.managerName;

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

    @Override
    public String toString() {
        return "Manager{" + "managerId=" + managerId
                + ", managerName=" + managerName + '}';
    }

}
