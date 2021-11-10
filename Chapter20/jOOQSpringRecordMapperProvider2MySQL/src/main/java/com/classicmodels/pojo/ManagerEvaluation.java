package com.classicmodels.pojo;

import java.io.Serializable;

public class ManagerEvaluation implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private int communicationAbility;
    private int ethics;
    private int performance;
    private int employeeInput;

    public ManagerEvaluation() {}
            
    public ManagerEvaluation(int communicationAbility, int ethics, int performance, int employeeInput) {
        this.communicationAbility = communicationAbility;
        this.ethics = ethics;
        this.performance = performance;
        this.employeeInput = employeeInput;
    }

    public int getCommunicationAbility() {
        return communicationAbility;
    }

    public void setCommunicationAbility(int communicationAbility) {
        this.communicationAbility = communicationAbility;
    }

    public int getEthics() {
        return ethics;
    }

    public void setEthics(int ethics) {
        this.ethics = ethics;
    }

    public int getPerformance() {
        return performance;
    }

    public void setPerformance(int performance) {
        this.performance = performance;
    }

    public int getEmployeeInput() {
        return employeeInput;
    }

    public void setEmployeeInput(int employeeInput) {
        this.employeeInput = employeeInput;
    }

    @Override
    public String toString() {
        return "ManagerEvaluation{" 
                + "communicationAbility=" + communicationAbility 
                + ", ethics=" + ethics 
                + ", performance=" + performance 
                + ", employeeInput=" + employeeInput + '}';
    }            
}
