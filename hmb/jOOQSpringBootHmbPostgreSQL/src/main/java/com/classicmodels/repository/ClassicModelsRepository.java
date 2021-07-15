package com.classicmodels.repository;

import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void sampleReactiveQuery() {

        Flux.from(ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME)
                .from(EMPLOYEE))
                .map(r -> r.get(EMPLOYEE.FIRST_NAME) + " " + r.get(EMPLOYEE.LAST_NAME))
                .doOnNext(System.out::println)
                .subscribe();

        Flux.from(ctx.select(OFFICE.CITY, OFFICE.COUNTRY)
                .from(OFFICE))
                .map(r -> r.get(OFFICE.CITY) + " " + r.get(OFFICE.COUNTRY))
                .doOnNext(System.out::println)
                .subscribe();
    }
}
