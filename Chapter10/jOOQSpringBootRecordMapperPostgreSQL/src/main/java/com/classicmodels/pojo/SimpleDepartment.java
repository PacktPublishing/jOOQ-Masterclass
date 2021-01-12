package com.classicmodels.pojo;

import java.io.Serializable;

public class SimpleDepartment implements Serializable {

    private static final long serialVersionUID = 1L;

    private String depName;
    private Short depCode;
    private String[] depTopic;

    public SimpleDepartment(String depName, Short depCode, String[] depTopic) {
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

    public Short getDepCode() {
        return depCode;
    }

    public void setDepCode(Short depCode) {
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