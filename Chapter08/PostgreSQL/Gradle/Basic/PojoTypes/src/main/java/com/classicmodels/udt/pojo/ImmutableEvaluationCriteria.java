package com.classicmodels.udt.pojo;

import java.io.Serializable;

public final class ImmutableEvaluationCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Integer communicationAbility;
    private final Integer ethics;
    private final Integer performance;
    private final Integer employeeInput;

    public ImmutableEvaluationCriteria(Integer communicationAbility, Integer ethics,
            Integer performance, Integer employeeInput) {
        this.communicationAbility = communicationAbility;
        this.ethics = ethics;
        this.performance = performance;
        this.employeeInput = employeeInput;
    }

    public Integer getCommunicationAbility() {
        return communicationAbility;
    }

    public Integer getEthics() {
        return ethics;
    }

    public Integer getPerformance() {
        return performance;
    }

    public Integer getEmployeeInput() {
        return employeeInput;
    }

    @Override
    public String toString() {
        return "ImmutableEvaluationCriteria{" + "communicationAbility=" + communicationAbility
                + ", ethics=" + ethics + ", performance=" + performance
                + ", employeeInput=" + employeeInput + '}';
    }
}
