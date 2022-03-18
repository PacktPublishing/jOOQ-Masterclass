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

        Table<?> t31 = values(row(1, "John"), row(2, "Mary"), row(3, "Kelly"))
                .as("t", "id", "name"); // or, .asTable("t", "id", "name");
        Table<?> t32 = select().from(values(row(1, "John"), row(2, "Mary"), row(3, "Kelly"))
                .as("t", "id", "name")).asTable();

        // select "t"."id", "t"."name" from (values (1, 'John'), (2, 'Mary'), (3, 'Kelly')) as "t" ("id", "name")
        ctx.selectFrom(t31).fetch();

        // select "alias_116047195"."id", "alias_116047195"."name" from (
        // select "t"."id", "t"."name" from (values (1, 'John'), (2, 'Mary'), (3, 'Kelly')) 
        // as "t" ("id", "name")) as "alias_116047195"
        ctx.selectFrom(t32).fetch();

        Table<?> t4 = select().from(values(row(1, "John"),
                row(2, "Mary"), row(3, "Kelly"))).asTable("t", "id", "name");

        // select "t"."id", "t"."name" from (select "v"."c1", "v"."c2" from (values (1, 'John'), 
        // (2, 'Mary'), (3, 'Kelly')) as "v" ("c1", "c2")) as "t" ("id", "name")
        ctx.selectFrom(t4).fetch();

        Table<?> t5 = select().from(values(row(1, "John"),
                row(2, "Mary"), row(3, "Kelly")).as("x", "x1", "x2")).asTable("t", "id", "name");

        // select "t"."id", "t"."name" from (select "x"."x1", "x"."x2" from (values (1, 'John'), 
        // (2, 'Mary'), (3, 'Kelly')) as "x" ("x1", "x2")) as "t" ("id", "name")
        ctx.selectFrom(t5).fetch();
    }

    public void nestedSelectDerivedTable() {

        // jOOQ generates the derived table alias                
        System.out.println("EXAMPLE 1.1:\n"
                + ctx.select().from(select(ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
                        .where(ORDERDETAIL.QUANTITY_ORDERED.gt(50)))
                        .innerJoin(PRODUCT)
                        .on(field(name("PRICE_EACH")).eq(PRODUCT.BUY_PRICE))
                        .fetch());

        // explicit derived table alias, but not required by jOOQ
        System.out.println("EXAMPLE 1.2:\n"
                + ctx.select().from(select(ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
                        .where(ORDERDETAIL.QUANTITY_ORDERED.gt(50)).asTable("T"))
                        .innerJoin(PRODUCT)
                        .on(field(name("T", "PRICE_EACH")).eq(PRODUCT.BUY_PRICE))
                        .fetch());

        // without explicit derived table alias this query leads to an error caused by ambiguous reference of "product_id"
        System.out.println("EXAMPLE 1.3:\n"
                + ctx.select(PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, field(name("T", "PRICE_EACH")))
                        .from(select(ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
                                .where(ORDERDETAIL.QUANTITY_ORDERED.gt(50)).asTable("T"))
                        .innerJoin(PRODUCT)
                        .on(field(name("T", "PRODUCT_ID")).eq(PRODUCT.PRODUCT_ID))
                        .fetch());

        Table<?> t = select(ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
                .where(ORDERDETAIL.QUANTITY_ORDERED.gt(50)).asTable("T");
        // or, Table<?> t = table(select(ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.PRICE_EACH).from(ORDERDETAIL)
        //                        .where(ORDERDETAIL.QUANTITY_ORDERED.gt(50L))).as("T");

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
                        .on(t.field(name("PRICE_EACH"), BigDecimal.class).eq(PRODUCT.BUY_PRICE))
                        .fetch());

        System.out.println("EXAMPLE 1.5:\n"
                + ctx.select(PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, t.field(name("PRICE_EACH")))
                        .from(t)
                        .innerJoin(PRODUCT)
                        .on(t.field(name("PRODUCT_ID"), Long.class).eq(PRODUCT.PRODUCT_ID))
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

        Field<BigDecimal> avg = avg(ORDERDETAIL.PRICE_EACH).as("AVG");
        Field<Long> ord = ORDERDETAIL.ORDER_ID.as("ORD");

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
                .asTable("T");

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

        Sale sale1 = SALE.as("T");
        Table<?> sale2 = SALE.as("T");
        Table<?> sale3 = ctx.selectFrom(SALE).asTable("T");

        // single SELECT!
        System.out.println("EXAMPLE 4.1:\n"
                + ctx.select(sale1.fields())
                        .from(sale1)
                        .where(sale1.SALE_.gt(50000.0)).fetch());

        // single SELECT!
        System.out.println("EXAMPLE 4.2:\n"
                + ctx.select(sale2.fields())
                        .from(sale2)
                        .where(sale2.field(name("SALE"), Double.class).gt(50000.0)).fetch());

        // nested SELECT!
        System.out.println("EXAMPLE 4.3:\n"
                + ctx.select(sale3.fields())
                        .from(sale3)
                        .where(sale3.field(name("SALE"), Double.class).gt(50000.0)).fetch());

        // fields() and asterisk()        
        Table<?> t = ctx.select(SALE.EMPLOYEE_NUMBER, count(SALE.SALE_).as("SALES_COUNT"))
                .from(SALE)
                .groupBy(SALE.EMPLOYEE_NUMBER).asTable("T");

        System.out.println("EXAMPLE 4.4:\n"
                + ctx.select(t.fields())
                        .from(t)
                        .orderBy(t.field(name("SALES_COUNT"))).fetch());

        System.out.println("EXAMPLE 4.5:\n"
                + ctx.select(t.asterisk(), EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                        .from(EMPLOYEE, t)
                        .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(t.field(name("EMPLOYEE_NUMBER"), Long.class)))
                        .orderBy(t.field(name("SALES_COUNT"))).fetch());
    }

    public void derivedTableAndLateral() {

        // LATERAL keyword to connect a derived table to the previous table in the FROM clause
        System.out.println("EXAMPLE 5.1:\n"
                + ctx.select().from(EMPLOYEE, lateral(
                        select(count().as("SALES_COUNT")).from(SALE)
                                .where(SALE.EMPLOYEE_NUMBER.eq(EMPLOYEE.EMPLOYEE_NUMBER)).asTable("T")))
                        .fetch());

        System.out.println("EXAMPLE 5.2:\n"
                + ctx.select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                        field(name("OFFICE_CODE")), field(name("CITY")), field(name("STATE")))
                        .from(MANAGER, lateral(select(OFFICE.OFFICE_CODE.as("OFFICE_CODE"),
                                OFFICE.CITY.as("CITY"), OFFICE.STATE.as("STATE"))
                                .from(OFFICE).join(OFFICE_HAS_MANAGER)
                                .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                                .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID)).asTable("T")))
                        .orderBy(MANAGER.MANAGER_ID)
                        .fetch());

        // CROSS APPLY
        System.out.println("EXAMPLE 5.3:\n"
                + ctx.select().from(EMPLOYEE)
                        .crossApply(select(count().as("SALES_COUNT")).from(SALE)
                                .where(SALE.EMPLOYEE_NUMBER.eq(EMPLOYEE.EMPLOYEE_NUMBER)).asTable("T"))
                        .fetch());

        System.out.println("EXAMPLE 5.4:\n"
                + ctx.select(MANAGER.MANAGER_ID, MANAGER.MANAGER_NAME,
                        field(name("OFFICE_CODE")), field(name("CITY")), field(name("STATE")))
                        .from(MANAGER).crossApply(select(OFFICE.OFFICE_CODE.as("OFFICE_CODE"),
                        OFFICE.CITY.as("CITY"), OFFICE.STATE.as("STATE"))
                        .from(OFFICE).join(OFFICE_HAS_MANAGER)
                        .on(OFFICE.OFFICE_CODE.eq(OFFICE_HAS_MANAGER.OFFICES_OFFICE_CODE))
                        .where(MANAGER.MANAGER_ID.eq(OFFICE_HAS_MANAGER.MANAGERS_MANAGER_ID)).asTable("T"))
                        .orderBy(MANAGER.MANAGER_ID)
                        .fetch());
    }
}
