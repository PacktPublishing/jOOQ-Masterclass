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

        ctx.withRecursive("FLIGHTS", "ARRIVAL_TOWN", "STEPS", "TOTAL_DISTANCE_KM", "PATH")
                .as(selectDistinct(OFFICE_FLIGHTS.DEPART_TOWN.as("ARRIVAL_TOWN"),
                        inline(0).as("STEPS"), inline(0).as("TOTAL_DISTANCE_KM"),
                        cast(from, SQLDataType.VARCHAR).as("PATH"))
                        .from(OFFICE_FLIGHTS)
                        .where(OFFICE_FLIGHTS.DEPART_TOWN.eq(from))
                        .unionAll(select(field(name("ARRIVALS", "ARRIVAL_TOWN"), String.class),
                                field(name("FLIGHTS", "STEPS"), Integer.class).plus(1),
                                field(name("FLIGHTS", "TOTAL_DISTANCE_KM"), Integer.class).plus(field(name("ARRIVALS", "DISTANCE_KM"))),
                                concat(field(name("FLIGHTS", "PATH")), inline(","), field(name("ARRIVALS", "ARRIVAL_TOWN"))))
                                .from(OFFICE_FLIGHTS.as("ARRIVALS"), table(name("FLIGHTS")))
                                .where(field(name("FLIGHTS", "ARRIVAL_TOWN")).eq(field(name("ARRIVALS", "DEPART_TOWN")))
                                        .and(field(name("FLIGHTS", "PATH")).notLike(concat(inline("%"),
                                                field(name("ARRIVALS", "ARRIVAL_TOWN")), inline("%")))))))
                .select()
                .from(name("FLIGHTS"))
                .where(field(name("ARRIVAL_TOWN")).eq(to))
                .orderBy(field(name("TOTAL_DISTANCE_KM")))
                .fetch();
    }
}