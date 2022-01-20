package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectDistinct;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.values;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    /*
    Partitioned outer joins is specific to Oracle and it allow us to do the same 
    "densifying" of data using a convenient syntax and an efficient execution plan.
    */
    
    // Fill Gaps in Sparse Data
    public void partitionedOuterJoinExamples() {

        System.out.println("EXAMPLE 1:\n"
                + ctx.select(ORDERDETAIL.ORDER_LINE_NUMBER, PRODUCT.PRODUCT_NAME,
                        sum(nvl(ORDERDETAIL.QUANTITY_ORDERED, 0)))
                        .from(PRODUCT)
                        .leftOuterJoin(ORDERDETAIL)
                        .partitionBy(ORDERDETAIL.ORDER_LINE_NUMBER)
                        .on(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))
                        .groupBy(ORDERDETAIL.ORDER_LINE_NUMBER, PRODUCT.PRODUCT_NAME)
                        .orderBy(1, 2)
                        .fetch()
        );

        System.out.println("EXAMPLE 2.1:\n"
                + ctx.select(SALE.FISCAL_YEAR, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME,
                        sum(nvl(SALE.SALE_, 0.0d)).as("SALES"))
                        .from(EMPLOYEE)
                        .leftOuterJoin(SALE).partitionBy(SALE.FISCAL_YEAR)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                        .where(EMPLOYEE.JOB_TITLE.eq("Sales Rep"))
                        .groupBy(SALE.FISCAL_YEAR, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .orderBy(1, 2)
                        .fetch()
        );

        // Example 2 alternative without partitioned (you can easily adapt for examples 1 and 3)
        System.out.println("EXAMPLE 2.2:\n"
                + ctx.select(field(name("T", "FY")), EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME,
                        sum(nvl(SALE.SALE_, 0.0d)).as("SALES"))
                        .from(EMPLOYEE)
                        .crossJoin(selectDistinct(SALE.FISCAL_YEAR.as("FY")).from(SALE).asTable("T"))
                        .leftOuterJoin(SALE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER)
                                .and(field(name("T", "FY")).eq(SALE.FISCAL_YEAR)))
                        .where(EMPLOYEE.JOB_TITLE.eq("Sales Rep"))
                        .groupBy(field(name("T", "FY")), EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .orderBy(1, 2)
                        .fetch()
        );
    
        System.out.println("EXAMPLE 3:\n"
                + ctx.select(field(name("TB", "YEAR")), EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME,
                        sum(nvl(SALE.SALE_, 0.0d)))
                        .from(select().from(values(row(2000), row(2001),
                                row(2002), row(2003), row(2004), row(2005), row(2006), row(2007))
                                .as("T", "YEAR")).asTable("TB"))
                        .leftOuterJoin(SALE).partitionBy(SALE.EMPLOYEE_NUMBER)
                        .on(field("TB.YEAR").eq(SALE.FISCAL_YEAR))
                        .leftOuterJoin(EMPLOYEE)
                        .on(EMPLOYEE.EMPLOYEE_NUMBER.eq(SALE.EMPLOYEE_NUMBER))
                        .where(EMPLOYEE.JOB_TITLE.eq("Sales Rep"))
                        .groupBy(field(name("TB", "YEAR")), EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .orderBy(1, 2)
                        .fetch()
        );
    }
}
