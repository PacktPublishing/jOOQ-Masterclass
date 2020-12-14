package com.classicmodels.repository;

import java.time.LocalDate;
import static jooq.generated.Tables.TOP_THREE_SALES_PER_EMPLOYEE;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.lateral;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.rownum;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
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
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, field(name("avg_sale")))
                        .from(EMPLOYEE, lateral(select(
                                avg(SALE.SALE_).as(name("avg_sale"))).from(SALE)
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

        // Oracle will name the column of such an unnested array as "COLUMN_VALUE"
        System.out.println("EXAMPLE 5\n"
                + ctx.select()
                        .from(DEPARTMENT, lateral(select(field("COLUMN_VALUE"))
                                .from(table(DEPARTMENT.TOPIC))
                                .where(field("COLUMN_VALUE")
                                        .in("commerce", "business"))
                        ))
                        .fetch()
        );
    }

    // EXAMPLE 6
    public void lateralDepartmentUnnestOrdinality() {

        System.out.println("EXAMPLE 6\n"
                + ctx.select()
                        .from(DEPARTMENT, lateral(select(field("COLUMN_VALUE"), field(rownum()).as("ordinality"))
                                .from(table(DEPARTMENT.TOPIC))
                                .where(field("COLUMN_VALUE").in("commerce", "business"))
                        ))
                        .fetch()
        );
    }

    // EXAMPLE 7
    public void findTop3SalesPerEmployee() {

        System.out.println("EXAMPLE 7\n"
                + ctx.select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.FIRST_NAME,
                        EMPLOYEE.LAST_NAME, field(name("sales")))
                        .from(EMPLOYEE, lateral(select(SALE.SALE_.as(name("sales"))).from(SALE)
                                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                                .orderBy(SALE.SALE_.desc())
                                .limit(3))
                        )
                        .orderBy(EMPLOYEE.EMPLOYEE_NUMBER)
                        .fetch()
        );
    }

    // EXAMPLE 8
    public void findTop3OrderedProductsIn2003() {

        System.out.println("EXAMPLE 8\n"
                + ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, field(name("od")), field(name("qo")))
                        .from(PRODUCT, lateral(select(ORDER.ORDER_DATE.as(name("od")), ORDERDETAIL.QUANTITY_ORDERED.as(name("qo")))
                                .from(ORDER)
                                .innerJoin(ORDERDETAIL)
                                .on(ORDER.ORDER_ID.eq(ORDERDETAIL.ORDER_ID)
                                        .and(ORDER.ORDER_DATE.between(LocalDate.of(2003, 1, 1), LocalDate.of(2003, 12, 31))))
                                .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID))
                                .orderBy(ORDERDETAIL.QUANTITY_ORDERED.desc())
                                .limit(3))
                        )
                        .orderBy(PRODUCT.PRODUCT_ID)
                        .fetch()
        );
    }
    
    // EXAMPLE 9
    public void findTop3SalesPerEmployeeViaTableValuedFunction() {       
        
        System.out.println("EXAMPLE 9\n"
                + ctx.select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.FIRST_NAME,
                        EMPLOYEE.LAST_NAME, field(name("SALES")))
                        .from(EMPLOYEE, lateral(select().from(
                                TOP_THREE_SALES_PER_EMPLOYEE
                                        .call(EMPLOYEE.EMPLOYEE_NUMBER))))
                        .orderBy(EMPLOYEE.EMPLOYEE_NUMBER)
                        .fetch()
        );
    }

    // EXAMPLE 10 (CROSS APPLY)
    public void crossApplyOfficeHasDepartments() {

        System.out.println("EXAMPLE 10\n"
                + ctx.select()
                        .from(OFFICE.crossApply(select().from(DEPARTMENT)
                                .where(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE))))
                        .fetch()
        );
    }
    
    // EXAMPLE 11
    public void findTop3SalesPerEmployeeViaTableValuedFunctionAndCrossApply() {

        System.out.println("EXAMPLE 11\n"
                + ctx.select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.FIRST_NAME,
                        EMPLOYEE.LAST_NAME, field("sales"))
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
                                .where(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE)))
                        .fetch()
        );
    }
}