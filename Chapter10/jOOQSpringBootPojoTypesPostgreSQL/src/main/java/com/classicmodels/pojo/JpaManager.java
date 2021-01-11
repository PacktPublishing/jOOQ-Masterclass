package com.classicmodels.pojo;

import com.classicmodels.udt.pojo.JpaEvaluationCriteria;
import java.io.Serializable;
import javax.persistence.Column;

public class JpaManager implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Column(name = "manager_name") 
    public String mn;
    
    @Column(name = "manager_evaluation") 
    public JpaEvaluationCriteria ec;

    @Override
    public String toString() {
        return "JpaManager{" + "managerName=" + mn + ", ec=" + ec + '}';
    }                
}
