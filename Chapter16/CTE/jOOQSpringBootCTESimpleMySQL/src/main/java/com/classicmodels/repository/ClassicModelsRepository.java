package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.CommonTableExpression;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record2;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.lateral;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.with;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void cte1() {

        CommonTableExpression<Record2<Long, BigDecimal>> t = name("cte_sales")
                .fields("employee_nr", "sales")
                .as(select(SALE.EMPLOYEE_NUMBER, sum(SALE.SALE_))
                        .from(SALE)
                        .groupBy(SALE.EMPLOYEE_NUMBER));
        
        ctx.with(t)
                .select() // or, .select(t.field("employee_nr"), t.field("sales"))
                .from(t)
                .where(t.field("sales", Double.class)
                        .eq(select(max(t.field("sales", Double.class))).from(t)))
                .fetch();
    }

    public void cte2() {

        Field<Long> e = SALE.EMPLOYEE_NUMBER;
        Field<BigDecimal> s = sum(SALE.SALE_);

        CommonTableExpression<Record2<Long, BigDecimal>> t = name("cte_sales")
                .fields(e.getName(), s.getName())
                .as(select(e, s)
                        .from(SALE)
                        .groupBy(e));

        ctx.with(t)
                .select() // or, .select(t.field(e.getName()), t.field(s.getName()))
                .from(t)
                .where(t.field(s.getName(), s.getType())
                        .eq(select(max(t.field(s.getName(), s.getType()))).from(t)))
                .fetch();
    }

    public void cte3() {

        Field<Long> e = SALE.EMPLOYEE_NUMBER;
        Field<BigDecimal> s = sum(SALE.SALE_);

        var t = name("cte_sales")
                .fields(e.getName(), s.getName())
                .as(select(e, s)
                        .from(SALE)
                        .groupBy(e));       

        ctx.with(t)
                .select() // or, .select(t.field(e), t.field(s))
                .from(t)
                .where(t.field(s)
                        .eq(select(max(t.field(s))).from(t)))
                .fetch();
    }

    public void cte4() {

        ctx.with("cte_sales", "employee_nr", "sales")
                .as(select(SALE.EMPLOYEE_NUMBER, sum(SALE.SALE_))
                        .from(SALE)
                        .groupBy(SALE.EMPLOYEE_NUMBER))
                .select() // field(name("employee_nr")), field(name("sales"))
                .from(name("cte_sales"))
                .where(field(name("sales"))
                        .eq(select(max(field(name("sales")))).from(name("cte_sales"))))
                .fetch();
        
        ctx.with("cte_sales")
                .as(select(SALE.EMPLOYEE_NUMBER.as("employee_nr"), sum(SALE.SALE_).as("sales"))
                        .from(SALE)
                        .groupBy(SALE.EMPLOYEE_NUMBER))
                .select() // field("employee_nr"), field("sales")
                .from(name("cte_sales"))
                .where(field(name("sales"))
                        .eq(select(max(field(name("sales")))).from(name("cte_sales"))))
                .fetch();
    }

    // two CTEs
    public void cte5() {

        var cte1 = name("cte_productline_counts")
                .fields("product_line", "code", "description", "product_count")
                .as(select(PRODUCT.PRODUCT_LINE, PRODUCT.CODE, PRODUCTLINE.TEXT_DESCRIPTION,
                        count(PRODUCT.PRODUCT_ID))
                        .from(PRODUCTLINE)
                        .join(PRODUCT)
                        .onKey()
                        .groupBy(PRODUCT.PRODUCT_LINE, PRODUCT.CODE, PRODUCTLINE.TEXT_DESCRIPTION));

        var cte2 = name("cte_productline_sales")
                .fields("product_line", "sales")
                .as(select(PRODUCT.PRODUCT_LINE, sum(ORDERDETAIL.QUANTITY_ORDERED.mul(ORDERDETAIL.PRICE_EACH)))
                        .from(PRODUCT)
                        .join(ORDERDETAIL)
                        .onKey()
                        .groupBy(PRODUCT.PRODUCT_LINE));

        ctx.with(cte1, cte2)
                .select(cte1.field("product_line"), cte1.field("code"),
                        cte1.field("product_count"), cte1.field("description"),
                        cte2.field("sales"))
                .from(cte1)
                .join(cte2)
                .on(cte1.field("product_line", String.class)
                        .eq(cte2.field("product_line", String.class)))
                .orderBy(cte1.field("product_line"))
                .fetch();
    }

    public void cte6() {

        Field<String> pl = PRODUCT.PRODUCT_LINE;

        var cte1 = name("cte_productline_counts")
                .fields(pl.getName(), "code", "description", "product_count")
                .as(select(pl, PRODUCT.CODE, PRODUCTLINE.TEXT_DESCRIPTION,
                        count(PRODUCT.PRODUCT_ID))
                        .from(PRODUCTLINE)
                        .join(PRODUCT)
                        .onKey()
                        .groupBy(pl, PRODUCT.CODE, PRODUCTLINE.TEXT_DESCRIPTION));

        var cte2 = name("cte_productline_sales")
                .fields(pl.getName(), "sales")
                .as(select(pl, sum(ORDERDETAIL.QUANTITY_ORDERED.mul(ORDERDETAIL.PRICE_EACH)))
                        .from(PRODUCT)
                        .join(ORDERDETAIL)
                        .onKey()
                        .groupBy(pl));

        ctx.with(cte1, cte2)
                .select(cte1.field(pl), cte1.field("code"),
                        cte1.field("product_count"), cte1.field("description"),
                        cte2.field("sales"))
                .from(cte1)
                .join(cte2)
                .on(cte1.field(pl)
                        .eq(cte2.field(pl)))
                .orderBy(cte1.field(pl))
                .fetch();
    }

    public void cte7() {

        ctx.with("cte_productline_counts")
                .as(select(PRODUCT.PRODUCT_LINE, PRODUCT.CODE,
                        count(PRODUCT.PRODUCT_ID).as("product_count"),
                        PRODUCTLINE.TEXT_DESCRIPTION.as("description"))
                        .from(PRODUCTLINE)
                        .join(PRODUCT)
                        .onKey()
                        .groupBy(PRODUCT.PRODUCT_LINE, PRODUCT.CODE, PRODUCTLINE.TEXT_DESCRIPTION))
                .with("cte_productline_sales")
                .as(select(PRODUCT.PRODUCT_LINE,
                        sum(ORDERDETAIL.QUANTITY_ORDERED.mul(ORDERDETAIL.PRICE_EACH)).as("sales"))
                        .from(PRODUCT)
                        .join(ORDERDETAIL)
                        .onKey()
                        .groupBy(PRODUCT.PRODUCT_LINE))
                .select(field(name("cte_productline_counts", "product_line")), field(name("code")),
                        field(name("product_count")), field(name("description")),
                        field(name("sales")))
                .from(name("cte_productline_counts"))
                .join(name("cte_productline_sales"))
                .on(field(name("cte_productline_counts", "product_line"))
                        .eq(field(name("cte_productline_sales", "product_line"))))
                .orderBy(field(name("cte_productline_counts", "product_line")))
                .fetch();
    }

    // nested CTEs
    public void cte8() {

        var cte1 = name("avg_per_office")
                .fields("office", "avg_salary_per_office")
                .as(select(EMPLOYEE.OFFICE_CODE, avg(EMPLOYEE.SALARY))
                        .from(EMPLOYEE)
                        .groupBy(EMPLOYEE.OFFICE_CODE));

        var cte2 = name("min_salary_office")
                .fields("min_avg_salary_per_office")
                .as(select(min(cte1.field("avg_salary_per_office"))).from(cte1));

        var cte3 = name("max_salary_office")
                .fields("max_avg_salary_per_office")
                .as(select(max(cte1.field("avg_salary_per_office"))).from(cte1));

        ctx.with(cte1, cte2, cte3)
                .select()
                .from(cte1)
                .crossJoin(cte2)
                .crossJoin(cte3)
                .fetch();
    }

    public void cte9() {

        ctx.with("avg_per_office")
                .as(select(EMPLOYEE.OFFICE_CODE.as("office"),
                        avg(EMPLOYEE.SALARY).as("avg_salary_per_office"))
                        .from(EMPLOYEE)
                        .groupBy(EMPLOYEE.OFFICE_CODE))
                .with("min_salary_office")
                .as(select(min(field(name("avg_salary_per_office")))
                        .as("min_avg_salary_per_office")).from(name("avg_per_office")))
                .with("max_salary_office")
                .as(select(max(field(name("avg_salary_per_office")))
                        .as("max_avg_salary_per_office")).from(name("avg_per_office")))
                .select()
                .from(name("avg_per_office"))
                .crossJoin(name("min_salary_office"))
                .crossJoin(name("max_salary_office"))
                .fetch();
    }

    // nested CTE as FROM WITH
    public void cte10() {

        // 1. compute min salary per office
        // 2. sum salaries per min salary
        // 3. avg sum salaries        
        ctx.with("t2")
                .as(select(avg(field("sum_min_sal", Float.class)).as("avg_sum_min_sal")).from(
                        with("t1")
                                .as(select(min(EMPLOYEE.SALARY).as("min_sal"))
                                .from(EMPLOYEE)
                                .groupBy(EMPLOYEE.OFFICE_CODE)).select(
                                sum(field("min_sal", Float.class)).as("sum_min_sal")).from(name("t1"))
                                .groupBy(field("min_sal"))))
                .select()
                .from(name("t2"))
                .fetch();
    }

    // materialized CTE
    public void cte11() {

        ctx.with("cte")
                // .asMaterialized(...) - has no effect
                // .asNotMaterialized(...) - has no effect
                .asNotMaterialized(select(ORDER.CUSTOMER_NUMBER, ORDERDETAIL.ORDER_LINE_NUMBER,
                        sum(ORDERDETAIL.PRICE_EACH).as("sum_price"),
                        sum(ORDERDETAIL.QUANTITY_ORDERED).as("sum_quantity"))
                        .from(ORDER)
                        .join(ORDERDETAIL)
                        .on(ORDER.ORDER_ID.eq(ORDERDETAIL.ORDER_ID))
                        .groupBy(ORDERDETAIL.ORDER_LINE_NUMBER, ORDER.CUSTOMER_NUMBER))
                .select(field(name("customer_number")), inline("Order Line Number").as("metric"),
                        field(name("order_line_number"))).from(name("cte")) // 1
                .unionAll(select(field(name("customer_number")), inline("Sum Price").as("metric"),
                        field(name("sum_price"))).from(name("cte"))) // 2                    
                .unionAll(select(field(name("customer_number")), inline("Sum Quantity").as("metric"),
                        field(name("sum_quantity"))).from(name("cte"))) // 3                   
                .fetch();

        // use lateral join
        ctx.with("cte")
                .as(select(ORDER.CUSTOMER_NUMBER, ORDERDETAIL.ORDER_LINE_NUMBER,
                        sum(ORDERDETAIL.PRICE_EACH).as("sum_price"),
                        sum(ORDERDETAIL.QUANTITY_ORDERED).as("sum_quantity"))
                        .from(ORDER)
                        .join(ORDERDETAIL)
                        .on(ORDER.ORDER_ID.eq(ORDERDETAIL.ORDER_ID))
                        .groupBy(ORDERDETAIL.ORDER_LINE_NUMBER, ORDER.CUSTOMER_NUMBER))
                .select(field(name("customer_number")),
                        field(name("t", "metric")), field(name("t", "value")))
                .from(table(name("cte")), lateral(
                        select(inline("Order Line Number").as("metric"),
                                field(name("order_line_number")).as("value"))
                                .unionAll(select(inline("Sum Price").as("metric"),
                                        field(name("sum_price")).as("value")))
                                .unionAll(select(inline("Sum Quantity").as("metric"),
                                        field(name("sum_quantity")).as("value"))))
                        .as("t"))
                .fetch();
    }
}
