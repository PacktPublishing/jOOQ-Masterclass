package com.classicmodels.repository;

import static jooq.generated.tables.OfficeFlights.OFFICE_FLIGHTS;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectDistinct;
import static org.jooq.impl.DSL.table;
import org.jooq.impl.SQLDataType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // Fibonacci number
    public void cte1() {

        ctx.withRecursive("fibonacci", "n", "f", "f1")
                .as(select(inline(1L), inline(0L), inline(1L))
                        .unionAll(select(field(name("n"), Long.class).plus(1),
                                field(name("f"), Long.class).plus(field(name("f1"))),
                                field(name("f"), Long.class))
                                .from(name("fibonacci"))
                                .where(field(name("n")).lt(20))))
                .select(field(name("n")), field(name("f")).as("f_nbr"))
                .from(name("fibonacci"))
                .fetch();
    }

    // Solving the Traveling Salesman Problem with Recursive CTEs
    public void cte2() {

        String from = "Los Angeles";
        String to = "Tokyo";

        ctx.withRecursive("flights", "arrival_town", "steps", "total_distance_km", "path")
                .as(selectDistinct(OFFICE_FLIGHTS.DEPART_TOWN.as("arrival_town"),
                        inline(0).as("steps"), inline(0).as("total_distance_km"),
                        cast(from, SQLDataType.VARCHAR).as("path"))
                        .from(OFFICE_FLIGHTS)
                        .where(OFFICE_FLIGHTS.DEPART_TOWN.eq(from))
                        .unionAll(select(field(name("arrivals", "arrival_town"), String.class),
                                field(name("flights", "steps"), Integer.class).plus(1),
                                field(name("flights", "total_distance_km"), Integer.class).plus(field(name("arrivals", "distance_km"))),
                                concat(field(name("flights", "path")), inline(","), field(name("arrivals", "arrival_town"))))
                                .from(OFFICE_FLIGHTS.as("arrivals"), table(name("flights")))
                                .where(field(name("flights", "arrival_town")).eq(field(name("arrivals", "depart_town")))
                                        .and(field(name("flights", "path")).notLike(concat(inline("%"),
                                                field(name("arrivals", "arrival_town")), inline("%")))))))
                .select()
                .from(name("flights"))
                .where(field(name("arrival_town")).eq(to))
                .orderBy(field(name("total_distance_km")))
                .fetch();
    }
}