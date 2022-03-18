package com.classicmodels.repository;

import org.jooq.DSLContext;
import org.jooq.Parameter;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.in;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.return_;
import static org.jooq.impl.SQLDataType.DECIMAL;
import static org.jooq.impl.SQLDataType.DOUBLE;
import static org.jooq.impl.SQLDataType.INTEGER;
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
    public void createScalarFunction() {

        Parameter<Integer> quantity = in("quantity", INTEGER);
        Parameter<Double> listPrice = in("list_price", DOUBLE);
        Parameter<Double> fractionOfPrice = in("fraction_of_price", DOUBLE);

        // ctx.dropFunctionIfExists("sale_price_jooq").execute(); followed by ctx.createFunction(...)
        
        ctx.createOrReplaceFunction("sale_price_jooq")
                .parameters(
                        quantity, listPrice, fractionOfPrice
                )
                .returns(DECIMAL(10, 2))
                .deterministic()
                .as(return_(listPrice.minus(listPrice.mul(fractionOfPrice)).mul(quantity)))
                .execute();
    }

    public void callScalarFunction() {
        // call this function (plain SQL)
        float result = ctx.select(function(name("sale_price_jooq"),
                DECIMAL(10, 2), inline(10), inline(20.45), inline(0.33)))
                .fetchOneInto(Float.class);

        System.out.println("Result: " + result);

        // calling the previously created functions via the generated code
        // can be done as you already know
    }
}
