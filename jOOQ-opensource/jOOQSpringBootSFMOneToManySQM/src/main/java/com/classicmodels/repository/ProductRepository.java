package com.classicmodels.repository;

import com.classicmodels.pojo.ProductLineDTO;
import java.util.List;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.simpleflatmapper.jooq.SelectQueryMapper;
import org.simpleflatmapper.jooq.SelectQueryMapperFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final SelectQueryMapper<ProductLineDTO> productMapper;
    private final DSLContext create;

    public ProductRepository(DSLContext create) {
        this.create = create;
        this.productMapper = SelectQueryMapperFactory
                .newInstance()
                .newMapper(ProductLineDTO.class);
    }

    public List<ProductLineDTO> findProductLineWithProducts() {

        List<ProductLineDTO> products = productMapper.asList(
                create.select(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                              PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK)
                        .from(PRODUCTLINE)
                        .innerJoin(PRODUCT).using(PRODUCT.PRODUCT_LINE)
                        .orderBy(PRODUCTLINE.PRODUCT_LINE)
        );

        return products;
    }
}
