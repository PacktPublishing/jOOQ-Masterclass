package com.classicmodels.repository;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void selectOfficeAddress() {

        String sql = ctx.configuration()
                .derive(SQLDialect.ORACLE)
                .dsl()
                .render(ctx.parser().parseQuery("""
          SELECT concat_ws('|', city, address_line_first, address_line_second, country, territory) AS address 
          FROM office
        """));

        ctx.resultQuery(sql).fetch();
    }
}
