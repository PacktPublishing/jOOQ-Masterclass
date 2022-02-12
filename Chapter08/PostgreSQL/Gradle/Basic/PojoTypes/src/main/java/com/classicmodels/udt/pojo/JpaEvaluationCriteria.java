package com.classicmodels.udt.pojo;

import jakarta.persistence.Column;
import java.io.Serializable;

public final class JpaEvaluationCriteria implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Column(name = "communication_ability") 
    public Integer ca;
    
    @Column(name = "ethics") 
    public Integer et;
    
    @Column(name = "performance") 
    public Integer pr;
    
    @Column(name = "employee_input") 
    public Integer ei;
    
    @Override
    public String toString() {
        return "JpaEvaluationCriteria{" + "communicationAbility=" + ca
                + ", ethics=" + et + ", performance=" + pr
                + ", employeeInput=" + ei + '}';
    }
}
