package com.classicmodels.repository;

import java.util.stream.Collectors;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.row;
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

    @Transactional
    public void queries() {

        // log only queries (SELECT/INSERT/UPDATE/DELETE) that refer to these tables (a query should refer all tables)
        ctx.data().put(EMPLOYEE.getQualifiedName(), "");
        ctx.data().put(SALE.getQualifiedName(), "");                
        
        ctx.selectFrom(PRODUCT).limit(10).fetch();
        
        ctx.selectFrom(PRODUCT).collect(Collectors.toList());
        
        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                .from(EMPLOYEE)
                .where(select(avg(SALE.SALE_)).from(SALE).lt(
                        (select(sum(SALE.SALE_)).from(SALE)
                                .where(EMPLOYEE.EMPLOYEE_NUMBER
                                        .eq(SALE.EMPLOYEE_NUMBER)))))
                .fetch();

        ctx.select(OFFICE.CITY, OFFICE.COUNTRY,
                DEPARTMENT.NAME)
                .from(OFFICE, DEPARTMENT)
                .where(row(OFFICE.CITY, OFFICE.COUNTRY).isNotNull())
                .fetch();

        ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE)
                .leftSemiJoin(CUSTOMER)
                .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(CUSTOMER.SALES_REP_EMPLOYEE_NUMBER))
                .leftSemiJoin(SALE)
                .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                .where(EMPLOYEE.SALARY.gt(7000))
                .fetch();

        ctx.insertInto(PRODUCT, PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, PRODUCT.CODE)
                .values("Classic Cars", "Super Car", 599302L)
                .execute();

        ctx.update(PRODUCT)
                .set(PRODUCT.PRODUCT_NAME, "Amazing Car")
                .where(PRODUCT.PRODUCT_NAME.eq("Super Car"))
                .execute();

        ctx.deleteFrom(PRODUCT)
                .where(PRODUCT.PRODUCT_NAME.eq("Amazing Car"))
                .execute();

        // plain SQL, batches and routines are not logged at all
    }
}
