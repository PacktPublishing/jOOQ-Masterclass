package com.classicmodels.repository;

import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.jooq.Parameter;
import static org.jooq.impl.DSL.call;
import static org.jooq.impl.DSL.in;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.update;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.INTEGER;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void createProcedure() {
      
        Parameter<Long> product_id = in("product_id", BIGINT);
        Parameter<Integer> debit = in("debit", INTEGER);

        ctx.dropProcedureIfExists("update_msrp_jooq")
                .execute();

        // or, use ctx.createOrReplaceProcedure() instead of dropping via dropFunction()
        ctx.createProcedure("update_msrp_jooq")
                .parameters(
                        product_id, debit
                )
                .as(update(PRODUCT)
                        .set(PRODUCT.MSRP, PRODUCT.MSRP.minus(debit))
                        .where(PRODUCT.PRODUCT_ID.eq(product_id)))
                .execute();
    }
    
     public void callProcedure() {

        // CALL statement in an anonymous block
        var result1 = ctx.begin(call(name("update_msrp_jooq"))
                .args(val(1L), val(100)))
                .execute();
        
        // CALL statement directly
        var result2 = ctx.call(name("update_msrp_jooq"))
              .args(val(1L), val(100))
            .execute();
        
        System.out.println("Result 1 (Affected row(s)): " + result1);
        System.out.println("Result 2 (Affected row(s)): " + result2);
        
        // calling the previously created procedure via the generated code
        // can be done as you saw in the application jOOQSpringBootCreateProcedureOracle
     }
}
