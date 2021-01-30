package com.classicmodels.controller;

import java.sql.ResultSet;
import java.time.Duration;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ClassicModelsController {

    private final DSLContext ctx;

    public ClassicModelsController(DSLContext ctx) {
        this.ctx = ctx;
    }

    @GetMapping(value = "/test", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> meetupRsvps() {

        return Flux.fromStream(ctx.select(SALE.SALE_, SALE.SALE_ID).from(SALE)
               // .resultSetType(ResultSet.FETCH_FORWARD)
               // .resultSetConcurrency(ResultSet.CONCUR_READ_ONLY)
                .fetchSize(5).fetchStream())
                .map(r -> "\"{"+r.get(SALE.SALE_ID) + "\"}:\"{" + r.get(SALE.SALE_) + "\"}")
                .delayElements(Duration.ofSeconds(15));
    }
}
