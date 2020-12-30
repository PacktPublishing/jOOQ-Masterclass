package com.classicmodels.repository;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Transactional
    public void insertProductWithSpecs() throws UnknownHostException {

        ctx.insertInto(PRODUCT, PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_LINE, PRODUCT.SPECS)
                .values("2002 Masserati Levante", "Classic Cars",
                        Map.of("Length (in)", "197", "Width (in)", "77.5", "Height (in)", 
                                "66.1", "Engine", "Twin Turbo Premium Unleaded V-6"))
                .execute();
    }

    public void fetchProductSpecs() {

        List<Map<String, String>> specs = ctx.select(PRODUCT.SPECS)
                .from(PRODUCT)
                .where(PRODUCT.PRODUCT_NAME.eq("2002 Masserati Levante"))
                .fetch(PRODUCT.SPECS);

        System.out.println("Product specs: " + specs);
    }
}
