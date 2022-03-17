package com.classicmodels.repository;

import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.max;
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

    // Single-Table Projection and Restriction (here, hidding bank details)
    public void stprView() {

        ctx.dropViewIfExists("transactions").execute();
        ctx.createView("transactions", "customer_number", "check_number",
                "caching_date", "transfer_amount", "status")
                .as(select(BANK_TRANSACTION.CUSTOMER_NUMBER, BANK_TRANSACTION.CHECK_NUMBER,
                        BANK_TRANSACTION.CACHING_DATE, BANK_TRANSACTION.TRANSFER_AMOUNT,
                        BANK_TRANSACTION.STATUS)
                        .from(BANK_TRANSACTION))
                .execute();

        System.out.println(
                ctx.select().from(name("transactions")).fetch()
        );
    }

    // Calculated Columns (here, calculate salary+commission)
    public void ccView() {

        ctx.dropViewIfExists("payroll").execute();
        ctx.createView("payroll", "employee_number", "paycheck_amt")
                .as(select(EMPLOYEE.EMPLOYEE_NUMBER, EMPLOYEE.SALARY
                        .plus(coalesce(EMPLOYEE.COMMISSION, 0.00)))
                        .from(EMPLOYEE))
                .execute();

        System.out.println(
                ctx.select().from(name("payroll")).fetch()
        );
    }

    // Translated Columns
    public void tcView() {

        ctx.dropViewIfExists("customer_orders").execute();
        ctx.createView("customer_orders")
                .as(select(CUSTOMER.CUSTOMER_NAME, CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CONTACT_LAST_NAME,
                        ORDER.SHIPPED_DATE, ORDERDETAIL.QUANTITY_ORDERED, ORDERDETAIL.PRICE_EACH,
                        PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_LINE)
                        .from(CUSTOMER)
                        .innerJoin(ORDER)
                        .on(CUSTOMER.CUSTOMER_NUMBER.eq(ORDER.CUSTOMER_NUMBER))
                        .innerJoin(ORDERDETAIL)
                        .on(ORDER.ORDER_ID.eq(ORDERDETAIL.ORDER_ID))
                        .innerJoin(PRODUCT)
                        .on(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID)))
                .execute();

        System.out.println(
                ctx.select().from(name("customer_orders")).fetch()
        );
    }

    // Grouped VIEWs
    public void groupedView() {

        ctx.dropViewIfExists("big_sales").execute();
        ctx.createView("big_sales", "employee_number", "big_sale")
                .as(select(SALE.EMPLOYEE_NUMBER, max(SALE.SALE_))
                        .from(SALE)                        
                        .groupBy(SALE.EMPLOYEE_NUMBER))                
                .execute();

        System.out.println(
                ctx.select().from(name("big_sales")).fetch()
        );

        // "flattening out" one-to-many relationships
        ctx.dropViewIfExists("employee_sales").execute();
        ctx.createView("employee_sales", "employee_number", "sales_count")
                .as(select(SALE.EMPLOYEE_NUMBER, count())
                        .from(SALE)
                        .groupBy(SALE.EMPLOYEE_NUMBER))
                .execute();

        var result = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME,
                coalesce(field(name("sales_count")), 0).as("sales_count"))
                .from(EMPLOYEE)
                .leftOuterJoin(table(name("employee_sales")))
                .on(EMPLOYEE.EMPLOYEE_NUMBER
                        .eq(field(name("employee_sales", "employee_number"), Long.class)))
                .fetch();

        System.out.println("Result:\n" + result);
    }

    // UNION-ed VIEWs
    public void unionedView() {

        ctx.dropViewIfExists("employee_sales_u").execute();
        ctx.createView("employee_sales_u", "employee_number", "sales_count")
                .as(select(SALE.EMPLOYEE_NUMBER, count())
                        .from(SALE)
                        .groupBy(SALE.EMPLOYEE_NUMBER)
                        .union(select(EMPLOYEE.EMPLOYEE_NUMBER, inline(0))
                                .from(EMPLOYEE)
                                .whereNotExists(select().from(SALE)
                                        .where(SALE.EMPLOYEE_NUMBER.eq(EMPLOYEE.EMPLOYEE_NUMBER))))
                ).execute();

        var result = ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, field(name("sales_count")))
                .from(EMPLOYEE)
                .innerJoin(table(name("employee_sales_u")))
                .on(EMPLOYEE.EMPLOYEE_NUMBER
                        .eq(field(name("employee_sales_u", "employee_number"), Long.class)))
                .fetch();

        System.out.println("Result:\n" + result);
    }

    // Nested VIEWs
    public void nestedView() {

        ctx.dropViewIfExists("customer_orders_2").execute();
        ctx.dropViewIfExists("customer_orders_1").execute();
        
        ctx.createView("customer_orders_1", "customer_number", "orders_count")
                .as(select(ORDER.CUSTOMER_NUMBER, count())
                        .from(ORDER)
                        .groupBy(ORDER.CUSTOMER_NUMBER))
                .execute();
        
        ctx.createView("customer_orders_2", "first_name", "last_name", "orders_count")                 
                .as(select(CUSTOMER.CONTACT_FIRST_NAME, CUSTOMER.CONTACT_LAST_NAME,
                        coalesce(field(name("orders_count")), 0))
                        .from(CUSTOMER)
                        .leftOuterJoin(table(name("customer_orders_1")))
                        .on(CUSTOMER.CUSTOMER_NUMBER
                                .eq(field(name("customer_orders_1", "customer_number"), Long.class))))                 
                .execute();
        
        System.out.println(
                ctx.select().from(name("customer_orders_2")).fetch()
        );
    }    
}
