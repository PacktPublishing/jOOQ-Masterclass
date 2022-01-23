package com.classicmodels.repository;

import java.time.LocalDate;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.Table;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.lateral;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.DSL.unnest;
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
                                .where(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE))).as("t"))
                        .fetch()
        );

        // the above query is equivalent in results to the following queries
        /*
        System.out.println("EXAMPLE 1\n"
                + ctx.select()
                        .from(OFFICE)
                        .crossJoin(lateral(select().from(DEPARTMENT)
                                .where(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE))).as("t"))
                        .fetch()
        );
        
        System.out.println("EXAMPLE 2\n"
                + ctx.select()
                        .from(OFFICE)
                        .innerJoin(lateral(select().from(DEPARTMENT)
                                .where(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE))).as("t")
                        ).on(trueCondition()) // Dummy predicate for INNER JOIN as val(1).eq(val(1))
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
                                .where(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE))).as("t")
                        ).on(trueCondition()) // Dummy predicate for LEFT JOIN as val(1).eq(val(1))
                        .fetch()
        );
    }

    // EXAMPLE 3
    public void lateralEmployeeAvgSales() {
        
        System.out.println("EXAMPLE 3\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, field(name("t", "avg_sale")))
                        .from(EMPLOYEE, lateral(select(
                                avg(SALE.SALE_).as("avg_sale")).from(SALE)
                                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))).asTable("t"))
                        .fetch()
        // or, fetch only avg: fetch("avg_sale", float.class)
        );
    }

    // EXAMPLE 4
    public void lateralOfficeCityCountryHasDepartments() {
        
        Table<?> t = select().from(DEPARTMENT)
                .where(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE)).asTable("t");
        
        System.out.println("EXAMPLE 4\n"
                + ctx.select(OFFICE.CITY, OFFICE.COUNTRY, t.asterisk())
                        .from(OFFICE, lateral(t))
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

    // EXAMPLE 6
    public void findTop3SalesPerEmployee() {
        
        System.out.println("EXAMPLE 6\n"
                + ctx.select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.FIRST_NAME,
                        EMPLOYEE.LAST_NAME, field(name("t", "sales")))
                        .from(EMPLOYEE, lateral(select(SALE.SALE_.as("sales")).from(SALE)
                                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                                .orderBy(SALE.SALE_.desc())
                                .limit(3)).asTable("t"))
                        .orderBy(EMPLOYEE.EMPLOYEE_NUMBER)
                        .fetch()
        );
    }

    // EXAMPLE 7
    public void findTop3OrderedProductsIn2003() {
        
        System.out.println("EXAMPLE 7\n"
                + ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, field(name("t", "od")), field(name("t", "qo")))
                        .from(PRODUCT, lateral(select(ORDER.ORDER_DATE.as("od"), ORDERDETAIL.QUANTITY_ORDERED.as("qo"))
                                .from(ORDER)
                                .innerJoin(ORDERDETAIL)
                                .on(ORDER.ORDER_ID.eq(ORDERDETAIL.ORDER_ID)
                                        .and(ORDER.ORDER_DATE.between(LocalDate.of(2003, 1, 1), LocalDate.of(2003, 12, 31))))
                                .where(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID))
                                .orderBy(ORDERDETAIL.QUANTITY_ORDERED.desc())
                                .limit(3)).asTable("t"))
                        .orderBy(PRODUCT.PRODUCT_ID)
                        .fetch()
        );
    }
}
