package com.classicmodels.repository;

import java.util.List;
import java.util.Map;
import jooq.generated.tables.pojos.Product;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }
    
    public Page<Product> fetchProductsPageExtraSelectCount(int page, int size) {

        long total = ctx.fetchCount(PRODUCT);

        List<Product> result = ctx.selectFrom(PRODUCT)
                .orderBy(PRODUCT.PRODUCT_ID)
                .limit(size)
                .offset(size * page)
                .fetchInto(Product.class);

        Page<Product> pageOfProduct = new PageImpl(result,
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, PRODUCT.PRODUCT_ID.getName())),
                total);

        return pageOfProduct;
    }

    public Page<Product> fetchProductsPageWithoutExtraSelectCount(int page, int size) {

        Map<Integer, List<Product>> result = ctx.select(
                PRODUCT.asterisk(), count().over().as("total"))
                .from(PRODUCT)
                .orderBy(PRODUCT.PRODUCT_ID)
                .limit(size)
                .offset(size * page)
                .fetchGroups(field("total", Integer.class), Product.class);
              
        Page<Product> pageOfProduct = new PageImpl(result.values().iterator().next(), 
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, PRODUCT.PRODUCT_ID.getName())),
                result.entrySet().iterator().next().getKey());
        
        return pageOfProduct;
    }

}
