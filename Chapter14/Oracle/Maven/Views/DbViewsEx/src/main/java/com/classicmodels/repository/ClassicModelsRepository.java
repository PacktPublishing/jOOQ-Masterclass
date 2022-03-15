package com.classicmodels.repository;

import java.time.LocalDate;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import org.jooq.DSLContext;
import org.jooq.Table;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.cumeDist;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.round;
import static org.jooq.impl.DSL.rowNumber;
import static org.jooq.impl.DSL.not;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.with;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.values;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // Compute the cumulative distribution values by headcount of each office
    public void view1() {

        ctx.dropViewIfExists("office_headcounts").execute();
        ctx.createView("office_headcounts", "office_code", "headcount")
                .as(select(OFFICE.OFFICE_CODE, count(EMPLOYEE.EMPLOYEE_NUMBER))
                        .from(OFFICE)
                        .innerJoin(EMPLOYEE)
                        .on(OFFICE.OFFICE_CODE.eq(EMPLOYEE.OFFICE_CODE))
                        .groupBy(OFFICE.OFFICE_CODE))
                .execute();

        ctx.select(field(name("office_code")), field(name("headcount")),
                round(cumeDist().over().orderBy(field(name("headcount"))).mul(100), 2)
                        .concat("%")
                        .as("cume_dist_val"))
                .from(name("office_headcounts"))
                .fetch();
    }

    public void view2() {

        ctx.dropViewIfExists("absent_values").execute();

        ctx.createView("absent_values", "data_val", "data_seq", "absent_data_grp").as(
                with("t", "data_val", "data_seq", "absent_data_grp")
                        .as(select(EMPLOYEE.EMPLOYEE_NUMBER,
                                rowNumber().over().orderBy(EMPLOYEE.EMPLOYEE_NUMBER),
                                EMPLOYEE.EMPLOYEE_NUMBER.minus(
                                        rowNumber().over().orderBy(EMPLOYEE.EMPLOYEE_NUMBER)))
                                .from(EMPLOYEE))
                        .select(field(name("absent_data_grp")), count(),
                                min(field(name("data_val"))).as("start_data_val"))
                        .from(name("t"))
                        .groupBy(field(name("absent_data_grp"))))
                .execute();

        System.out.println(
                ctx.select().from(name("absent_values")).fetch()
        );
    }

    public void view3() {

        // 2003 trucks availability for shipping orders
        Table truck = select().from(values(
                row("Truck1", LocalDate.of(2003, 1, 1), LocalDate.of(2003, 1, 12)),
                row("Truck2", LocalDate.of(2003, 1, 8), LocalDate.of(2003, 1, 27)),
                row("Truck3", LocalDate.of(2003, 2, 10), LocalDate.of(2003, 2, 26)),
                row("Truck4", LocalDate.of(2003, 4, 4), LocalDate.of(2003, 4, 26)),
                row("Truck5", LocalDate.of(2003, 4, 14), LocalDate.of(2003, 5, 10)),
                row("Truck6", LocalDate.of(2003, 6, 20), LocalDate.of(2003, 7, 1)),
                row("Truck7", LocalDate.of(2003, 5, 4), LocalDate.of(2003, 6, 15)),
                row("Truck8", LocalDate.of(2003, 6, 10), LocalDate.of(2003, 6, 30)),
                row("Truck9", LocalDate.of(2003, 6, 25), LocalDate.of(2003, 7, 10)),
                row("Truck10", LocalDate.of(2003, 7, 11), LocalDate.of(2003, 7, 15)),
                row("Truck11", LocalDate.of(2003, 7, 13), LocalDate.of(2003, 8, 3)),
                row("Truck12", LocalDate.of(2003, 7, 21), LocalDate.of(2003, 8, 25)),
                row("Truck13", LocalDate.of(2003, 8, 11), LocalDate.of(2003, 9, 15)),
                row("Truck14", LocalDate.of(2003, 10, 6), LocalDate.of(2003, 10, 20)),
                row("Truck15", LocalDate.of(2003, 11, 1), LocalDate.of(2003, 11, 20))
        )).asTable("truck", "truck_id", "free_from", "free_to");

        ctx.dropViewIfExists("order_truck_exact").execute();
        ctx.dropViewIfExists("order_truck_all").execute();
        ctx.dropViewIfExists("order_truck").execute();

        // create view 'order_truck' - which trucks are available during each order
        ctx.createView("order_truck", "truck_id", "order_id")
                .as(select(field(name("truck_id")), ORDER.ORDER_ID)
                        .from(truck, ORDER)
                        .where(not(field(name("free_to")).lt(ORDER.ORDER_DATE)
                                .or(field(name("free_from")).gt(ORDER.REQUIRED_DATE)))))
                .execute();

        System.out.println("'order_truck' view:\n"
                + ctx.select().from(name("order_truck")).orderBy(field(name("order_id"))).fetch()
        );

        // how many orders can be shipped by each truck
        System.out.println("How many orders can be shipped by each truck:\n"
                + ctx.select(field(name("truck_id")), count().as("order_count"))
                        .from(name("order_truck"))
                        .groupBy(field(name("truck_id")))
                        .fetch());

        // how many trucks can ship the same order
        System.out.println("How many trucks can ship the same order:\n"
                + ctx.select(field(name("order_id")), count().as("truck_count"))
                        .from(name("order_truck"))
                        .groupBy(field(name("order_id")))
                        .fetch());

        // create view 'order_truck_all' - earliest and latest points in both intervals
        ctx.createView("order_truck_all", "truck_id", "order_id", "entry_date", "exit_date")
                .as(select(field(name("t", "truck_id")), field(name("t", "order_id")),
                        ORDER.ORDER_DATE, ORDER.REQUIRED_DATE)
                        .from(table(name("order_truck")).as("t"), ORDER)
                        .where(ORDER.ORDER_ID.eq(field(name("t", "order_id"), Long.class)))
                        .union(select(field(name("t", "truck_id")), field(name("t", "order_id")),
                                truck.field(name("free_from"), LocalDate.class), 
                                truck.field(name("free_to"), LocalDate.class))
                                .from(table(name("order_truck")).as("t"), truck)
                                .where(truck.field(name("truck_id"), String.class)
                                        .eq(field(name("t", "truck_id"), String.class)))))
                .execute();

        System.out.println(
                ctx.select().from(name("order_truck_all")).orderBy(field(name("truck_id"))).fetch()
        );

        // create view 'order_truck_exact' - exact points in both intervals
        ctx.createView("order_truck_exact", "truck_id", "order_id", "entry_date", "exit_date")
                .as(select(field(name("truck_id")), field(name("order_id")),
                        max(field(name("entry_date"))), min(field(name("exit_date"))))
                        .from(name("order_truck_all"))
                        .groupBy(field(name("truck_id")), field(name("order_id"))))
                .execute();

        System.out.println(
                ctx.select().from(name("order_truck_exact")).orderBy(field(name("truck_id"))).fetch()
        );
    }
}
