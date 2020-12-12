package com.classicmodels.repository;

import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.lateral;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.unnest;
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

    // EXAMPLE 1 (CROSS JOIN LATERAL)   
    public void lateralOfficeHasDepartments() {

        System.out.println("EXAMPLE 1\n"
                + ctx.select()
                        .from(OFFICE, lateral(select().from(DEPARTMENT)
                                .where(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE))))
                        .fetch()
        );

        // the above query is equivalent to the following queries
        /*
        System.out.println("EXAMPLE 1\n"
                + ctx.select()
                        .from(OFFICE)
                        .crossJoin(lateral(select().from(DEPARTMENT)
                                .where(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE))))
                        .fetch()
        );
        
        System.out.println("EXAMPLE 2\n"
                + ctx.select()
                        .from(OFFICE)
                        .innerJoin(lateral(select().from(DEPARTMENT)
                                .where(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE))
                        )).on(val(1).eq(val(1))) // Dummy predicate for INNER JOIN
                        .fetch()
        );
         */
    }

    // EXAMPLE 2 (LEFT OUTER JOIN LATERAL)    
    public void leftOuterJoinLateralOfficeHasDepartments() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select()
                        .from(OFFICE)
                        .leftOuterJoin(lateral(select().from(DEPARTMENT)
                                .where(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE))
                        )).on(val(1).eq(val(1))) // Dummy predicate for LEFT JOIN
                        .fetch()
        );
    }

    // EXAMPLE 3
    public void lateralEmployeeAvgSales() {

        System.out.println("EXAMPLE 3\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, field("avg_sale"))
                        .from(EMPLOYEE, lateral(select(
                                avg(SALE.SALE_).as("avg_sale")).from(SALE)
                                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))))
                        .fetch()
        // or, fetch only avg: fetch("avg_sale", float.class)
        );
    }

    // EXAMPLE 4
    public void lateralOfficeCityCountryHasDepartments() {

        System.out.println("EXAMPLE 4\n"
                + ctx.select(OFFICE.CITY, OFFICE.COUNTRY, table(name("departments")).asterisk())
                        .from(OFFICE, lateral(select().from(DEPARTMENT)
                                .where(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE)))
                                .as("departments"))
                        .fetch()
        );
    }

    // EXAMPLE 5
    public void lateralDepartmentUnnest() {

        // unnest an ad-hoc anonymously typed array
        System.out.println("EXAMPLE 5\n"
                + ctx.select()
                        .from(DEPARTMENT, lateral(select().from(
                                unnest(new String[]{"one", "two", "three"}))).as("t", "nr"))                        
                        .fetch()
        );
    }
}
