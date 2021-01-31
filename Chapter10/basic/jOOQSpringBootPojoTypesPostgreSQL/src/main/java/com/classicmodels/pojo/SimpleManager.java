package com.classicmodels.pojo;

import java.io.Serializable;
import jooq.generated.udt.pojos.EvaluationCriteria;

public class SimpleManager implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String managerName;
    private EvaluationCriteria ec;

    public SimpleManager(String managerName, EvaluationCriteria ec) {
        this.managerName = managerName;
        this.ec = ec;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public EvaluationCriteria getEc() {
        return ec;
    }

    public void setEc(EvaluationCriteria ec) {
        this.ec = ec;
    }

    @Override
    public String toString() {
        return "SimpleManager{" + "managerName=" + managerName + ", ec=" + ec + '}';
    }                
}
