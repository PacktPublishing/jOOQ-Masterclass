package com.classicmodels.repository;

import jooq.generated.tables.Employee;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.OfficeHasManager.OFFICE_HAS_MANAGER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Payment.PAYMENT;
import jooq.generated.tables.Sale;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sum;
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
    // EXAMPLES 1.1 and 1.2 - non-ANSI JOIN syntax - better avoid it
    public void implicitJoinOfficeEmployeeViaWhere() {

        System.out.println("EXAMPLE 1.1\n"
                + ctx.select(OFFICE.OFFICE_CODE, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(OFFICE, EMPLOYEE)
                        .where(OFFICE.OFFICE_CODE.eq(EMPLOYEE.OFFICE_CODE))
                        .orderBy(OFFICE.OFFICE_CODE)
                        .fetch()
        );

        System.out.println("EXAMPLE 1.2 (using Oracle (+))\n"
                + ctx.select(OFFICE.OFFICE_CODE, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(OFFICE, EMPLOYEE)
                        .where(OFFICE.OFFICE_CODE.plus().eq(EMPLOYEE.OFFICE_CODE))
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
    // If we think to an m:n relationship from the relationship table then we see two to-one relationships   
    public void implicitJoinManagerOfficeFromRelationshipTable() {

        System.out.println("EXAMPLE 5 \n"
                + ctx.select(OFFICE_HAS_MANAGER.manager().fields())
                        .from(OFFICE_HAS_MANAGER)
                        .where(OFFICE_HAS_MANAGER.office().OFFICE_CODE.eq("6"))
                        .fetch()
        );
    }
    
    // EXAMPLE 6
    // outer and correlated subquery share implicit join path
    public void outerCorrelatedSaleSameImplicitJoinPath() {  
        
        var s = SALE.as("S");
        System.out.println("EXAMPLE 6 \n"
                + ctx.selectDistinct(s.employee().FIRST_NAME)
                        .from(s)
                        .where(exists(
                                select(s.employee().FIRST_NAME)
                                        .from(s)))
                        .fetch()
        );
    }

    /* Self JOIN */
    // EXAMPLE 7
    public void selfJoinEmployee() {

        Employee a = EMPLOYEE.as("a");
        Employee b = EMPLOYEE.as("b");

        System.out.println("EXAMPLE 7\n"
                + ctx.select(concat(a.FIRST_NAME, inline(" "), a.LAST_NAME).as("employee"),
                        concat(b.FIRST_NAME, inline(" "), b.LAST_NAME).as("reports_to"))
                        .from(a)
                        .leftJoin(b)
                        .on(b.EMPLOYEE_NUMBER.eq(a.REPORTS_TO))
                        .fetch()
        );
    }

    // EXAMPLE 8
    public void selfJoinEmployeeViaNavigationMethod() {

        System.out.println("EXAMPLE 8\n"
                + ctx.select(concat(EMPLOYEE.FIRST_NAME, inline(" "), EMPLOYEE.LAST_NAME).as("employee"),
                        concat(EMPLOYEE.employee().FIRST_NAME, inline(" "), EMPLOYEE.employee().LAST_NAME).as("reports_to"))
                        .from(EMPLOYEE)
                        .fetch()
        );
    }

    // EXAMPLE 9
    public void selfJoinComparingEmployeeViaNavigationMethod() {

        System.out.println("EXAMPLE 9\n"
                + ctx.select(concat(EMPLOYEE.FIRST_NAME, inline(" "), EMPLOYEE.LAST_NAME).as("employee"),
                        concat(EMPLOYEE.employee().FIRST_NAME, inline(" "), EMPLOYEE.employee().LAST_NAME).as("reports_to"))
                        .from(EMPLOYEE)
                        .where(EMPLOYEE.JOB_TITLE.eq(EMPLOYEE.employee().JOB_TITLE))
                        .fetch()
        );
    }

    // EXAMPLE 10
    public void selfJoinThreeTimes() {

        Sale s1 = SALE.as("s1");
        Sale s2 = SALE.as("s2");

        System.out.println("EXAMPLE 10\n"
                + ctx.selectDistinct(SALE.EMPLOYEE_NUMBER, SALE.FISCAL_YEAR.as("2003"),
                        s1.FISCAL_YEAR.as("2004"), s2.FISCAL_YEAR.as("2005"))
                        .from(SALE)
                        .innerJoin(s1)
                        .on(SALE.EMPLOYEE_NUMBER.eq(s1.EMPLOYEE_NUMBER)
                                .and(SALE.FISCAL_YEAR.eq(2003)
                                        .and(s1.FISCAL_YEAR.eq(SALE.FISCAL_YEAR.plus(1)))))
                        .innerJoin(s2)
                        .on(s2.EMPLOYEE_NUMBER.eq(s1.EMPLOYEE_NUMBER)
                                .and(s1.FISCAL_YEAR.eq(SALE.FISCAL_YEAR.plus(1))
                                        .and(s2.FISCAL_YEAR.eq(SALE.FISCAL_YEAR.plus(2)))))
                        .orderBy(SALE.EMPLOYEE_NUMBER)
                        .fetch()
        );
    }
}
