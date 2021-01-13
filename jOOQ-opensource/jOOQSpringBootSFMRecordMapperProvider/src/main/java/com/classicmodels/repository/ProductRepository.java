package com.classicmodels.repository;

import com.classicmodels.pojo.ProductDTO;
import java.util.List;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.simpleflatmapper.jooq.JooqMapperFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final DSLContext create;

    public ProductRepository(DSLContext create) {
        this.create = create;
    }

    public List<ProductDTO> findProductsNameVendorAndStock() {

        List<ProductDTO> result = DSL.using(create.configuration()
                .set(JooqMapperFactory.newInstance()
                        //.ignorePropertyNotFound()
                        .newRecordMapperProvider()))
                .select(PRODUCT.PRODUCT_ID,
                        PRODUCT.PRODUCT_NAME,
                        PRODUCT.PRODUCT_VENDOR,
                        PRODUCT.QUANTITY_IN_STOCK)
                .from(PRODUCT)
                .orderBy(PRODUCT.PRODUCT_ID)
                .fetchInto(ProductDTO.class);

        return result;
    }
}
