package com.classicmodels.repository;

import com.classicmodels.model.ProductLine;
import java.util.List;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import org.simpleflatmapper.jooq.SelectQueryMapper;
import org.simpleflatmapper.jooq.SelectQueryMapperFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;
    private final SelectQueryMapper<ProductLine> productMapper;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
        this.productMapper = SelectQueryMapperFactory
                .newInstance()
                .newMapper(ProductLine.class);
    }

    public List<ProductLine> findProductLineAndProductJooq() {

        List<ProductLine> products = productMapper.asList(
                ctx.select()
                        .from(PRODUCTLINE)
                        .innerJoin(PRODUCT).on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                        .orderBy(PRODUCTLINE.PRODUCT_LINE));

        return products;
    }

    public List<ProductLine> findProductLineJooq() {
        return ctx.selectFrom(PRODUCTLINE)
                .fetchInto(ProductLine.class);
    }

    public void updateProductLineDescriptionJooq() {

        ctx.update(PRODUCTLINE)
                .set(PRODUCTLINE.TEXT_DESCRIPTION, "Classic cars are so cool (jOOQ as well)!")
                .where(PRODUCTLINE.PRODUCT_LINE.eq("Classic Cars"))
                .execute();
    }
}