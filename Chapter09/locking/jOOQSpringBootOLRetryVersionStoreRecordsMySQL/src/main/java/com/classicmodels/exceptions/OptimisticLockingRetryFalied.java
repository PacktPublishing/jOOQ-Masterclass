package com.classicmodels.exceptions;

public class OptimisticLockingRetryFalied extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OptimisticLockingRetryFalied(String message) {
        super(message);
    }
}
