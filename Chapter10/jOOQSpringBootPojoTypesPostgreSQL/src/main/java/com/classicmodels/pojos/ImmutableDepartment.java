package com.classicmodels.pojos;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class ImmutableDepartment implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private final String name;
    private final Short code;
    private final String[] topic;
 
    @ConstructorProperties({ "name", "code", "topic" })
    public ImmutableDepartment(String name, Short code, String[] topic) {
        this.name = name;
        this.code = code;
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "ImmutableDepartment{" + "name=" + name + ", code=" + code + ", topic=" + topic + '}';
    }        
}