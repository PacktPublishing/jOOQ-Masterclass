package com.classicmodels.repository;

import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.case_;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.floor;
import static org.jooq.impl.DSL.generateSeries;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.ntile;
import static org.jooq.impl.DSL.percentRank;
import static org.jooq.impl.DSL.rand;
import static org.jooq.impl.DSL.repeat;
import static org.jooq.impl.DSL.round;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.sin;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.unnest;
import static org.jooq.impl.DSL.widthBucket;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // UNNEST
    public void cte1() {

        ctx.with("dt")
                .as(select().from(unnest(new String[]{"John", "Mary", "Kelly"}).as("n")))
                .select()
                .from(name("dt"))
                .fetch();
    }

    // GENERATE_SERIES
    public void cte2() {

        ctx.with("dt")
                .as(select().from(generateSeries(1, 10, 2).as("t", "s")))
                .select()
                .from(name("dt"))
                .fetch();

        // CUSTOM BINNING OF GRADES
        ctx.with("grades")
                .as(select(round(inline(70).plus(sin(field(name("serie", "sample"), Integer.class)).mul(30))).as("grade"))
                        .from(generateSeries(1, 100).as("serie", "sample")))
                .select(
                        case_().when(field(name("grade")).lt(60), "F")
                                .when(field(name("grade")).lt(70), "D")
                                .when(field(name("grade")).lt(80), "C")
                                .when(field(name("grade")).lt(90), "B")
                                .else_("A").as("letter_grade"),
                        count()
                ).from(name("grades"))
                .groupBy(field(name("letter_grade")))
                .orderBy(field(name("letter_grade")))
                .fetch();

        // CUSTOM BINNING OF GRADES VIA PERCENT_RANK           
        ctx.with("grades")
                .as(select(round(inline(70).plus(sin(field(name("serie", "sample"), Integer.class)).mul(30))).as("grade"))
                        .from(generateSeries(1, 100).as("serie", "sample")))
                .with("percent_grades")
                .as(select(percentRank().over().orderBy(field("grade")).as("percent_grade"))
                        .from(name("grades")))
                .select(
                        case_().when(field(name("percent_grade")).lt(0.6), "F")
                                .when(field(name("percent_grade")).lt(0.7), "D")
                                .when(field(name("percent_grade")).lt(0.8), "C")
                                .when(field(name("percent_grade")).lt(0.9), "B")
                                .else_("A").as("letter_grade"),
                        count()
                ).from(name("percent_grades"))
                .groupBy(field(name("letter_grade")))
                .orderBy(field(name("letter_grade")))
                .fetch();

        //EQUAL HEIGHT BINNING           
        ctx.with("grades")
                .as(select(round(inline(70).plus(sin(field(name("serie", "sample"), Integer.class)).mul(30))).as("grade"))
                        .from(generateSeries(1, 100).as("serie", "sample")))
                .with("grades_with_tiles")
                .as(select(field(name("grade")), ntile(10).over().orderBy(field(name("grade"))).as("bucket"))
                        .from(name("grades")))
                .select(min(field(name("grade"))).as("from_grade"),
                        max(field(name("grade"))).as("to_grade"),
                        count().as("cnt"),
                        field(name("bucket")))
                .from(name("grades_with_tiles"))
                .groupBy(field(name("bucket")))
                .orderBy(field(name("from_grade")))
                .fetch();

        // EQUAL WIDTH BINNING                
        ctx.with("grades")
                .as(select(round(inline(70).plus(sin(field(name("serie", "sample"), Integer.class)).mul(30))).as("grade"))
                        .from(generateSeries(1, 100).as("serie", "sample")))
                .select(floor((field("grade", Integer.class).minus(1)).divide(10)).as("bucket"),
                        min(field(name("grade"))).as("from_grade"),
                        max(field(name("grade"))).as("to_grade"),
                        count().as("cnt"))
                .from(name("grades"))
                .groupBy(field(name("bucket")))
                .orderBy(field(name("bucket")))
                .fetch();

        // PostgreSQL provides the function width_bucket that simplifies the previous query                      
        ctx.with("grades")
                .as(select(round(inline(70).plus(sin(field(name("serie", "sample"), Integer.class)).mul(30))).as("grade"))
                        .from(generateSeries(1, 100).as("serie", "sample")))
                .select(widthBucket(field(name("grade"), Integer.class), 0, 101, 20).as("bucket"),
                        (widthBucket(field(name("grade"), Integer.class), 0, 101, 20).minus(1)).mul(5).as("low_bound"),
                        widthBucket(field(name("grade"), Integer.class), 0, 101, 20).mul(5).as("high_bound"),
                        count().as("cnt"))
                .from(name("grades"))
                .groupBy(field(name("bucket")), field(name("low_bound")), field(name("high_bound")))
                .orderBy(field(name("bucket")))
                .fetch();

        // BINNING WITH CHART        
        ctx.with("grades")
                .as(select(round(inline(70).plus(sin(field(name("serie", "sample"), Integer.class)).mul(30))).as("grade"))
                        .from(generateSeries(1, 100).as("serie", "sample")))
                .with("buckets")
                .as(select(field(name("serie", "bucket")), (field(name("serie", "bucket")).minus(1)).mul(5).plus(1).as("low_bound"),
                        field(name("serie", "bucket")).mul(5).as("high_bound"))
                        .from(generateSeries(1, 20).as("serie", "bucket")))
                .select(concat(field(name("low_bound")), inline("-"), field(name("high_bound"))).as("bounds"),
                        count(field(name("grade"))).as("cnt"),
                        repeat("+", count(field(name("grade"))).cast(Integer.class)).as("chart"))
                .from(name("buckets"))
                .leftJoin(name("grades"))
                .on(field(name("grade")).between(field(name("low_bound")), field(name("high_bound"))))
                .groupBy(field(name("bucket")), field(name("low_bound")), field(name("high_bound")))
                .orderBy(field(name("bucket")))
                .fetch();

        ctx.with("sales_stats")
                .as(select(min(ORDERDETAIL.ORDER_ID).as("min"), max(ORDERDETAIL.ORDER_ID).as("max"))
                        .from(ORDERDETAIL))
                .with("histogram")
                .as(select(widthBucket(ORDERDETAIL.ORDER_ID.coerce(Integer.class), field(name("min"), Integer.class),
                        field(name("max"), Integer.class), inline(20)).as("bucket"),
                        concat(min(ORDERDETAIL.ORDER_ID), inline("-"), max(ORDERDETAIL.ORDER_ID)).as("range"),
                        sum(ORDERDETAIL.QUANTITY_ORDERED).as("sum"))
                        .from(ORDERDETAIL, table(name("sales_stats")))
                        .groupBy(field(name("bucket")))
                        .orderBy(field(name("bucket"))))
                .select(field(name("bucket")), field(name("range")), field(name("sum")),
                        repeat("+", field(name("sum")).divide(1000).cast(Integer.class)).as("bar"))
                .from(name("histogram"))
                .fetch();
    }

    // RANDOM CHOICE FROM ARRAY
    public void cte3() {

        ctx.with("dt")
                .as(select().from(unnest(new String[]{"John", "Mary", "Kelly"}).as("n")))
                .select()
                .from(name("dt"))
                .orderBy(rand())
                .limit(1)
                .fetch();
    }

    // SAMPLING
    public void cte4() {

        ctx.with("dt")
                .as(selectFrom(PRODUCT).orderBy(rand()).limit(10))
                .select()
                .from(name("dt"))
                .fetch();
    }
}
