package com.classicmodels.repository;

import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    @Transactional
    public void tenant() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {

            String authority = auth.getAuthorities().iterator().next().getAuthority();
            String database = authority.substring(5).toLowerCase();

            DSL.using(
                    "jdbc:mysql://localhost:3306/" + database, "root", "root")
                    .configuration().derive(new Settings()
                            .withRenderCatalog(Boolean.FALSE)
                            .withRenderSchema(Boolean.FALSE))
                    .dsl()
                    .insertInto(PRODUCT, PRODUCT.PRODUCT_NAME, PRODUCT.QUANTITY_IN_STOCK)
                    .values("Product", 100)
                    .execute();
        }
    }
}
