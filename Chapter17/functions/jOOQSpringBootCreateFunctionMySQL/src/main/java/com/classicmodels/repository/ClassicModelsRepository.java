package com.classicmodels.repository;

import java.math.BigDecimal;
import org.jooq.DSLContext;
import org.jooq.Parameter;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.in;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.return_;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.SQLDataType.DECIMAL;
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

    public void createScalarFunction() {

        Parameter<Integer> quantity = in("quantity", INTEGER);
        Parameter<BigDecimal> list_price = in("list_price", DECIMAL);
        Parameter<BigDecimal> discount = in("discount", DECIMAL);

        ctx.dropFunctionIfExists("net_price_each_jooq")
                .execute();
        
        // or, use ctx.createOrReplaceFunction() instead of dropping via dropFunction()
        ctx.createFunction("net_price_each_jooq")               
                .parameters(
                        quantity, list_price, discount
                )
                .returns(DECIMAL(10, 2))
                .deterministic()
                .as(return_(quantity.mul(list_price).mul(inline(1).minus(discount))))                
                .execute();
        
        // call this function (plain SQL)
        float result = ctx.select(function(name("net_price_each_jooq"), 
                DECIMAL(10, 2), val(10), val(20.45), val(0.33)))
                .fetchOneInto(Float.class);
        
        System.out.println("Result: " + result);
        
        // calling the previously created functions via the generated code
        // can be done as you saw in the application jOOQSpringBootSFMySQL
    }
}