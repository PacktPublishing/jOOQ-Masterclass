package com.classicmodels.repository;

import java.time.LocalDate;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import static jooq.generated.tables.TopThreeSalesPerEmployee.TOP_THREE_SALES_PER_EMPLOYEE;
import org.jooq.DSLContext;
import org.jooq.Table;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.lateral;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.rownum;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.trueCondition;
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
    public void lateralOfficeHasDepartments() {

        System.out.println("EXAMPLE 1\n"
                + ctx.select()
                        .from(OFFICE, lateral(select().from(DEPARTMENT)
                                .where(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE))).as("T"))
                        .fetch()
        );

        // the above query is equivalent to the following queries
        /*
        System.out.println("EXAMPLE 1\n"
                + ctx.select()
                        .from(OFFICE)
                        .crossJoin(lateral(select().from(DEPARTMENT)
                                .where(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE))).as("T"))
                        .fetch()
        );
        
        System.out.println("EXAMPLE 2\n"
                + ctx.select()
                        .from(OFFICE)
                        .innerJoin(lateral(select().from(DEPARTMENT)
                                .where(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE))).as("T"))
                        .on(trueCondition()) // Dummy predicate for INNER JOIN as val(1).eq(val(1))
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
                                .where(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE))).as("T")
                        ).on(trueCondition()) // Dummy predicate for LEFT JOIN as val(1).eq(val(1))
                        .fetch()
        );
    }

    // EXAMPLE 3
    public void lateralEmployeeAvgSales() {

        System.out.println("EXAMPLE 3\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, field(name("T", "AVG_SALE")))
                        .from(EMPLOYEE, lateral(select(
                                avg(SALE.SALE_).as("AVG_SALE")).from(SALE)
                                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER)).asTable("T")))
                        .fetch()
        // or, fetch only avg: fetch("avg_sale", float.class)
        );
    }

    // EXAMPLE 4
    public void lateralOfficeCityCountryHasDepartments() {

        Table<?> t = select().from(DEPARTMENT)
                .where(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE)).asTable("T");

        System.out.println("EXAMPLE 4\n"
                + ctx.select(OFFICE.CITY, OFFICE.COUNTRY, t.asterisk())
                        .from(OFFICE, lateral(t))
                        .fetch()
        );
    }

    // EXAMPLE 5
    public void lateralDepartmentUnnest() {

        // Oracle will name the column of such an unnested array as "COLUMN_VALUE"
        System.out.println("EXAMPLE 5\n"
                + ctx.select()
                        .from(DEPARTMENT, lateral(select(field(name("COLUMN_VALUE")))
                                .from(table(DEPARTMENT.TOPIC))
                                .where(field(name("COLUMN_VALUE"))
                                        .in("commerce", "business"))))
                        .fetch()
        );
    }

    // EXAMPLE 6
    public void lateralDepartmentUnnestOrdinality() {

        System.out.println("EXAMPLE 6\n"
                + ctx.select()
                        .from(DEPARTMENT, lateral(select(field(name("COLUMN_VALUE")), field(rownum()).as("ORDINALITY"))
                                .from(table(DEPARTMENT.TOPIC))
                                .where(field(name("COLUMN_VALUE")).in("commerce", "business"))
                        ))
                        .fetch()
        );
    }

    // EXAMPLE 7
    public void findTop3SalesPerEmployee() {

        System.out.println("EXAMPLE 7\n"
                + ctx.select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.FIRST_NAME,
                        EMPLOYEE.LAST_NAME, field(name("T", "SALES")))
                        .from(EMPLOYEE, lateral(select(SALE.SALE_.as("SALES")).from(SALE)
                                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                                .orderBy(SALE.SALE_.desc())
                                .limit(3).asTable("T"))
                        )
                        .orderBy(EMPLOYEE.EMPLOYEE_NUMBER)
                        .fetch()
        );
    }

    // EXAMPLE 8
    public void findTop3OrderedProductsIn2003() {

        System.out.println("EXAMPLE 8\n"
                + ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, field(name("T", "OD")), field(name("T", "QO")))
                        .from(PRODUCT, lateral(select(ORDER.ORDER_DATE.as("OD"), ORDERDETAIL.QUANTITY_ORDERED.as("QO"))
                                .from(ORDER)
                                .innerJoin(ORDERDETAIL)
                                .on(ORDER.ORDER_ID.eq(ORDERDETAIL.ORDER_ID)
                                        .and(ORDER.ORDER_DATE.between(LocalDate.of(2003, 1, 1), LocalDate.of(2003, 12, 31))))
                                .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID))
                                .orderBy(ORDERDETAIL.QUANTITY_ORDERED.desc())
                                .limit(3).asTable("T"))
                        )
                        .orderBy(PRODUCT.PRODUCT_ID)
                        .fetch()
        );
    }

    // EXAMPLE 9
    public void findTop3SalesPerEmployeeViaTableValuedFunction() {

        System.out.println("EXAMPLE 9\n"
                + ctx.select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.FIRST_NAME,
                        EMPLOYEE.LAST_NAME, field(name("T", "SALES")))
                        .from(EMPLOYEE, lateral(select().from(
                                TOP_THREE_SALES_PER_EMPLOYEE
                                        .call(EMPLOYEE.EMPLOYEE_NUMBER)).asTable("T")))
                        .orderBy(EMPLOYEE.EMPLOYEE_NUMBER)
                        .fetch()
        );
    }

    // EXAMPLE 10 (CROSS APPLY)
    public void crossApplyOfficeHasDepartments() {

        System.out.println("EXAMPLE 10\n"
                + ctx.select()
                        .from(OFFICE)
                        .crossApply(select().from(DEPARTMENT)
                                .where(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE)).asTable("T"))
                        .fetch()
        );
    }

    // EXAMPLE 11
    public void findTop3SalesPerEmployeeViaTableValuedFunctionAndCrossApply() {

        System.out.println("EXAMPLE 11\n"
                + ctx.select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.FIRST_NAME,
                        EMPLOYEE.LAST_NAME, field(name("SALES")))
                        .from(EMPLOYEE.crossApply(TOP_THREE_SALES_PER_EMPLOYEE.call(EMPLOYEE.EMPLOYEE_NUMBER)))
                        .orderBy(EMPLOYEE.EMPLOYEE_NUMBER)
                        .fetch()
        );
    }

    // EXAMPLE 12 (OUTER APPLY)    
    public void outerApplyOfficeHasDepartments() {

        System.out.println("EXAMPLE 12\n"
                + ctx.select()
                        .from(OFFICE)
                        .outerApply(select().from(DEPARTMENT)
                                .where(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE)).asTable("t"))
                        .fetch()
        );
    }
}