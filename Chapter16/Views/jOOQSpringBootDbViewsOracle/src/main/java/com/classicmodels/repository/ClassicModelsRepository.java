package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // read-only views (not updatable)
    public void roViews() {

        ctx.dropViewIfExists("sales_1504_1370").execute();
        ctx.createView("sales_1504_1370")
                .as(select().from(SALE).where(SALE.EMPLOYEE_NUMBER.eq(1504L))
                        .unionAll(select().from(SALE).where(SALE.EMPLOYEE_NUMBER.eq(1370L))))
                .execute();

        System.out.println(
                ctx.select().from(name("sales_1504_1370")).fetch()
        );

        // but, this view can be expressed as an updatable one as follows        
        ctx.dropViewIfExists("sales_1504_1370_u").execute();
        ctx.createView("sales_1504_1370_u")
                .as(select().from(SALE).where(SALE.EMPLOYEE_NUMBER.in(1504L, 1370L)))
                .execute();               
        
        System.out.println(
                ctx.select().from(name("sales_1504_1370_u")).fetch()
        );
    }

    // updatable views - partially updatable
    public void updatableViews() {

        ctx.dropViewIfExists("employees_and_sales").execute();
        ctx.createView("employees_and_sales", "first_name", "last_name", "salary", "sale_id", "sale")
                .as(select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY,
                        SALE.SALE_ID, SALE.SALE_)
                        .from(EMPLOYEE)
                        .join(SALE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER)))
                .execute();

        System.out.println("Before update:\n" 
                + ctx.select().from(name("employees_and_sales")).fetch()
        );
        
        ctx.update(table(name("employees_and_sales")))
                .set(field(name("sale")), 
                        field(name("sale"),  Double.class)
                                .plus(field(name("sale")).mul(0.25)))                
                .where(field(name("sale")).gt(5000))
                .execute();
        
        System.out.println("After update:\n" 
                + ctx.select().from(name("employees_and_sales")).fetch()
        );
        
        // this will not work!!!
        // this causes "ORA-01776: cannot modify more than one base table through a join view"
        /*
        ctx.update(table(name("employees_and_sales")))
                .set(field(name("sale")),
                        field(name("sale"), Double.class)
                                .minus(field(name("sale")).mul(0.25)))
                .set(field(name("salary")),
                        field(name("salary"), Integer.class)
                                .minus(field(name("salary")).mul(0.25)))
                .where(field(name("sale")).gt(5000))
                .execute();
        */
    }
}
