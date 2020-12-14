package com.classicmodels.repository;

import java.time.LocalDate;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import static org.jooq.impl.DSL.select;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.lateral;
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

    // EXAMPLE 1 (CROSS APPLY)
    public void crossApplyOfficeHasDepartments() {

        System.out.println("EXAMPLE 1\n"
                + ctx.select()
                        .from(OFFICE.crossApply(select().from(DEPARTMENT)
                                .where(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE))))
                        .fetch()
        );
    }

    // EXAMPLE 2 (OUTER APPLY)    
    public void outerApplyOfficeHasDepartments() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select()
                        .from(OFFICE)
                        .outerApply(select().from(DEPARTMENT)
                                .where(OFFICE.OFFICE_CODE.eq(DEPARTMENT.OFFICE_CODE)))
                        .fetch()
        );
    }

    // EXAMPLE 3
    public void crossApplyEmployeeAvgSales() {

        System.out.println("EXAMPLE 3\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, field("avg_sale"))
                        .from(EMPLOYEE.crossApply(select(
                                avg(SALE.SALE_).as("avg_sale")).from(SALE)
                                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))))
                        .fetch()
        // or, fetch only avg: fetch("avg_sale", float.class)
        );
    }

    // EXAMPLE 4
    public void crossApplyOfficeCityCountryHasDepartments() {
    }

    // EXAMPLE 5
    public void crossApplyDepartmentUnnest() {

        // unnest an ad-hoc anonymously typed array
        System.out.println("EXAMPLE 5\n"
                + ctx.select()
                        .from(DEPARTMENT.crossApply(select().from(
                                unnest(new String[]{"one", "two", "three"}))))
                        .fetch()
        );
    }

    // EXAMPLE 6
    public void findTop3SalesPerEmployee() {

        System.out.println("EXAMPLE 6\n"
                + ctx.select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.FIRST_NAME,
                        EMPLOYEE.LAST_NAME, field("sales"))
                        .from(EMPLOYEE.crossApply(select(SALE.SALE_.as("sales")).from(SALE)
                                .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                                .orderBy(SALE.SALE_.desc())
                                .limit(3))
                        )
                        .orderBy(EMPLOYEE.EMPLOYEE_NUMBER)
                        .fetch()
        );
    }
    
    // EXAMPLE 7
    public void findTop3OrderedProductsIn2003() {

        System.out.println("EXAMPLE 7\n"
                + ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, field("od"), field("qo"))
                        .from(PRODUCT.crossApply(select(ORDER.ORDER_DATE.as("od"), ORDERDETAIL.QUANTITY_ORDERED.as("qo"))
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
    
    
}
