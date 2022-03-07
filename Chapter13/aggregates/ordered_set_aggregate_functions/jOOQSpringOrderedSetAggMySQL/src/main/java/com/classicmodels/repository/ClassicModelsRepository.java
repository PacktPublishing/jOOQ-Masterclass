package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.all;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.listAgg;
import static org.jooq.impl.DSL.max;
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

    // LIST_AGG()
    public void listAggEmployee() {

        ctx.select(
                listAgg(EMPLOYEE.FIRST_NAME).withinGroupOrderBy(EMPLOYEE.SALARY).as("list_agg"))
                .from(EMPLOYEE)
                .fetch();
        
        ctx.select(
                listAgg(EMPLOYEE.FIRST_NAME, ";").withinGroupOrderBy(EMPLOYEE.SALARY).as("list_agg"))
                .from(EMPLOYEE)
                .fetch();
        
        String result = ctx.select(
                listAgg(EMPLOYEE.FIRST_NAME, ",").withinGroupOrderBy(EMPLOYEE.SALARY).as("list_agg"))
                .from(EMPLOYEE)
                .fetchOneInto(String.class);
        System.out.println("Result: " + result);        

        ctx.select(
                listAgg(EMPLOYEE.FIRST_NAME).withinGroupOrderBy(EMPLOYEE.SALARY)
                        .filterWhere(EMPLOYEE.SALARY.gt(80000))
                        .as("list_agg"))
                .from(EMPLOYEE)
                .fetch();

        ctx.select(
                listAgg(concat(EMPLOYEE.FIRST_NAME, inline(" "), EMPLOYEE.LAST_NAME), ",")
                        .withinGroupOrderBy(EMPLOYEE.SALARY.desc(), EMPLOYEE.FIRST_NAME.desc()).as("employees"))
                .from(EMPLOYEE)
                .fetch();

        ctx.select(EMPLOYEE.JOB_TITLE, listAgg(EMPLOYEE.FIRST_NAME, ",")
                .withinGroupOrderBy(EMPLOYEE.FIRST_NAME).as("employees"))
                .from(EMPLOYEE)
                .groupBy(EMPLOYEE.JOB_TITLE)
                .orderBy(EMPLOYEE.JOB_TITLE)
                .fetch();

        ctx.select(ORDERDETAIL.ORDER_ID, listAgg(PRODUCT.PRODUCT_NAME, ",")
                .withinGroupOrderBy(PRODUCT.PRODUCT_NAME).as("products"))
                .from(ORDERDETAIL)
                .join(PRODUCT)
                .on(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))
                .groupBy(ORDERDETAIL.ORDER_ID)
                .fetch();
    }

    // MODE()
    public void modeOrderedSetAggregateFunctionEmulation() {

        // emulation of mode that returns all results (1)
        ctx.select(SALE.FISCAL_MONTH)
                .from(SALE)
                .groupBy(SALE.FISCAL_MONTH)
                .having(count().ge(all(select(count())
                        .from(SALE).groupBy(SALE.FISCAL_MONTH))))
                .fetch();
        
        // emulation of mode that returns all results (2)
        ctx.select(field("FISCAL_MONTH")).from(
                select(SALE.FISCAL_MONTH, count(SALE.FISCAL_MONTH).as("CNT1"))
                        .from(SALE)
                        .groupBy(SALE.FISCAL_MONTH))
                .where(field("CNT1").eq(
                        select(max(field("CNT2")))
                                .from(select(count(SALE.FISCAL_MONTH).as("CNT2"))
                                        .from(SALE).groupBy(SALE.FISCAL_MONTH))))
                .fetch();
                
        // emulation of mode using a percentage of the total number of occurrences
        ctx.select(avg(ORDERDETAIL.QUANTITY_ORDERED))
                .from(ORDERDETAIL)
                .groupBy(ORDERDETAIL.QUANTITY_ORDERED)
                .having(count().ge(all(select(count().mul(0.75))
                        .from(ORDERDETAIL).groupBy(ORDERDETAIL.QUANTITY_ORDERED))))
                .fetch();

        ctx.select(avg(ORDERDETAIL.QUANTITY_ORDERED))
                .from(ORDERDETAIL)
                .groupBy(ORDERDETAIL.QUANTITY_ORDERED)
                .having(count().ge(all(select(count().mul(0.95))
                        .from(ORDERDETAIL).groupBy(ORDERDETAIL.QUANTITY_ORDERED))))
                .fetch();
    }
}
