package com.classicmodels.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private static final Logger log = LoggerFactory.getLogger(ClassicModelsRepository.class);

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void selectProductsAndLog() {

        // var result =
        ctx.select(ORDERDETAIL.ORDER_ID, ORDERDETAIL.QUANTITY_ORDERED,
                PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                .from(ORDERDETAIL)
                .join(PRODUCT)
                .on(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))
                .fetch();

        // log.debug("Result set:\n" + result.format(result.size()));  
        
        // this query uses lazy access to ResultSet and logs only the 5 buffered records
        ctx.selectFrom(PRODUCT)
                .collect(Collectors.toList());
    }
}
