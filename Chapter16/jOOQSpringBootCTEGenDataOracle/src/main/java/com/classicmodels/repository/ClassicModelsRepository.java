package com.classicmodels.repository;

import java.time.LocalDate;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.generateSeries;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.percentileCont;
import static org.jooq.impl.DSL.rand;
import static org.jooq.impl.DSL.repeat;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.stddevSamp;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.unnest;
import static org.jooq.impl.DSL.values;
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

    // VALUES LIST
    public void cte1() {

        ctx.with("dt")
                .as(select()
                        .from(values(row(1, "John"), row(2, "Mary"), row(3, "Kelly"))
                                .as("t", "id", "name")))
                .select()
                .from(name("dt"))
                .fetch();

        ctx.with("dt")
                .as(select()
                        .from(values(row(LocalDate.of(2021, 1, 1), 10),
                                row(LocalDate.of(2021, 1, 2), 12),
                                row(LocalDate.of(2021, 1, 3), 13),
                                row(LocalDate.of(2021, 1, 4), 14),
                                row(LocalDate.of(2021, 1, 5), 18),
                                row(LocalDate.of(2021, 1, 6), 15),
                                row(LocalDate.of(2021, 1, 7), 16),
                                row(LocalDate.of(2021, 1, 8), 17))
                                .as("t", "day", "temp")))
                .select(field(name("day")), field(name("temp")),
                        max(field(name("temp"))).over().orderBy(field(name("day")))
                                .rowsBetweenPreceding(2).andCurrentRow()
                                .as("hottest_temperature_last_three_days"))
                .from(name("dt"))
                .fetch();
    }

    // UNNEST
    public void cte2() {

        ctx.with("dt")
                .as(select().from(unnest(new String[]{"John", "Mary", "Kelly"}).as("n")))
                .select()
                .from(name("dt"))
                .fetch();
    }

    // GENERATE_SERIES
    public void cte3() {

        ctx.with("dt")
                .as(select().from(generateSeries(1, 10, 2).as("t", "s")))
                .select()
                .from(name("dt"))
                .fetch();

        ctx.with("dt")
                .as(select().from(generateSeries(1, 3).as("t", "s")))
                .select(
                        count(),
                        avg(field(name("s"), Integer.class)),
                        stddevSamp(field(name("s"), Integer.class)),
                        min(field(name("s"))),
                        percentileCont(0.5).withinGroupOrderBy(field(name("s"))),
                        max(field(name("s"))))
                .from(name("dt"))
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
                        .groupBy(widthBucket(ORDERDETAIL.ORDER_ID.coerce(Integer.class), field(name("min"), Integer.class),
                                field(name("max"), Integer.class), inline(20)))
                        .orderBy(1))
                .select(field(name("bucket")), field(name("range")), field(name("sum")),
                        repeat("+", field(name("sum")).divide(1000).cast(Integer.class)).as("bar"))
                .from(name("histogram"))
                .fetch();
    }

    // RANDOM CHOICE FROM ARRAY
    public void cte4() {

        ctx.with("dt")
                .as(select().from(unnest(new String[]{"John", "Mary", "Kelly"}).as("n")))
                .select()
                .from(name("dt"))
                .orderBy(rand())
                .limit(1)
                .fetch();
    }

    // SAMPLING
    public void cte5() {

        ctx.with("dt")
                .as(selectFrom(PRODUCT).orderBy(rand()).limit(10))
                .select()
                .from(name("dt"))
                .fetch();
    }
}
