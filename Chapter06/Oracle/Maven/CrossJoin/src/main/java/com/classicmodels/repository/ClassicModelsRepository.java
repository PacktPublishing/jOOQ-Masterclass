package com.classicmodels.repository;

import jooq.generated.tables.Customer;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Department.DEPARTMENT;
import static jooq.generated.tables.Office.OFFICE;
import jooq.generated.tables.Order;
import static jooq.generated.tables.Order.ORDER;
import jooq.generated.tables.Orderdetail;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import jooq.generated.tables.Product;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.trueCondition;
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
    public void joinOfficeDepartmentViaImplicitCrossJoin() {

        System.out.println("EXAMPLE 1\n"
                + ctx.select()
                        .from(OFFICE, DEPARTMENT)
                        .fetch()
        );
    }

    // EXAMPLE 2
    public void joinOfficeDepartmentCertainColsViaImplicitCrossJoin() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select(OFFICE.CITY, OFFICE.COUNTRY,
                        DEPARTMENT.NAME)
                        .from(OFFICE, DEPARTMENT)
                        .where(row(OFFICE.CITY, OFFICE.COUNTRY).isNotNull())
                        .fetch()
        );
    }

    // EXAMPLE 3
    public void joinOfficeDepartmentViaCrossJoinAsInnerJoin() {

        System.out.println("EXAMPLE 3\n"
                + ctx.select()
                        .from(OFFICE)
                        .innerJoin(DEPARTMENT)
                        .on(trueCondition())
                        // .on(one().eq(one()))
                        // .on(val(1).eq(val(1)))
                        .fetch()
        );
    }

    // EXAMPLE 4
    public void joinOfficeDepartmentViaCrossJoin() {

        System.out.println("EXAMPLE 4\n"
                + ctx.select()
                        .from(OFFICE)
                        .crossJoin(DEPARTMENT)
                        .fetch()
        );
    }

    // EXAMPLE 5
    public void joinOfficeDepartmentCertainColsViaCrossJoin() {

        System.out.println("EXAMPLE 5\n"
                + ctx.select(OFFICE.CITY, OFFICE.COUNTRY,
                        DEPARTMENT.NAME)
                        .from(OFFICE)
                        .crossJoin(DEPARTMENT)
                        .where(row(OFFICE.CITY, OFFICE.COUNTRY).isNotNull())
                        .fetch()
        );
    }

    // EXAMPLE 6
    public void joinOfficeDepartmentConcatCertainColsViaCrossJoin() {

        System.out.println("EXAMPLE 6\n"
                + ctx.select(concat(OFFICE.CITY, inline(", "), OFFICE.COUNTRY,
                        inline(": "), DEPARTMENT.NAME).as("offices"))
                        .from(OFFICE)
                        .crossJoin(DEPARTMENT)
                        .where(row(OFFICE.CITY, OFFICE.COUNTRY).isNotNull())
                        .fetch()
        );
    }

    // EXAMPLE 7
    /*
    select
        "c"."CUSTOMER_NUMBER",
        "p"."PRODUCT_ID",
        nvl("sales",
        ?) 
    from
        "CLASSICMODELS"."CUSTOMER" "c" cross 
    join
        "CLASSICMODELS"."PRODUCT" "p" 
    left outer join
        (
            select
                "c"."CUSTOMER_NUMBER" "cn",
                "p"."PRODUCT_ID" "pi",
                sum(("i"."QUANTITY_ORDERED" * "i"."PRICE_EACH")) "sales" 
            from
                "CLASSICMODELS"."ORDER" "o" 
            join
                "CLASSICMODELS"."ORDERDETAIL" "i" 
                    on "i"."ORDER_ID" = "o"."ORDER_ID" 
            join
                "CLASSICMODELS"."CUSTOMER" "c" 
                    on "c"."CUSTOMER_NUMBER" = "o"."CUSTOMER_NUMBER" 
            join
                "CLASSICMODELS"."PRODUCT" "p" 
                    on "p"."PRODUCT_ID" = "i"."PRODUCT_ID" 
            group by
                "c"."CUSTOMER_NUMBER",
                "p"."PRODUCT_ID"
        ) "alias_119882641" 
            on (
                "cn" = "c"."CUSTOMER_NUMBER" 
                and "pi" = "p"."PRODUCT_ID"
            ) 
    where
        "sales" is null 
    order by
        "p"."PRODUCT_ID",
        "c"."CUSTOMER_NUMBER"    
    */
    public void findProductsNoSalesAcrossCustomers() {

        Customer c = CUSTOMER.as("c");
        Product p = PRODUCT.as("p");
        Order o = ORDER.as("o");
        Orderdetail i = ORDERDETAIL.as("i");

        System.out.println("EXAMPLE 7\n"
                + ctx.select(c.CUSTOMER_NUMBER, p.PRODUCT_ID, nvl(field(name("sales")), 0))
                        .from(c)
                        .crossJoin(p)
                        .leftJoin(select(c.CUSTOMER_NUMBER.as(name("cn")), p.PRODUCT_ID.as(name("pi")),
                                sum(i.QUANTITY_ORDERED.mul(i.PRICE_EACH)).as(name("sales")))
                                .from(o)
                                .innerJoin(i).on(i.ORDER_ID.eq(o.ORDER_ID))
                                .innerJoin(c).on(c.CUSTOMER_NUMBER.eq(o.CUSTOMER_NUMBER))
                                .innerJoin(p).on(p.PRODUCT_ID.eq(i.PRODUCT_ID))
                                .groupBy(c.CUSTOMER_NUMBER, p.PRODUCT_ID))
                        .on(field(name("cn")).eq(c.CUSTOMER_NUMBER)
                                .and(field(name("pi")).eq(p.PRODUCT_ID)))
                        .where(field(name("sales")).isNull())
                        .orderBy(p.PRODUCT_ID, c.CUSTOMER_NUMBER)
                        .fetch()
        );
    }
}