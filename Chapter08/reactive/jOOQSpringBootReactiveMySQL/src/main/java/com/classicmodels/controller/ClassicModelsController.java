package com.classicmodels.controller;

import java.time.Duration;
import static jooq.generated.tables.Employee.EMPLOYEE;
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

    @GetMapping(value = "/employees", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> fetchEmployees() {

        return Flux.from(ctx.select(EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, 
                EMPLOYEE.JOB_TITLE, EMPLOYEE.SALARY).from(EMPLOYEE))
                .map(e -> e.formatHTML())
                .delayElements(Duration.ofSeconds(2))
                .share();
    }
}
