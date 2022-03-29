package com.classicmodels.repository;

import java.util.logging.Logger;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import org.jooq.impl.CallbackExecuteListener;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private static final Logger logger = Logger.getLogger(ClassicModelsRepository.class.getName());

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void selectProducts() {

        ctx.configuration().derive(new CallbackExecuteListener()
                .onRenderEnd(ecx -> {

                    String sql = ecx.sql();

                    if (sql != null) {
                        ecx.sql(sql.replace(
                                "select",
                                "select /*+ MAX_EXECUTION_TIME(5) */"
                        ));

                        logger.info(() -> {
                            return "Executing modified query : " + ecx.sql();
                        });
                    }
                }))
                .dsl()
                .select(PRODUCTLINE.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .fetch();
    }
}
