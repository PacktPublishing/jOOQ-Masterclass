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

    public Map<Office, List<Employee>> fetchOfficeWithEmploeePage(int start, int end) {

        Map<Office, List<Employee>> result = ctx.select().from(select(OFFICE.OFFICE_CODE, OFFICE.CITY,
                OFFICE.COUNTRY, OFFICE.TERRITORY, OFFICE.STATE, OFFICE.ADDRESS_LINE_FIRST, OFFICE.PHONE,
                EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.JOB_TITLE, 
                EMPLOYEE.SALARY, EMPLOYEE.EXTENSION, EMPLOYEE.EMAIL,
                denseRank().over().orderBy(OFFICE.OFFICE_CODE, OFFICE.CITY).as("rank"))
                .from(OFFICE)
                .join(EMPLOYEE)
                .on(OFFICE.OFFICE_CODE.eq(EMPLOYEE.OFFICE_CODE))
                .asTable("t"))
                .where(field("t.rank").between(start, end))
                .fetchGroups(Office.class, Employee.class);

        return result;
    }

}
