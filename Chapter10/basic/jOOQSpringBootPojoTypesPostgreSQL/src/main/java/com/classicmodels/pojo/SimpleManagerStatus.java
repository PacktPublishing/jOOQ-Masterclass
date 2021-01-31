package com.classicmodels.pojo;

import java.io.Serializable;
import jooq.generated.embeddables.pojos.ManagerStatus;

public class SimpleManagerStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long managerId;
    private ManagerStatus ms;

    public SimpleManagerStatus(Long managerId, ManagerStatus ms) {
        this.managerId = managerId;
        this.ms = ms;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }   

    public ManagerStatus getMs() {
        return ms;
    }

    public void setMs(ManagerStatus ms) {
        this.ms = ms;
    }

    @Override
    public String toString() {
        return "SimpleManagerStatus{" + "managerId=" + managerId + ", ms=" + ms + '}';
    }               
}
