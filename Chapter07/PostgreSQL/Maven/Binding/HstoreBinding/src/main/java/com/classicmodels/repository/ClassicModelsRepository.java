package com.classicmodels.repository;

import java.util.List;
import java.util.Map;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import org.postgresql.util.HStoreConverter;
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
    public void insertProductWithSpecs() {

        
        // workaround to avoid defining a binding/converter
        // to test this, you need to disable the current binding
        /*
        ctx.insertInto(PRODUCT, PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_LINE, 
                PRODUCT.CODE, PRODUCT.SPECS)
                .values("2002 Masserati Levante", "Classic Cars", 599302L,
                        field("?::hstore", String.class,
                                HStoreConverter.toString(Map.of("Length (in)", "197", "Width (in)", "77.5", "Height (in)",
                                        "66.1", "Engine", "Twin Turbo Premium Unleaded V-6"))))
                .execute();
        */
      
        ctx.insertInto(PRODUCT, PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_LINE, 
                PRODUCT.CODE, PRODUCT.SPECS)
                .values("2002 Masserati Levante", "Classic Cars", 599302L,
                        Map.of("Length (in)", "197", "Width (in)", "77.5", "Height (in)", 
                                "66.1", "Engine", "Twin Turbo Premium Unleaded V-6"))
                .execute();         
    }

    public void fetchProductSpecs() {

        /*
        // workaround to avoid defining a binding/converter
        // to test this, you need to disable the current binding
        List<Map<String, String>> specs = ctx.select(PRODUCT.SPECS.coerce(String.class))
                .from(PRODUCT)
                .where(PRODUCT.PRODUCT_NAME.eq("2002 Masserati Levante"))
                .fetch(rs -> {
                    return HStoreConverter.fromString(rs.getValue(PRODUCT.SPECS).toString());
                });

        System.out.println("Product specs: " + specs);
        */
                
        List<Map<String, String>> specs = ctx.select(PRODUCT.SPECS)
                .from(PRODUCT)
                .where(PRODUCT.PRODUCT_NAME.eq("2002 Masserati Levante"))
                .fetch(PRODUCT.SPECS);

        System.out.println("Product specs (all): " + specs);  
        
        List<Map<String, String>> specsFiltered = ctx.select(PRODUCT.SPECS)
                .from(PRODUCT)
                .where(PRODUCT.PRODUCT_NAME.eq("2002 Masserati Levante")
                .and(PRODUCT.SPECS.contains(Map.of("Height (in)", "66.1"))))
                .fetch(PRODUCT.SPECS);
        
        System.out.println("Product specs (filtered): " + specsFiltered);
    }
}
