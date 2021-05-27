package com.classicmodels.repository;

import static jooq.generated.tables.DailyActivity.DAILY_ACTIVITY;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.case_;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.rowNumber;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.table;
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
    Calculating Weighted Moving Average in SQL
    ------------------------------------------

    with cte_wma1 as 
            (select day, sales, row_number() over () 
             from daily_activity)
        select cte_wma1.day, avg(cte_wma1.sales) as sales, 
        sum(case 
            when cte_wma1.row_number - cte_wma2.row_number = 0 then 0.4 * cte_wma2.sales
            when cte_wma1.row_number - cte_wma2.row_number = 1 then 0.3 * cte_wma2.sales
            when cte_wma1.row_number - cte_wma2.row_number = 2 then 0.2 * cte_wma2.sales
            when cte_wma1.row_number - cte_wma2.row_number = 3 then 0.1 * cte_wma2.sales
        end)
        from cte_wma1
        join cte_wma1 cte_wma2 on cte_wma2.row_number between cte_wma1.row_number - 3 and cte_wma1.row_number 
        group by 1
        order by 1                
     */
    
    public void cte1() {

        var t1 = name("cte_wma")
                .fields("day", "sales", "rn")
                .as(select(DAILY_ACTIVITY.DAY_DATE, DAILY_ACTIVITY.SALES, cast(rowNumber().over(), Float.class))
                        .from(DAILY_ACTIVITY));

        ctx.with(t1)
                .select(t1.field("day"), avg(t1.field("sales", Float.class)),
                        sum(
                                case_().when(t1.field("rn", Float.class)
                                        .minus(field(name("t2", "rn"))).eq(0f), t1.field("sales", Float.class).mul(0.4f))
                                        .when(t1.field("rn", Float.class)
                                                .minus(field(name("t2", "rn"))).eq(1f), t1.field("sales", Float.class).mul(0.3f))
                                        .when(t1.field("rn", Float.class)
                                                .minus(field(name("t2", "rn"))).eq(2f), t1.field("sales", Float.class).mul(0.2f))
                                        .when(t1.field("rn", Float.class)
                                                .minus(field(name("t2", "rn"))).eq(3f), t1.field("sales", Float.class).mul(0.1f))
                        ))
                .from(t1)
                .join(t1.as("t2"))
                .on(field(name("t2", "rn")).between(t1.field("rn").minus(3), t1.field("rn")))
                .groupBy(t1.field("day"))
                .orderBy(t1.field("day"))
                .fetch();
    }

    public void cte2() {

        ctx.with("cte_wma")
                .as(select(DAILY_ACTIVITY.DAY_DATE.as("day"), DAILY_ACTIVITY.SALES.as("sales"),
                        cast(rowNumber().over(), Float.class).as("rn"))
                        .from(DAILY_ACTIVITY))
                .select(field(name("cte_wma", "day")), avg(field(name("cte_wma", "sales"), Float.class)),
                        sum(
                                case_().when(field(name("cte_wma", "rn"), Float.class)
                                        .minus(field(name("t2", "rn"))).eq(0f), field(name("cte_wma", "sales"), Float.class).mul(0.4f))
                                        .when(field(name("cte_wma", "rn"), Float.class)
                                                .minus(field(name("t2", "rn"))).eq(1f), field(name("cte_wma", "sales"), Float.class).mul(0.3f))
                                        .when(field(name("cte_wma", "rn"), Float.class)
                                                .minus(field(name("t2", "rn"))).eq(2f), field(name("cte_wma", "sales"), Float.class).mul(0.2f))
                                        .when(field(name("cte_wma", "rn"), Float.class)
                                                .minus(field(name("t2", "rn"))).eq(3f), field(name("cte_wma", "sales"), Float.class).mul(0.1f))
                        ))
                .from(name("cte_wma"))
                .join(table(name("cte_wma")).as("t2"))
                .on(field(name("t2", "rn")).between(field(name("cte_wma", "rn")).minus(3), field(name("cte_wma", "rn"))))
                .groupBy(field(name("cte_wma", "day")))
                .orderBy(field(name("cte_wma", "day")))
                .fetch();
    }
}
