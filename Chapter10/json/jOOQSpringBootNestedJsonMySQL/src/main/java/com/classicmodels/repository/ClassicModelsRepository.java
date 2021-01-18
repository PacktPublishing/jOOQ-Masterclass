package com.classicmodels.repository;

import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import org.jooq.JSON;
import org.jooq.Record1;
import org.jooq.Result;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.jsonArrayAgg;
import static org.jooq.impl.DSL.jsonObjectAgg;
import static org.jooq.impl.DSL.key;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void ToJson() {

        Result<Record1<JSON>> result = ctx.select(
                jsonObject(
                        key("productLine").value(PRODUCTLINE.PRODUCT_LINE),
                        key("textDescription").value(PRODUCTLINE.TEXT_DESCRIPTION),
                        key("products").value(jsonArrayAgg(
                                jsonObject(key("productName").value(PRODUCT.PRODUCT_ID),
                                        key("productVendor").value(PRODUCT.PRODUCT_VENDOR),
                                        key("quantityInStock").value(PRODUCT.QUANTITY_IN_STOCK),
                                        key("orderdetail")
                                                .value(jsonArrayAgg(
                                                        jsonObject(
                                                                key("qod").value(ORDERDETAIL.QUANTITY_ORDERED))
                                                ))))
                        )))
                //        .orderBy(PRODUCT.QUANTITY_IN_STOCK)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .join(ORDERDETAIL)
                .on(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID))
                .groupBy(PRODUCTLINE.PRODUCT_LINE, PRODUCT.PRODUCT_ID)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetch();

        System.out.println("Example 4 (one-to-many):\n" + result.formatJSON());
    }
}
