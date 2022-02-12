package com.classicmodels.pojo;

import java.io.Serializable;

public class SimpleDepartment implements Serializable {

    private static final long serialVersionUID = 1L;

    private String depName;
    private Integer depCode;
    private String[] depTopic;

    public SimpleDepartment(String depName, Integer depCode, String[] depTopic) {
        this.depName = depName;
        this.depCode = depCode;
        this.depTopic = depTopic;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public Integer getDepCode() {
        return depCode;
    }

    public void setDepCode(Integer depCode) {
        this.depCode = depCode;
    }

    public String[] getDepTopic() {
        return depTopic;
    }

    public void setDepTopic(String[] depTopic) {
        this.depTopic = depTopic;
    }

    @Override
    public String toString() {
        return "SimpleDepartment{" + "depName=" + depName + ", depCode=" + depCode 
                + ", depTopic=" + depTopic + '}';
    }     
}