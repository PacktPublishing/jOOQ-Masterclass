package com.classicmodels.repository;

import static jooq.generated.tables.ProductDev.PRODUCT_DEV;
import org.jooq.DSLContext;
import org.jooq.conf.MappedSchema;
import org.jooq.conf.MappedTable;
import org.jooq.conf.RenderMapping;
import org.jooq.conf.Settings;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public void tenant() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {

            String authority = auth.getAuthorities().iterator().next().getAuthority();
            String database = authority.substring(5).toLowerCase();

            ctx.configuration().derive(new Settings()
                    .withRenderMapping(new RenderMapping()
                            .withSchemata(
                                    new MappedSchema().withInput("development")
                                            .withOutput(database)
                                            .withTables(
                                                    new MappedTable().withInput("product_dev")
                                                            .withOutput("product_" + database))))).dsl()
                    .insertInto(PRODUCT_DEV, PRODUCT_DEV.PRODUCT_NAME, PRODUCT_DEV.QUANTITY_IN_STOCK)
                    .values("Product", 100)
                    .execute();
        }
    }
}
