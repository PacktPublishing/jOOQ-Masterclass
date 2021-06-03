package com.classicmodels.repository;

import java.time.LocalDate;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.case_;
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
import static org.jooq.impl.DSL.rowNumber;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.selectOne;
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
        
        // A famous problem expressed via CTE:
        // You have n student classes of known size, and m classrooms of known size, where m>=n. 
        // What's the best algorithm for assigning as many classes as possible to rooms of adequate size?
        ctx.with("classes")
                .as(select()
                        .from(values(row("c1", 80), row("c2", 70), row("c3", 65),
                                row("c4", 55), row("c5", 50), row("c6", 40))
                                .as("t", "class_nbr", "class_size")))
                .with("rooms")
                .as(select()
                        .from(values(row("r1", 70), row("r2", 40), row("r3", 50),
                                row("r4", 85), row("r5", 30), row("r6", 65), row("r7", 55))
                                .as("t", "room_nbr", "room_size")))
                .with("matches")
                .as(select(field(name("class_nbr")), field(name("class_size")),
                        field(name("room_nbr")), field(name("room_size")),
                        case_().when(field(name("class_size"))
                                .eq(field(name("room_size"))), 1).else_(0).as("exact_match"))
                        .from(name("classes"))
                        .join(name("rooms"))
                        .on(field(name("class_size")).le(field(name("room_size")))))
                .with("preferences")
                .as(select(field(name("class_nbr")), field(name("class_size")),
                        rowNumber().over().partitionBy(field(name("class_nbr")))
                                .orderBy(field(name("exact_match")), field(name("room_size")),
                                        field(name("room_nbr"))).as("class_room_pref"),
                        field(name("room_nbr")), field(name("room_size")),
                        rowNumber().over().partitionBy(field(name("room_nbr")))
                                .orderBy(field(name("exact_match")), field(name("class_size")).desc(),
                                        field(name("class_nbr"))).as("room_class_pref"))
                        .from(table(name("matches")).as("m"))
                        .whereNotExists(selectOne().from(name("matches"))
                                .where(field(name("room_nbr")).eq(field(name("m", "room_nbr")))
                                        .and(field(name("class_size")).gt(field(name("m", "class_size")))))))
                .with("final")
                .as(select(field(name("class_nbr")), field(name("class_size")),
                        field(name("room_nbr")), field(name("room_size")),
                        rowNumber().over().partitionBy(field(name("class_nbr")))
                                .orderBy(field(name("class_room_pref"))).as("final_pref"))
                        .from(table(name("preferences")).as("p"))
                        .whereNotExists(selectOne().from(name("preferences"))
                                .where(field(name("room_nbr")).eq(field(name("p", "room_nbr")))
                                        .and(field(name("class_room_pref")).eq(field(name("room_class_pref")))
                                                .and(field(name("room_class_pref")).lt(field(name("p", "room_class_pref"))))))))
                .select(field(name("c", "class_nbr")), field(name("c", "class_size")),
                        field(name("f", "room_nbr")), field(name("f", "room_size")))
                .from(table(name("classes")).as("c"))
                .leftJoin(table(name("final")).as("f"))
                .on(field(name("c", "class_nbr")).eq(field(name("f", "class_nbr")))
                        .and(field(name("f", "final_pref")).eq(1)))
                .orderBy(1)
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
