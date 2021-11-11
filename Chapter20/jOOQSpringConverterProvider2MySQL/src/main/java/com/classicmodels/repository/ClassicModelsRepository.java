package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleProductLine;
import java.util.List;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.jsonArrayAgg;
import static org.jooq.impl.DSL.jsonObject;
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

    public void fetchProducts() {        
        
        List<SimpleProductLine> result = ctx.select(
                jsonObject(
                        key("productLine").value(PRODUCTLINE.PRODUCT_LINE),
                        key("textDescription").value(PRODUCTLINE.TEXT_DESCRIPTION),
                        key("products").value(jsonArrayAgg(
                                jsonObject(key("productName").value(PRODUCT.PRODUCT_NAME),
                                        key("productVendor").value(PRODUCT.PRODUCT_VENDOR),
                                        key("quantityInStock").value(PRODUCT.QUANTITY_IN_STOCK)))
                                .orderBy(PRODUCT.QUANTITY_IN_STOCK))))
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .groupBy(PRODUCTLINE.PRODUCT_LINE)
                .orderBy(PRODUCTLINE.PRODUCT_LINE)
                .fetchInto(SimpleProductLine.class);
        
        System.out.println("result:   " + result);
    }
}
