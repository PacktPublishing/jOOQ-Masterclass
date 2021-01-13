package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleProductLine;
import java.util.List;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.simpleflatmapper.jooq.SelectQueryMapper;
import org.simpleflatmapper.jooq.SelectQueryMapperFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final SelectQueryMapper<SimpleProductLine> sqMapper;
    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
        this.sqMapper = SelectQueryMapperFactory
                .newInstance()
                .newMapper(SimpleProductLine.class);
    }

    public List<SimpleProductLine> findProductLineWithProducts() {

        List<SimpleProductLine> result = sqMapper.asList(
                ctx.select(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                              PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                        .from(PRODUCTLINE)
                        .innerJoin(PRODUCT)
                        .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                        .orderBy(PRODUCTLINE.PRODUCT_LINE)
        );

        return result;
    }
}
