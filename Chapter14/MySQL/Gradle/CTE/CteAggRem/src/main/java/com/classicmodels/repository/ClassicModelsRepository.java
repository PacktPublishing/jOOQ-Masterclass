package com.classicmodels.repository;

import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sum;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    /*
    Calculating Top N items and Aggregating (sum) the remainder into "Remainder"
    */
    
    public void cte1() {

        var t = name("cte_top3")
                .fields("employee_nr", "sales")
                .as(select(SALE.EMPLOYEE_NUMBER, sum(SALE.SALE_))
                        .from(SALE)
                        .groupBy(SALE.EMPLOYEE_NUMBER)
                        .orderBy(sum(SALE.SALE_).desc())
                        .limit(3));

        ctx.with(t)
                .select()
                .from(name("cte_top3"))
                .unionAll(select(inline("Remainder"), sum(SALE.SALE_)).from(SALE)
                        .where(SALE.EMPLOYEE_NUMBER.notIn(
                                select(t.field("employee_nr", Long.class)).from(name("cte_top3")))))
                .fetch();
    }

    public void cte2() {

        ctx.with("cte_top3")
                .as(select(SALE.EMPLOYEE_NUMBER.as("employee_nr"), sum(SALE.SALE_).as("sales"))
                        .from(SALE)
                        .groupBy(SALE.EMPLOYEE_NUMBER)
                        .orderBy(sum(SALE.SALE_).desc())
                        .limit(3))
                .select()
                .from(name("cte_top3"))
                .unionAll(select(inline("Remainder"), sum(SALE.SALE_)).from(SALE)
                        .where(SALE.EMPLOYEE_NUMBER.notIn(
                                select(field(name("employee_nr"), Long.class)).from(name("cte_top3")))))
                .fetch();
    }
}
