package com.classicmodels.repository;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext mysqlCtx;
    private final DSLContext postgresqlCtx;

    public ClassicModelsRepository(@Qualifier("mysqlDSLContext") DSLContext mysqlCtx, 
            @Qualifier("postgresqlDSLContext") DSLContext postgresqlCtx) {
        this.mysqlCtx = mysqlCtx;
        this.postgresqlCtx = postgresqlCtx;
    }

    public void tenant() {

        DSL.using(
                "jdbc:mysql://localhost:3306/classicmodels", "root", "root")
                .select().from(mysql.jooq.generated.tables.Product.PRODUCT).fetch();

        DSL.using(
                "jdbc:postgresql://localhost:5432/classicmodels", "postgres", "root")
                .select().from(postgresql.jooq.generated.tables.Product.PRODUCT).fetch();

        // or, via the injected DSLContexts
        mysqlCtx.select().from(mysql.jooq.generated.tables.Product.PRODUCT).fetch();
        
        postgresqlCtx.select().from(postgresql.jooq.generated.tables.Product.PRODUCT).fetch();
    }
}
