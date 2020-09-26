package com.classicmodels.repository;

import java.util.List;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import jooq.generated.tables.pojos.Employee;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext create;

    public ClassicModelsRepository(DSLContext create) {
        this.create = create;
    }

    public Object[][] unionEmployeeAndCustomerNames() {

        Object[][] result = create.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .union(select(CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CONTACT_LAST_NAME)
                        .from(CUSTOMER))
                .fetchArrays();

        // use custom column headings
        /*
        Object[][] result = create.select(EMPLOYEE.FIRST_NAME.as("fn"), EMPLOYEE.LAST_NAME.as("ln"))
                .from(EMPLOYEE)
                .union(select(CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CONTACT_LAST_NAME)
                        .from(CUSTOMER))
                .fetchArrays();
         */
        
        return result;
    }

    public Object[][] unionEmployeeAndCustomerNamesConcatColumns() {

        Object[][] result = create.select(
                concat(EMPLOYEE.FIRST_NAME, val(" "), EMPLOYEE.LAST_NAME).as("full_name"))
                .from(EMPLOYEE)
                .union(select(
                        concat(CUSTOMER.CONTACT_FIRST_NAME, val(" "), CUSTOMER.CONTACT_LAST_NAME))
                        .from(CUSTOMER))
                .fetchArrays();

        return result;
    }

    public Object[][] unionEmployeeAndCustomerNamesDifferentiate() {

        Object[][] result = create.select(
                concat(EMPLOYEE.FIRST_NAME, val(" "), EMPLOYEE.LAST_NAME).as("full_name"),
                val("Employee").as("contactType"))
                .from(EMPLOYEE)
                .union(select(
                        concat(CUSTOMER.CONTACT_FIRST_NAME, val(" "), CUSTOMER.CONTACT_LAST_NAME),
                        val("Customer").as("contactType"))
                        .from(CUSTOMER))
                .fetchArrays();

        return result;
    }

    public Object[][] unionEmployeeAndCustomerNamesOrderBy() {

        Object[][] result = create.select(
                concat(EMPLOYEE.FIRST_NAME, val(" "), EMPLOYEE.LAST_NAME).as("full_name"))
                .from(EMPLOYEE)
                .union(select(
                        concat(CUSTOMER.CONTACT_FIRST_NAME, val(" "), CUSTOMER.CONTACT_LAST_NAME))
                        .from(CUSTOMER))
                .orderBy(field("full_name"))
                .fetchArrays();

        return result;
    }

    public List<Employee> unionEmployeeSmallestAndHighestSalary() {

        List<Employee> result = create.selectFrom(EMPLOYEE)
                .orderBy(EMPLOYEE.SALARY.asc()).limit(1)
                .union(
                        selectFrom(EMPLOYEE)
                                .orderBy(EMPLOYEE.SALARY.desc()).limit(1))
                .orderBy(1)
                .fetchInto(Employee.class);

        return result;
    }

    public Object[][] unionAllOfficeCustomerCityAndCountry() {

        Object[][] result = create.select(OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE)
                .unionAll(select(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                        .from(CUSTOMERDETAIL))
                .orderBy(OFFICE.CITY, OFFICE.COUNTRY)
                .fetchArrays();

        return result;
    }
}
