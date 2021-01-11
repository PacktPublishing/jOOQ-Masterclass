package com.classicmodels.pojos;

import java.io.Serializable;

public class SimpleDepartment implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private Short code;
    private String[] topic;
 
    public SimpleDepartment(String name, Short code, String[] topic) {
        this.name = name;
        this.code = code;
        this.topic = topic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getCode() {
        return code;
    }

    public void setCode(Short code) {
        this.code = code;
    }

    public String[] getTopic() {
        return topic;
    }

    public void setTopic(String[] topic) {
        this.topic = topic;
    }
        
    @Override
    public String toString() {
        return "SimpleDepartment{" + "name=" + name + ", code=" + code + ", topic=" + topic + '}';
    }        
}