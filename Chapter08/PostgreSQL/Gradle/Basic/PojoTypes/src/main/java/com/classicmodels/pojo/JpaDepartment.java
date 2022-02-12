package com.classicmodels.pojo;

import jakarta.persistence.Column;
import java.io.Serializable;

public class JpaDepartment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "name") 
    private String depName;
    
    @Column(name = "code") 
    private Short depCode;
    
    @Column(name = "topic") 
    private String[] depTopic;   

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
        return "JpaDepartment{" + "depName=" + depName + ", depCode=" + depCode 
                + ", depTopic=" + depTopic + '}';
    }     
}