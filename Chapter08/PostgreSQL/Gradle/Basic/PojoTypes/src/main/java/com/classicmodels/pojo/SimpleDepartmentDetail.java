package com.classicmodels.pojo;

import java.io.Serializable;
import jooq.generated.embeddables.pojos.DepartmentDetail;

public class SimpleDepartmentDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private String departmentId;
    private DepartmentDetail departmentDetail;

    public SimpleDepartmentDetail(String departmentId, DepartmentDetail departmentDetail) {
        this.departmentId = departmentId;
        this.departmentDetail = departmentDetail;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public DepartmentDetail getDepartmentDetail() {
        return departmentDetail;
    }

    public void setDepartmentDetail(DepartmentDetail departmentDetail) {
        this.departmentDetail = departmentDetail;
    }

    @Override
    public String toString() {
        return "SimpleDepartmentDetail{" + "departmentId=" + departmentId 
                + ", departmentDetail=" + departmentDetail + '}';
    }    
}