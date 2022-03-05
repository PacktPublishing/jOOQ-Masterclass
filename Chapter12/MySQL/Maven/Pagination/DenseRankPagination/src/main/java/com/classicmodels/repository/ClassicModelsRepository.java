package com.classicmodels.repository;

import java.util.List;
import java.util.Map;
import jooq.generated.tables.pojos.Employee;
import static jooq.generated.tables.Employee.EMPLOYEE;
import jooq.generated.tables.pojos.Office;
import static jooq.generated.tables.Office.OFFICE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.denseRank;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // classical offset pagination
    public Map<Office, List<Employee>> fetchOfficeWithEmployeeOffset(int page, int size) {

        Map<Office, List<Employee>> result = ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY,
                OFFICE.COUNTRY, OFFICE.TERRITORY, OFFICE.STATE, OFFICE.ADDRESS_LINE_FIRST, OFFICE.PHONE,
                EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.JOB_TITLE,
                EMPLOYEE.SALARY, EMPLOYEE.EXTENSION, EMPLOYEE.EMAIL)
                .from(OFFICE)
                .join(EMPLOYEE)
                .on(OFFICE.OFFICE_CODE.eq(EMPLOYEE.OFFICE_CODE))
                .orderBy(OFFICE.OFFICE_CODE)
                .limit(size)
                .offset(size * page)
                .fetchGroups(Office.class, Employee.class);

        return result;
    }

    // classical keyset pagination
    public Map<Office, List<Employee>> fetchOfficeWithEmployeeSeek(String officeCode, int size) {

        Map<Office, List<Employee>> result = ctx.select(OFFICE.OFFICE_CODE, OFFICE.CITY,
                OFFICE.COUNTRY, OFFICE.TERRITORY, OFFICE.STATE, OFFICE.ADDRESS_LINE_FIRST, OFFICE.PHONE,
                EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.JOB_TITLE,
                EMPLOYEE.SALARY, EMPLOYEE.EXTENSION, EMPLOYEE.EMAIL)
                .from(OFFICE)
                .join(EMPLOYEE)
                .on(OFFICE.OFFICE_CODE.eq(EMPLOYEE.OFFICE_CODE))
                .orderBy(OFFICE.OFFICE_CODE)
                .seek(officeCode)
                .limit(size)
                .fetchGroups(Office.class, Employee.class);

        return result;
    }

    // using DENSE_RANK() to avoid result set truncation
    public Map<Office, List<Employee>> fetchOfficeWithEmployeeDR(int start, int end) {

        Map<Office, List<Employee>> result = ctx.select().from(select(OFFICE.OFFICE_CODE, OFFICE.CITY,
                OFFICE.COUNTRY, OFFICE.TERRITORY, OFFICE.STATE, OFFICE.ADDRESS_LINE_FIRST, OFFICE.PHONE,
                EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.JOB_TITLE,
                EMPLOYEE.SALARY, EMPLOYEE.EXTENSION, EMPLOYEE.EMAIL,
                denseRank().over().orderBy(OFFICE.OFFICE_CODE, OFFICE.CITY).as("rank"))
                .from(OFFICE)
                .join(EMPLOYEE)
                .on(OFFICE.OFFICE_CODE.eq(EMPLOYEE.OFFICE_CODE))
                .asTable("t"))
                .where(field(name("t", "rank")).between(start, end))
                .fetchGroups(Office.class, Employee.class);

        return result;
    }
}