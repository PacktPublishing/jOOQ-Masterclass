package com.classicmodels.repository;

import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Top3product.TOP3PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectCount;
import static org.jooq.impl.DSL.selectDistinct;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.trueCondition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private static final long GIVEN_ORDER_ID = 10100L;

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    /* Division examples */

    /* Find all orders containing the top 3 product */
    
    // EXAMPLE 1 - Division via INNER JOIN
    public void fetchOrderContainingTop3ProductsViaInnerJoin() {

        System.out.println("EXAMPLE 1\n"
                + ctx.select(ORDERDETAIL.ORDER_ID)
                        .from(ORDERDETAIL)
                        .innerJoin(TOP3PRODUCT)
                        .on(ORDERDETAIL.PRODUCT_ID.eq(TOP3PRODUCT.PRODUCT_ID))
                        .groupBy(ORDERDETAIL.ORDER_ID)
                        .having(count(ORDERDETAIL.ORDER_ID).eq(
                                select(count(TOP3PRODUCT.PRODUCT_ID)).from(TOP3PRODUCT)))
                        .fetch()
        );
    }

    // EXAMPLE 2 - Division via WHERE NOT EXISTS
    public void fetchOrderContainingTop3ProductsViaWhereNotExists() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select().from(
                        selectDistinct(ORDERDETAIL.ORDER_ID.as("OID")).from(ORDERDETAIL).asTable("T1")
                                .whereNotExists(selectOne().from(TOP3PRODUCT)
                                        .whereNotExists(selectOne().from(ORDERDETAIL)
                                                .where(field(name("T1", "OID")).eq(ORDERDETAIL.ORDER_ID)
                                                        .and(TOP3PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID))))))
                        .fetch()
        );
    }

    // EXAMPLE 3 - Division via ANTI JOIN
    public void fetchOrderContainingTop3ProductViaAntiJoin() {

        System.out.println("EXAMPLE 3\n"
                + ctx.select().from(
                        selectDistinct(ORDERDETAIL.ORDER_ID.as("OID")).from(ORDERDETAIL).asTable("T")
                                .leftAntiJoin(TOP3PRODUCT
                                        .leftAntiJoin(ORDERDETAIL)
                                        .on(field(name("T", "OID")).eq(ORDERDETAIL.ORDER_ID)
                                                .and(TOP3PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID))))
                                .on(trueCondition())) // or, val(1).eq(val(1)), one().eq(one())
                        .fetch()
        );
    }

    // EXAMPLE 4 - Division via jOOQ
    public void fetchOrderContainingTop3ProductViajOOQ() {

        System.out.println("EXAMPLE 4\n"
                + ctx.select().from(
                        ORDERDETAIL
                                .divideBy(TOP3PRODUCT)
                                .on(field(TOP3PRODUCT.PRODUCT_ID).eq(ORDERDETAIL.PRODUCT_ID))
                                .returning(ORDERDETAIL.ORDER_ID))
                        .fetch()
        );
    }

    /* Find all orders containing at least the products from the given order (e.g., 10100)) */
    
    // EXAMPLE 5 - Division via INNER JOIN
    public void fetchOrderContainingAtLeastCertainProductsViaInnerJoin() {

        System.out.println("EXAMPLE 5\n"
                + ctx.select(field(name("OID")))
                        .from(
                                select(PRODUCT.PRODUCT_ID.as("P1")).from(PRODUCT).asTable("T1")
                                        .innerJoin(select(ORDERDETAIL.ORDER_ID.as("OID"),
                                                ORDERDETAIL.PRODUCT_ID.as("P2")).from(ORDERDETAIL).asTable("T2"))
                                        .on(field(name("T1", "P1")).eq(field(name("T2", "P2"))))
                                        .innerJoin(select(ORDERDETAIL.PRODUCT_ID.as("P3"))
                                                .from(ORDERDETAIL).where(ORDERDETAIL.ORDER_ID.eq(GIVEN_ORDER_ID)).asTable("T3"))
                                        .on(field(name("T1", "P1")).eq(field(name("T3", "P3")))))
                        .groupBy(field(name("T2", "OID")))
                        .having(count().eq(selectCount()
                                .from(ORDERDETAIL).where(ORDERDETAIL.ORDER_ID.eq(GIVEN_ORDER_ID))))
                        .fetch()
        );
    }

    // EXAMPLE 6 - Division via WHERE NOT EXISTS
    public void fetchOrderContainingAtLeastCertainProductsViaWhereNotExists() {

        System.out.println("EXAMPLE 6\n"
                + ctx.select().from(
                        selectDistinct(ORDERDETAIL.ORDER_ID.as("OID")).from(ORDERDETAIL).asTable("T1")
                                .whereNotExists(selectOne().from(select(ORDERDETAIL.PRODUCT_ID.as("P"))
                                        .from(ORDERDETAIL).where(ORDERDETAIL.ORDER_ID.eq(GIVEN_ORDER_ID)).asTable("T2"))
                                        .whereNotExists(selectOne().from(ORDERDETAIL)
                                                .where(field(name("T1", "OID")).eq(ORDERDETAIL.ORDER_ID)
                                                        .and(field(name("T2", "P")).eq(ORDERDETAIL.PRODUCT_ID))))))
                        .fetch()
        );
    }

    // EXAMPLE 7 - Division via ANTI JOIN
    public void fetchOrderContainingAtLeastCertainProductsViaAntiJoin() {

        System.out.println("EXAMPLE 7\n"
                + ctx.select().from(
                        ctx.selectDistinct(ORDERDETAIL.ORDER_ID.as("OID")).from(ORDERDETAIL).asTable("T1")
                                .leftAntiJoin(select(ORDERDETAIL.PRODUCT_ID.as("P"))
                                        .from(ORDERDETAIL).where(ORDERDETAIL.ORDER_ID.eq(GIVEN_ORDER_ID)).asTable("T2")
                                        .leftAntiJoin(ORDERDETAIL)
                                        .on(field(name("T1", "OID")).eq(ORDERDETAIL.ORDER_ID)
                                                .and(field(name("T2", "P")).eq(ORDERDETAIL.PRODUCT_ID))))
                                .on(trueCondition())) // or, val(1).eq(val(1)), one().eq(one())
                        .fetch()
        );
    }

    // EXAMPLE 8 - Division via jOOQ
    public void fetchOrderContainingAtLeastCertainProductsViajOOQ() {

        System.out.println("EXAMPLE 8\n"
                + ctx.select().from(
                        ORDERDETAIL
                                .divideBy(select(ORDERDETAIL.PRODUCT_ID.as("P"))
                                        .from(ORDERDETAIL).where(ORDERDETAIL.ORDER_ID
                                        .eq(GIVEN_ORDER_ID)).asTable("T"))
                                .on(field(name("T", "P")).eq(ORDERDETAIL.PRODUCT_ID))
                                .returning(ORDERDETAIL.ORDER_ID))
                        .fetch()
        );
    }
}