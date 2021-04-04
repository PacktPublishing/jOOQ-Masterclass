package com.classicmodels.repository;

import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import static jooq.generated.tables.Productlinedetail.PRODUCTLINEDETAIL;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // EXAMPLE 1
    public void joinEmployeeSaleViaUsing() {

        System.out.println("EXAMPLE 1\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SALE.SALE_)
                        .from(EMPLOYEE)
                        .innerJoin(SALE)
                        .using(EMPLOYEE.EMPLOYEE_NUMBER)
                        .fetch()
        );
    }

    // EXAMPLE 2
    public void joinProductlineProductlinedetailViaUsing() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select(PRODUCTLINE.TEXT_DESCRIPTION, PRODUCTLINE.CREATED_ON,
                        PRODUCTLINEDETAIL.LINE_CAPACITY, PRODUCTLINEDETAIL.LINE_TYPE)
                        .from(PRODUCTLINE)
                        .innerJoin(PRODUCTLINEDETAIL)
                        .using(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.CODE)
                        .fetch()
        );
    }
    
    // EXAMPLE 3
    public void joinOfficeCustomerdetailViaUsing() {

        System.out.println("EXAMPLE 3\n"
                + ctx.selectDistinct(OFFICE.OFFICE_CODE, OFFICE.TERRITORY, CUSTOMERDETAIL.STATE)
                        .from(OFFICE)
                        .innerJoin(CUSTOMERDETAIL)
                        .using(OFFICE.CITY, OFFICE.COUNTRY)
                        .fetch()
        );
    }
}