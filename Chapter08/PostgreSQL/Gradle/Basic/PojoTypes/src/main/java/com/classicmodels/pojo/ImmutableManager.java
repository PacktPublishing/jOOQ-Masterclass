package com.classicmodels.pojo;

import com.classicmodels.udt.pojo.ImmutableEvaluationCriteria;
import java.io.Serializable;

public final class ImmutableManager implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private final String managerName;
    private final ImmutableEvaluationCriteria iec;

    public ImmutableManager(String managerName, ImmutableEvaluationCriteria iec) {
        this.managerName = managerName;
        this.iec = iec;
    }

    public String getManagerName() {
        return managerName;
    }    

    public ImmutableEvaluationCriteria getIec() {
        return iec;
    }

    @Override
    public String toString() {
        return "ImmutableManager{" + "managerName=" + managerName + ", iec=" + iec + '}';
    }   
}