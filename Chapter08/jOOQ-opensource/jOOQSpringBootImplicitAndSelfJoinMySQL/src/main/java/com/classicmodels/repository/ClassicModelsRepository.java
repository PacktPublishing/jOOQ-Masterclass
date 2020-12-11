package com.classicmodels.repository;

import jooq.generated.tables.Employee;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Productlinedetail.PRODUCTLINEDETAIL;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    /* Implicit JOIN */
    // EXAMPLE 1
    public void implicitJoinOfficeEmployeeViaWhere() {

        System.out.println("EXAMPLE 1\n"
                + ctx.select(OFFICE.OFFICE_CODE, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(OFFICE, EMPLOYEE)
                        .where(OFFICE.OFFICE_CODE.eq(EMPLOYEE.OFFICE_CODE))
                        .orderBy(OFFICE.OFFICE_CODE)
                        .fetch()
        );
    }

    // EXAMPLE 2
    public void implicitJoinOfficeEmployeeViaNavigationMethod() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select(EMPLOYEE.office().OFFICE_CODE, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE)
                        .orderBy(EMPLOYEE.office().OFFICE_CODE)
                        .fetch()
        );
    }

    // EXAMPLE 3
    public void implicitJoinPaymentCustomerViaNavigationMethod() {

        System.out.println("EXAMPLE 3 (composite key) \n"
                + ctx.select(PAYMENT.customer().CUSTOMER_NAME, sum(PAYMENT.INVOICE_AMOUNT))
                        .from(PAYMENT)
                        .groupBy(PAYMENT.customer().CUSTOMER_NAME)
                        .orderBy(PAYMENT.customer().CUSTOMER_NAME)
                        .fetch()
        );
    }

    // EXAMPLE 4
    public void implicitJoinOrderCustomerEmployeeViaNavigationMethod() {

        System.out.println("EXAMPLE 4 \n"
                + ctx.select(
                        ORDERDETAIL.order().customer().employee().OFFICE_CODE,
                        ORDERDETAIL.order().customer().CUSTOMER_NAME,
                        ORDERDETAIL.order().SHIPPED_DATE, ORDERDETAIL.order().STATUS,
                        ORDERDETAIL.QUANTITY_ORDERED, ORDERDETAIL.PRICE_EACH)
                        .from(ORDERDETAIL)
                        .orderBy(ORDERDETAIL.order().customer().CUSTOMER_NAME)
                        .fetch()
        );
    }

    // EXAMPLE 5
    public void implicitJoinProductlinedetailProdcutLineViaFK() {

        System.out.println("EXAMPLE 5.1 \n"
                + ctx.select(PRODUCTLINEDETAIL.productlinedetailIbfk_1().CREATED_ON,
                        PRODUCTLINEDETAIL.LINE_CAPACITY)
                        .from(PRODUCTLINEDETAIL)
                        .fetch()
        );

        System.out.println("EXAMPLE 5.2 \n"
                + ctx.select(PRODUCTLINEDETAIL.productlinedetailIbfk_2().CREATED_ON,
                        PRODUCTLINEDETAIL.LINE_CAPACITY)
                        .from(PRODUCTLINEDETAIL)
                        .fetch()
        );
    }

    /* Self JOIN */
    // EXAMPLE 6
    public void selfJoinEmployee() {

        Employee a = EMPLOYEE.as("a");
        Employee b = EMPLOYEE.as("b");

        System.out.println("EXAMPLE 6\n"
                + ctx.select(concat(a.FIRST_NAME, val(" "), a.LAST_NAME).as("employee"),
                        concat(b.FIRST_NAME, val(" "), b.LAST_NAME).as("reports_to"))
                        .from(a)
                        .leftJoin(b)
                        .on(b.EMPLOYEE_NUMBER.eq(a.REPORTS_TO))
                        .fetch()
        );
    }

    // EXAMPLE 7
    public void selfJoinEmployeeViaNavigationMethod() {

        System.out.println("EXAMPLE 7\n"
                + ctx.select(concat(EMPLOYEE.FIRST_NAME, val(" "), EMPLOYEE.LAST_NAME).as("employee"),
                        concat(EMPLOYEE.employee().FIRST_NAME, val(" "), EMPLOYEE.employee().LAST_NAME).as("reports_to"))
                        .from(EMPLOYEE)
                        .fetch()
        );
    }
    
    // EXAMPLE 8
    public void selfJoinComparingEmployeeViaNavigationMethod() {

        System.out.println("EXAMPLE 8\n"
                + ctx.select(concat(EMPLOYEE.FIRST_NAME, val(" "), EMPLOYEE.LAST_NAME).as("employee"),
                        concat(EMPLOYEE.employee().FIRST_NAME, val(" "), EMPLOYEE.employee().LAST_NAME).as("reports_to"))
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.JOB_TITLE.eq(EMPLOYEE.employee().JOB_TITLE))
                        .fetch()
        );
    }
}
