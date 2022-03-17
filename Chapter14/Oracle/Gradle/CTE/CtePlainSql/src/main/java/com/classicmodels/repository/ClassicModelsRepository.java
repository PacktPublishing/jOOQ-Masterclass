package com.classicmodels.repository;

import org.jooq.CommonTableExpression;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Result;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.resultQuery;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.VARCHAR;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void cte() {        
        
        CommonTableExpression<Record2<Long, String>> cte = name("cte")
                .fields("pid", "ppl").as(
                resultQuery(
                        // Put any plain SQL statement here
                        """
                        select "CLASSICMODELS"."PRODUCT"."PRODUCT_ID", "CLASSICMODELS"."PRODUCT"."PRODUCT_LINE" 
                        from "CLASSICMODELS"."PRODUCT" 
                        where "CLASSICMODELS"."PRODUCT"."QUANTITY_IN_STOCK" > 0            
                        """
                ).coerce(field("pid", BIGINT), field("ppl", VARCHAR))
        );

        Result<Record2<Long, String>> result = ctx.with(cte).selectFrom(cte).fetch();
        
        System.out.println("Result:\n" + result);
    }
}