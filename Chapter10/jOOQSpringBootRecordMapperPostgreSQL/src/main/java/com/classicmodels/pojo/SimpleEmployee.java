package com.classicmodels.pojo;

import java.io.Serializable;

public class SimpleEmployee implements Serializable {

    private static final long serialVersionUID = 1L;

    private String ln;
    private String fn;
    private String boss;

    public String getLn() {
        return ln;
    }

    public void setLn(String ln) {
        this.ln = ln;
    }

    public String getFn() {
        return fn;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public String getBoss() {
        return boss;
    }

    public void setBoss(String boss) {
        this.boss = boss;
    }

    @Override
    public String toString() {
        return "SimpleEmployee{" + "ln=" + ln + ", fn=" + fn + ", boss=" + boss + '}';
    }       
}