package com.classicmodels.repository;

import com.classicmodels.pojo.Office;
import java.util.List;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public List<Office> findOfficesInTerritory(String territory) {

        /* Using jOOQ DSL to build and execute SQL */
        List<Office> result = ctx.selectFrom(table("office")) // or, ctx.select().from(table("office"))
                .where(field("territory").eq(territory))
                .fetchInto(Office.class); // or, fetch().into(Office.class)

        return result;
    }
}
