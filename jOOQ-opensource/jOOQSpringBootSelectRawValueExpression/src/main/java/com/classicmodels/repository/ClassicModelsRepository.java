package com.classicmodels.repository;

import java.time.LocalDate;
import java.util.List;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Product.PRODUCT;
import jooq.generated.tables.pojos.Customerdetail;
import jooq.generated.tables.pojos.Order;
import jooq.generated.tables.pojos.Product;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Row3;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext create;

    public ClassicModelsRepository(DSLContext create) {
        this.create = create;
    }

    /* Comparison predicates */
    public List<Product> findProductsByVendorScaleAndProductLine(
            String vendor, String scale, String productLine) {

        // row value expression
        Condition condition = row(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE, PRODUCT.PRODUCT_LINE)
                .eq(vendor, scale, productLine);

        // SQL query using the above row value expression
        List<Product> result = create.selectFrom(PRODUCT)
                .where(condition)
                .fetchInto(Product.class);

        return result;
    }

    public Product findProductByIdVendorAsCarouselDieCastLegendsScaleAndProductLine(Long productId) {

        // row value expression
        Condition condition = row(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_VENDOR,
                PRODUCT.PRODUCT_SCALE, PRODUCT.PRODUCT_LINE)
                .eq(select(PRODUCT.PRODUCT_ID, val("Carousel DieCast Legends"),
                        PRODUCT.PRODUCT_SCALE, PRODUCT.PRODUCT_LINE)
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_ID.eq(productId)));

        // SQL query using the above row value expression
        Product result = create.selectFrom(PRODUCT)
                .where(condition)
                .fetchOneInto(Product.class);

        return result;
    }

    /* IN predicates */
    public List<Product> findProductsByVendorScaleAndProductLineIn() {

        // row value expression
        Condition condition = row(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE, PRODUCT.PRODUCT_LINE)
                .in(row("Carousel DieCast Legends", "1:24", "Classic Cars"),
                        row("Exoto Designs", "1:18", "Vintage Cars"));

        // SQL query using the above row value expression
        List<Product> result = create.selectFrom(PRODUCT)
                .where(condition)
                .fetchInto(Product.class);

        return result;
    }

    public List<Product> findProductsByVendorScaleAndProductLineInCollection(
            List<Row3<String, String, String>> rows) {

        // row value expression
        Condition condition = row(PRODUCT.PRODUCT_VENDOR, PRODUCT.PRODUCT_SCALE, PRODUCT.PRODUCT_LINE)
                .in(rows);

        // SQL query using the above row value expression
        List<Product> result = create.selectFrom(PRODUCT)
                .where(condition)
                .fetchInto(Product.class);

        return result;
    }

    public List<Customerdetail> findProductsByVendorScaleAndProductLineInSelect() {

        // row value expression
        Condition condition = row(CUSTOMERDETAIL.CITY, CUSTOMERDETAIL.COUNTRY)
                .in(select(OFFICE.CITY, OFFICE.COUNTRY).from(OFFICE));

        // SQL query using the above row value expression
        List<Customerdetail> result = create.selectFrom(CUSTOMERDETAIL)
                .where(condition)
                .fetchInto(Customerdetail.class);

        return result;
    }

    /* BETWEEN predicates */
    public List<Order> findOrdersBetweenOrderDateAndShippedDate() {

        // row value expression
        Condition condition = row(ORDER.ORDER_DATE, ORDER.SHIPPED_DATE)
                .between(LocalDate.of(2003, 1, 6), LocalDate.of(2003, 1, 13))
                .and(LocalDate.of(2003, 12, 13), LocalDate.of(2003, 12, 11));

        // SQL query using the above row value expression
        List<Order> result = create.selectFrom(ORDER)
                .where(condition)
                .fetchInto(Order.class);

        return result;
    }
}
