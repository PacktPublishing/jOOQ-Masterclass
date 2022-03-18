package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.OfficeHasManager.OFFICE_HAS_MANAGER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import jooq.generated.tables.Sale;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.lateral;
import static org.jooq.impl.DSL.name;
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

    public void unamedDerivedTable() {

        // unnamed derived table
        Table<?> t1 = select(inline(1).as("one")).asTable();
        // or, Table<?> t1 = table(select(inline(1).as("one")));

        // select "alias_30260683"."one" from (select 1 as "one") as "alias_30260683"
        ctx.selectFrom(t1).fetch();

        // explicit alias
        Table<?> t2 = select(inline(1).as("one")).asTable("t");
        // or, Table<?> t2 = table(select(inline(1).as("one"))).as("t");

        // select "t"."one" from (select 1 as "one") as "t"
        ctx.selectFrom(t2).fetch();
    }

    public void nestedSelectDerivedTable() {

        // jOOQ generates the derived table alias                
        System.out.println("EXAMPLE 1.1:\n"
                + ctx.select().from(select(ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
                        .where(ORDERDETAIL.QUANTITY_ORDERED.gt(50)))
                        .innerJoin(PRODUCT)
                        .on(field(name("price_each")).eq(PRODUCT.BUY_PRICE))
                        .fetch());

        // explicit derived table alias, but not required by jOOQ
        System.out.println("EXAMPLE 1.2:\n"
                + ctx.select().from(select(ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
                        .where(ORDERDETAIL.QUANTITY_ORDERED.gt(50)).asTable("t"))
                        .innerJoin(PRODUCT)
                        .on(field(name("t", "price_each")).eq(PRODUCT.BUY_PRICE))
                        .fetch());

        // without explicit derived table alias this query leads to an error caused by ambiguous reference of "product_id"
        System.out.println("EXAMPLE 1.3:\n"
                + ctx.select(PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, field(name("t", "price_each")))
                        .from(select(ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
                                .where(ORDERDETAIL.QUANTITY_ORDERED.gt(50)).asTable("t"))
                        .innerJoin(PRODUCT)
                        .on(field(name("t", "product_id")).eq(PRODUCT.PRODUCT_ID))
                        .fetch());

        Table<?> t = select(ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
                .where(ORDERDETAIL.QUANTITY_ORDERED.gt(50)).asTable("t");
        // or, Table<?> t = table(select(ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
        //                        .where(ORDERDETAIL.QUANTITY_ORDERED.gt(50))).as("t");

        // this also works, but jOOQ will attach the alias for the derived table
        // var t = select(ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
        //        .where(ORDERDETAIL.QUANTITY_ORDERED.gt(50));
        // var t = select(ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
        //       .where(ORDERDETAIL.QUANTITY_ORDERED.gt(50)).asTable();
        // var t = table(select(ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
        //        .where(ORDERDETAIL.QUANTITY_ORDERED.gt(50)));
        
        System.out.println("EXAMPLE 1.4:\n"
                + ctx.select()
                        .from(t)
                        .innerJoin(PRODUCT)
                        .on(t.field(name("price_each"), BigDecimal.class).eq(PRODUCT.BUY_PRICE))
                        .fetch());

        System.out.println("EXAMPLE 1.5:\n"
                + ctx.select(PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, t.field(name("price_each")))
                        .from(t)
                        .innerJoin(PRODUCT)
                        .on(t.field(name("product_id"), Long.class).eq(PRODUCT.PRODUCT_ID))
                        .fetch());

        // using <T> Field<T> field(Field<T> field) to gain type-safety        
        System.out.println("EXAMPLE 1.6:\n"
                + ctx.select(PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, t.field(ORDERDETAIL.PRICE_EACH))
                        .from(t)
                        .innerJoin(PRODUCT)
                        .on(t.field(ORDERDETAIL.PRODUCT_ID).eq(PRODUCT.PRODUCT_ID))
                        .fetch());

        System.out.println("EXAMPLE 1.7:\n"
                + ctx.select()
                        .from(t)
                        .innerJoin(PRODUCT)
                        .on(t.field(ORDERDETAIL.PRICE_EACH).eq(PRODUCT.BUY_PRICE))
                        .fetch());
    }

    public void useExtractedFieldsInDerivedTableAndQuery() {

        Field<BigDecimal> avg = avg(ORDERDETAIL.PRICE_EACH).as("avg");
        Field<Long> ord = ORDERDETAIL.ORDER_ID.as("ord");

        System.out.println("EXAMPLE 2.1:\n"
                + ctx.select(ORDERDETAIL.ORDER_ID, ORDERDETAIL.ORDERDETAIL_ID,
                        ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.PRICE_EACH)
                        .from(ORDERDETAIL, select(avg, ord)
                                .from(ORDERDETAIL)
                                .groupBy(ORDERDETAIL.ORDER_ID))
                        .where(ORDERDETAIL.ORDER_ID.eq(ord)
                                .and(ORDERDETAIL.PRICE_EACH.lt(avg)))
                        .orderBy(ORDERDETAIL.ORDER_ID)
                        .fetch());

        // extract the derived table
        Table<?> t = select(avg, ord)
                .from(ORDERDETAIL)
                .groupBy(ORDERDETAIL.ORDER_ID)
                .asTable("t");

        System.out.println("EXAMPLE 2.2:\n"
                + ctx.select(ORDERDETAIL.ORDER_ID, ORDERDETAIL.ORDERDETAIL_ID,
                        ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.PRICE_EACH)
                        .from(ORDERDETAIL, t)
                        .where(ORDERDETAIL.ORDER_ID.eq(ord)           // unqualified
                                .and(ORDERDETAIL.PRICE_EACH.lt(avg))) // unqualified
                        .orderBy(ORDERDETAIL.ORDER_ID)
                        .fetch());
        
        System.out.println("EXAMPLE 2.3:\n"
                + ctx.select(ORDERDETAIL.ORDER_ID, ORDERDETAIL.ORDERDETAIL_ID,
                        ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.PRICE_EACH)
                        .from(ORDERDETAIL, t)
                        .where(ORDERDETAIL.ORDER_ID.eq(t.field(ord))            // qualified
                                .and(ORDERDETAIL.PRICE_EACH.lt(t.field(avg))))  // qualified
                        .orderBy(ORDERDETAIL.ORDER_ID)
                        .fetch());
    }

    public void noNeedToTransformIntoTable() {

        // if a subquery is not a derived table then it can be extracted as a SELECT
        System.out.println("EXAMPLE 3.1:\n"
                + ctx.selectFrom(PRODUCT)
                        .where(row(PRODUCT.PRODUCT_ID, PRODUCT.BUY_PRICE).in(
                                select(ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
                                        .where(ORDERDETAIL.QUANTITY_ORDERED.gt(50))))
                        .fetch());

        // SelectConditionStep<Record2<Long, BigDecimal>>
        var s = select(ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
                .where(ORDERDETAIL.QUANTITY_ORDERED.gt(50));

        System.out.println("EXAMPLE 3.2:\n"
                + ctx.selectFrom(PRODUCT)
                        .where(row(PRODUCT.PRODUCT_ID, PRODUCT.BUY_PRICE).in(s))
                        .fetch());

        System.out.println("EXAMPLE 3.3:\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .where(select(avg(SALE.SALE_)).from(SALE).lt(
                                (select(sum(SALE.SALE_)).from(SALE)
                                        .where(EMPLOYEE.EMPLOYEE_NUMBER
                                                .eq(SALE.EMPLOYEE_NUMBER)))))
                        .fetch());

        // another subquery that can be extracted as a SELECT, no need to trasform it into a derived table
        var s1 = select(avg(SALE.SALE_)).from(SALE);
        var s2 = select(sum(SALE.SALE_)).from(SALE)
                .where(EMPLOYEE.EMPLOYEE_NUMBER
                        .eq(SALE.EMPLOYEE_NUMBER));

        System.out.println("EXAMPLE 3.4:\n"
                + ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.SALARY)
                        .from(EMPLOYEE)
                        .where(s1.lt((s2)))
                        .fetch());
    }

    public void selectAsteriskAndFields() {

        Sale sale1 = SALE.as("t");
        Table<?> sale2 = SALE.as("t");
        Table<?> sale3 = ctx.selectFrom(SALE).asTable("t");

        // single SELECT!
        System.out.println("EXAMPLE 4.1:\n"
                + ctx.select(sale1.fields())
                        .from(sale1)
                        .where(sale1.SALE_.gt(50000.0)).fetch());

        // single SELECT!
        System.out.println("EXAMPLE 4.2:\n"
                + ctx.select(sale2.fields())
                        .from(sale2)
                        .where(sale2.field(name("sale"), Double.class).gt(50000.0)).fetch());

        // nested SELECT!
        System.out.println("EXAMPLE 4.3:\n"
                + ctx.select(sale3.fields())
                        .from(sale3)
                        .where(sale3.field(name("sale"), Double.class).gt(50000.0)).fetch());

        // fields() and asterisk()        
        Table<?> t = ctx.select(SALE.EMPLOYEE_NUMBER, count(SALE.SALE_).as("sales_count"))
                .from(SALE)
                .groupBy(SALE.EMPLOYEE_NUMBER).asTable("t");

        System.out.println("EXAMPLE 4.4:\n"
                + ctx.select(t.fields())
                        .from(t)
                        .orderBy(t.field(name("sales_count"))).fetch());

        System.out.println("EXAMPLE 4.5:\n"
                + ctx.select(t.asterisk(), EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE, t)
                        .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(t.field(name("employee_number"), Long.class)))
                        .orderBy(t.field(name("sales_count"))).fetch());
    }

    public void derivedTableAndLateral() {

        // LATERAL keyword to connect a derived table to the previous table in the FROM clause
        System.out.println("EXAMPLE 5.1:\n"
                + ctx.select().from(EMPLOYEE, lateral(
                        select(count().as("sales_count")).from(SALE)
                                .where(SALE.EMPLOYEE_NUMBER.eq(EMPLOYEE.EMPLOYEE_NUMBER)).asTable("t")))
                        .fetch());

        System.out.println("EXAMPLE 5.2:\n"
                + ctx.select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                        field(name("office_code")), field(name("city")), field(name("state")))
                        .from(MANAGER, lateral(select(OFFICE.OFFICE_CODE.as("office_code"),
                                OFFICE.CITY.as("city"), OFFICE.STATE.as("state"))
                                .from(OFFICE).join(OFFICE_HAS_MANAGER)
                                .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                                .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID)).asTable("t")))
                        .orderBy(MANAGER.MANAGER_ID)
                        .fetch());        
    }
}
